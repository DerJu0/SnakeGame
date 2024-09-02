package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import static java.awt.Font.BOLD;

public class StartMenu extends JPanel {
    public static String username;
    private JFrame frame;
    private SnakeGame game;
    private BufferedImage backgroundImage;
    private JTextField usernameField;

    public StartMenu(JFrame frame) {
        this.frame = frame;
        this.setLayout(new GridBagLayout()); // Verwende GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();

        // Lade das Hintergrundbild von einer URL
        try {
            URL url = new URL("https://wallpaperaccess.com/full/6233787.jpg"); // Ersetze mit deiner URL
            backgroundImage = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            // Falls das Bild nicht geladen werden kann, zeige einen Farbblock an
            backgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = backgroundImage.createGraphics();
            g2d.setColor(Color.GRAY);
            g2d.fillRect(0, 0, 800, 600);
            g2d.dispose();
        }

        // Titel
        JLabel titleLabel = new JLabel("Snake Game");
        titleLabel.setFont(new Font("Segoe UI", BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0); // Abstand nach oben und unten
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(titleLabel, gbc);

        // Eingabefeld für den Benutzernamen
        JLabel usernameLabel = new JLabel("Nutzername: ");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
        usernameLabel.setForeground(Color.YELLOW);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        gbc.gridy = 1;
        this.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(300, 40));

        gbc.gridy = 2;
        this.add(usernameField, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(40, 0, 0, 0); // Abstand nach unten für den Startbutton

        // Buttons
        JButton startButton = createModernButton("Start Game");
        startButton.addActionListener(e -> startGame());
        this.add(startButton, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(20, 0, 0, 0);
        JButton colorButton = createModernButton("Snake Color");
        colorButton.addActionListener(e -> changeSnakeColor());
        this.add(colorButton, gbc);

        gbc.gridy = 5;
        JButton bgColorButton = createModernButton("Hintergrundfarbe");
        bgColorButton.addActionListener(e -> changeBackgroundColor());
        this.add(bgColorButton, gbc);

        gbc.gridy = 6;
        JButton quitButton = createModernButton("Schließen");
        quitButton.addActionListener(e -> System.exit(0));
        this.add(quitButton, gbc);
    }

    private JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(45, 45, 45));
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(200, 50));

        // Abgerundete Ecken und Schatten
        button.setUI(new RoundedCornerButtonUI());

        return button;
    }

    private void startGame() {
        username = usernameField.getText().trim();
        if (username.isEmpty()) {

            username = "nA";
        }

        game = new SnakeGame(frame, username);
        frame.setContentPane(game);
        frame.revalidate();
        game.requestFocusInWindow();
    }

    private void changeSnakeColor() {
        Color newColor = JColorChooser.showDialog(frame, "Snake Color", SnakeGame.getSnakeColor());
        if (newColor != null) {
            SnakeGame.setSnakeColor(newColor);
        }
    }

    private void changeBackgroundColor() {
        Color newColor = JColorChooser.showDialog(frame, "Hintergrundfarbe", SnakeGame.getBackgroundColor());
        if (newColor != null) {
            SnakeGame.setBackgroundColor(newColor);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Zeichne das Hintergrundbild
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Halbtransparenter Overlay für besseren Kontrast
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    // Custom UI für runde Ecken
    private static class RoundedCornerButtonUI extends javax.swing.plaf.basic.BasicButtonUI {
        private static final int ARC_WIDTH = 40;
        private static final int ARC_HEIGHT = 40;

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            JButton button = (JButton) c;
            button.setOpaque(false);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            JButton button = (JButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Hintergrund
            g2.setColor(button.getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, button.getWidth(), button.getHeight(), ARC_WIDTH, ARC_HEIGHT));

            // Text
            g2.setColor(button.getForeground());
            g2.setFont(button.getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (button.getWidth() - fm.stringWidth(button.getText())) / 2;
            int y = (button.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(button.getText(), x, y);

            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize(JComponent c) {
            JButton button = (JButton) c;
            FontMetrics fm = button.getFontMetrics(button.getFont());
            int width = fm.stringWidth(button.getText()) + 40;
            int height = fm.getHeight() + 20;
            return new Dimension(width, height);
        }
    }
}
