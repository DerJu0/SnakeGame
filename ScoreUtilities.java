package de.eternal5.gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ScoreUtilities {
    public static int highScore = 0;
    public static String playerName;
    public static String highScoreName = "";
    public static int score = 0;

    public static void saveScore(String nutzername) {
        JSONObject scoreObject = new JSONObject();
        scoreObject.put("playerName", nutzername);
        scoreObject.put("score", score);

        // Versuche, den aktuellen Highscore zu laden
        try (BufferedReader br = new BufferedReader(new FileReader("score.json"))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }
            JSONObject existingData = new JSONObject(jsonBuilder.toString());

            // Überprüfen, ob der Highscore existiert
            if (existingData.has("highScore") && !existingData.get("highScore").equals("")) {
                highScore = existingData.getInt("highScore");
                highScoreName = existingData.optString("playerName", "");

                // Überprüfen, ob der aktuelle Score den Highscore übertrifft
                if (score > highScore) {
                    highScore = score;
                    highScoreName = playerName;
                    scoreObject.put("highScore", highScore);
                } else {
                    scoreObject.put("highScore", highScore);
                }
            } else {
                // Wenn die Datei leer ist, setze den Highscore und den Spielernamen
                highScore = score;
                scoreObject.put("highScore", highScore);
            }
        } catch (IOException e) {
            // Fehler beim Lesen der Datei, ignoriere und erstelle die Datei neu
            highScore = score;  // Setze den Highscore auf den aktuellen Score
            scoreObject.put("highScore", highScore);
        }

        // Speichern in der score.json-Datei
        try (FileWriter fileWriter = new FileWriter("score.json")) {
            fileWriter.write(scoreObject.toString(4)); // Indentierung für bessere Lesbarkeit
            System.out.println("Score saved: " + scoreObject.toString(4)); // Debugging-Info
        } catch (IOException e) {
            e.printStackTrace();  // Fehler beim Schreiben der Datei
        }
    }
    public static void updateScore(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        if(score > highScore){
            highScore = score;
        }
        gc.fillText("Punkte: " + score + "/"+ highScore +" l eternal5/Julian Schwendt", 10, 20); // Punkte oben links anzeigen
    }

}
