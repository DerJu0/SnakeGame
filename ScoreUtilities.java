package de.eternal5.gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static de.eternal5.gui.SnakeGame.*;

public class ScoreUtilities {

    // Neue Methode zum Laden des Highscores aus der Datei
    public static void loadScore() {
        try (BufferedReader br = new BufferedReader(new FileReader("score.json"))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }
            JSONObject existingData = new JSONObject(jsonBuilder.toString());

            // Falls HighScore im JSON vorhanden ist, lade ihn
            if (existingData.has("highScore")) {
                highScore = existingData.getInt("highScore");
                SnakeGame.highScoreName = existingData.optString("playerName", "");
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Laden des Scores, Standardwert 0 wird verwendet.");
            highScore = 0;  // Standardwert, wenn Fehler auftreten
        }
    }

    // Methode zum Speichern des Highscores (nur wenn der aktuelle Score besser ist)
    public static void saveScore(String nutzername) {
        JSONObject scoreObject = new JSONObject();
        scoreObject.put("playerName", nutzername);
        scoreObject.put("score", score);

        try (BufferedReader br = new BufferedReader(new FileReader("score.json"))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }
            JSONObject existingData = new JSONObject(jsonBuilder.toString());

            // Falls ein Highscore im JSON vorhanden ist, lade diesen
            if (existingData.has("highScore")) {
                highScore = existingData.getInt("highScore");
                SnakeGame.highScoreName = existingData.optString("playerName", "");

                // Wenn der aktuelle Score besser als der gespeicherte HighScore ist, speichere ihn
                if (score > highScore) {
                    highScore = score;
                    SnakeGame.highScoreName = nutzername;  // Spielername speichern
                    scoreObject.put("highScore", highScore);  // Neuer Highscore wird gespeichert
                } else {
                    scoreObject.put("highScore", highScore);  // Der alte Highscore bleibt erhalten
                }
            } else {
                // Wenn noch kein Highscore gespeichert ist, speichere den aktuellen Score als Highscore
                highScore = score;
                scoreObject.put("highScore", highScore);
            }
        } catch (IOException e) {
            // Falls die Datei nicht existiert oder Fehler auftreten, speichere den aktuellen Score als Highscore
            highScore = score;
            scoreObject.put("highScore", highScore);
        }

        // Speichere die Highscore-Daten in der JSON-Datei
        try (FileWriter fileWriter = new FileWriter("score.json")) {
            fileWriter.write(scoreObject.toString(4));
            System.out.println("Score saved: " + scoreObject.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methode, um den aktuellen Score und Highscore anzuzeigen
    public static void updateScore(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        if (score > highScore) {
            highScore = score;
        }
        gc.fillText("Punkte: " + score + "/" + highScore + " l E5/Julian Schwendt", 10, 20);
    }

    // Gibt den aktuellen Highscore als String zurück
    public static String getCurrentScore() {
        loadScore();  // Laden des aktuellen Highscores
        return String.valueOf(highScore);  // Den besten Highscore zurückgeben
    }
}