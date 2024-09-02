package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.Font.BOLD;
import static org.example.StartMenu.username;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    /* WIDTH = Breite der Anwendung */
    private static final int WIDTH = 800;
    /* HEIGHT = Höhe der Anwendung */
    private static final int HEIGHT = 600;


    private static final int BOX_SIZE = 20;
    private static final int NUM_BOXES_X = WIDTH / BOX_SIZE;
    private static final int NUM_BOXES_Y = HEIGHT / BOX_SIZE;

    /* Standartfarbe der Schlange  - DUNKELGRÜN*/
    private static Color snakeColor = new Color(34, 139, 34);
    /* Standart Hintergrundfarbe  - SCHWARZ*/
    private static Color backgroundColor = new Color(0, 0, 0);
    /* Farbe des "Essens" - ROT & ORANGE*/
    private static final Color FOOD_COLOR = new Color(255, 69, 0);
    /* Farbe der Umrandung */
    private static final Color BORDER_COLOR = new Color(183, 64, 106); // White
    /* Farbe des "Score" Schrift */
    private static final Color SCORE_COLOR = Color.WHITE;

    private ArrayList<Point> snake;
    private Point food;
    private int direction;
    private boolean running;
    private boolean paused;
    private boolean gameOver;
    private int score;
    private Timer timer;
    private BufferedImage canvas;
    private Graphics2D canvasGraphics;

    public SnakeGame(JFrame frame, String username) {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(backgroundColor);
        this.setFocusable(true);
        this.addKeyListener(this);

        snake = new ArrayList<>();
        snake.add(new Point(NUM_BOXES_X / 2, NUM_BOXES_Y / 2));
        direction = KeyEvent.VK_RIGHT;
        running = true;
        paused = false;
        gameOver = false;
        score = 0;

        spawnFood();

        canvas = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        canvasGraphics = canvas.createGraphics();
        canvasGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        timer = new Timer(100, this);
        timer.start();
    }

    private void spawnFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(NUM_BOXES_X), rand.nextInt(NUM_BOXES_Y));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw everything onto the BufferedImage
        canvasGraphics.setColor(backgroundColor);
        canvasGraphics.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw snake
        canvasGraphics.setColor(snakeColor);
        for (Point p : snake) {
            canvasGraphics.fillRoundRect(p.x * BOX_SIZE, p.y * BOX_SIZE, BOX_SIZE, BOX_SIZE, 10, 10);
        }

        // Draw food
        canvasGraphics.setColor(FOOD_COLOR);
        canvasGraphics.fillRoundRect(food.x * BOX_SIZE, food.y * BOX_SIZE, BOX_SIZE, BOX_SIZE, 10, 10);

        // Draw border
        canvasGraphics.setColor(BORDER_COLOR);
        canvasGraphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        // Draw score
        canvasGraphics.setColor(SCORE_COLOR);
        canvasGraphics.setFont(new Font("Segoe UI", BOLD, 20));
        String scoreText = "Score: " + score;
        FontMetrics fm = canvasGraphics.getFontMetrics();
        canvasGraphics.drawString(scoreText, 10, 20);

        // Draw username
        canvasGraphics.setFont(new Font("Segoe UI", BOLD, 16));
        canvasGraphics.setColor(Color.YELLOW);
        canvasGraphics.drawString(username, WIDTH - 100, 20);


        // Draw game over screen if game over
        if (gameOver) {
            //JOptionPane.showMessageDialog(null, Font.BOLD + "Game Over");

            String message = "<html><span style='color:red;'><b>Game Over / Score: "+score+"</b></span></html>";

            // Zeige das Dialogfeld an
            JOptionPane.showMessageDialog(null, message, "Spielende", JOptionPane.INFORMATION_MESSAGE);

            System.exit(0);
        }

        // Draw the BufferedImage onto the JPanel
        g.drawImage(canvas, 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !paused && !gameOver) {
            moveSnake();
            checkCollisions();
            checkFood();
            repaint();
        }
    }

    private void moveSnake() {
        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case KeyEvent.VK_UP:
                newHead.translate(0, -1);
                break;
            case KeyEvent.VK_DOWN:
                newHead.translate(0, 1);
                break;
            case KeyEvent.VK_LEFT:
                newHead.translate(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
                newHead.translate(1, 0);
                break;
        }

        // Add new head and remove tail
        snake.add(0, newHead);
        if (!newHead.equals(food)) {
            snake.remove(snake.size() - 1);
        } else {
            score += 10;
            spawnFood();
        }
    }

    private void checkCollisions() {
        Point head = snake.get(0);

        // Check collision with walls
        if (head.x < 0 || head.x >= NUM_BOXES_X || head.y < 0 || head.y >= NUM_BOXES_Y) {
            gameOver = true;
            running = false;
            saveScoreToDatabase(); // Save score when game is over
        }

        // Check collision with itself
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                running = false;
                saveScoreToDatabase(); // Save score when game is over
            }
        }
    }

    private void checkFood() {
        if (food.equals(snake.get(0))) {
            score += 10;
            spawnFood();
        }
    }

    private void saveScoreToDatabase() {
        if(username.equals("nA")){
            return;
        } else {
            DatabaseUtil.saveScore(username, score);
            System.out.println("Score von "+username+" gespeichert!");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int newDirection = e.getKeyCode();

        if (newDirection == KeyEvent.VK_E) {
            paused = !paused;
            if (paused) {
                timer.stop();

                String message = "<html><span style='color:GREY;'><b> Spiel Pausiert</b></span></html>";
                String[] options = {"Fortsetzen"};  // Text des Buttons

                int result = JOptionPane.showOptionDialog(
                    null,
                    message,
                    "Pausiert",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
                );


                if(result == JOptionPane.OK_OPTION){
                    timer.start();
                    paused = false;
                }
            } else {
                timer.start();
            }
        } else if (newDirection == KeyEvent.VK_M) {
            score += 10000;
        } else if (newDirection == KeyEvent.VK_ESCAPE) {
            // Save score to database and exit the game when ESC is pressed
            if (!gameOver) {
                saveScoreToDatabase();
            }
            String message = "<html><span style='color:red;'><b> Spiel wird geschlossen</b></span></html>";

            // Zeige das Dialogfeld an
            timer.stop();
            JOptionPane.showMessageDialog(null, message, "Spielende", JOptionPane.INFORMATION_MESSAGE);

            System.exit(0);

        } else if (!paused && !gameOver) {
            if (newDirection == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) {
                direction = newDirection;
            } else if (newDirection == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) {
                direction = newDirection;
            } else if (newDirection == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) {
                direction = newDirection;
            } else if (newDirection == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
                direction = newDirection;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static Color getSnakeColor() {
        return snakeColor;
    }

    public static void setSnakeColor(Color color) {
        snakeColor = color;
    }

    public static Color getBackgroundColor() {
        return backgroundColor;
    }

    public static void setBackgroundColor(Color color) {
        backgroundColor = color;
    }
}
