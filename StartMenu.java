package de.eternal5.gui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static de.eternal5.gui.Utilities.showError;

public class StartMenu {
    private SnakeGame snakeGame;
    public static String username;

    public StartMenu(SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
        showMenu();
    }

    private void showMenu() {
        Stage stage = new Stage();
        stage.setTitle("Snake Game - Startmenü");

        // GUI-Elemente
        Label usernameLabel = new Label("Benutzername:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Created by: ETERNAL5/Julian Schwendt");
        Label snakeColorLabel = new Label("Wähle die Schlange Farbe:");
        ColorPicker snakeColorPicker = new ColorPicker(Color.GREEN);

        Label backgroundColorLabel = new Label("Wähle den Hintergrund Farbe:");
        ColorPicker backgroundColorPicker = new ColorPicker(Color.WHITE);

        // Slider für die Geschwindigkeit
        Label speedLabel = new Label("Geschwindigkeit der Schlange (ms):");
        Slider speedSlider = new Slider(25, 100, 50);  // Slider von 25 bis 100 ms
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);

        // Textfeld für die Geschwindigkeitseingabe (Startwert aus dem Slider)
        TextField speedField = new TextField(String.valueOf((int) speedSlider.getValue()));

        // Synchronisiere den Slider mit dem Textfeld (Slider -> Textfeld)
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            speedField.setText(String.valueOf(newVal.intValue()));
        });

        // Erlaube freie Eingabe von Geschwindigkeiten im Textfeld (Textfeld -> Slider)
        speedField.setOnAction(e -> {
            try {
                int speed = Integer.parseInt(speedField.getText());
                if (speed >= 1) {  // Beliebige Zahl, mindestens 1
                    // Slider wird angepasst, bleibt aber bei seinem normalen Bereich
                    speedSlider.setValue(Math.max(50, Math.min(300, speed)));  // Nur Slider-Bereich ändern
                } else {
                    showError("Bitte eine positive Zahl für die Geschwindigkeit eingeben.");
                }
            } catch (NumberFormatException ex) {
                showError("Ungültige Eingabe. Bitte eine Zahl eingeben.");
            }
        });

        Button startButton = new Button("Spiel Starten");
        startButton.setOnAction(e -> {
            String playerName = usernameField.getText();
            username = playerName;
            System.out.println(username);
            Color snakeColor = snakeColorPicker.getValue();
            Color backgroundColor = backgroundColorPicker.getValue();
            double speed = Double.parseDouble(speedField.getText());  // Geschwindigkeit aus dem Textfeld
            snakeGame.startGame(snakeColor, backgroundColor, playerName, speed);
            stage.close();
        });

        // Layout
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(snakeColorLabel, 0, 1);
        grid.add(snakeColorPicker, 1, 1);
        grid.add(backgroundColorLabel, 0, 2);
        grid.add(backgroundColorPicker, 1, 2);
        grid.add(speedLabel, 0, 3);
        grid.add(speedSlider, 1, 3);
        grid.add(speedField, 2, 3);  // Füge das Textfeld für die Geschwindigkeit hinzu
        grid.add(startButton, 0, 4, 2, 1);

        // Szene und Darstellung
        Scene scene = new Scene(grid, 450, 250);  // Szene Größe angepasst
        stage.setScene(scene);
        stage.show();
    }

}

