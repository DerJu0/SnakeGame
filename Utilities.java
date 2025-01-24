package de.eternal5.gui;

import javafx.scene.control.Alert;
import javafx.scene.media.AudioClip;

import java.util.Random;

public class Utilities {
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Random Text
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        int length = 10; // Länge der gewünschten Zufalls-Kombination
        String randomString = generateRandomString(length);
        System.out.println("Zufällige Kombination: " + randomString);
    }

    
    public class SoundEffects {
        private static final AudioClip FRUIT_SOUND = new AudioClip(
                SoundEffects.class.getResource("./sound.mp3").toExternalForm()
        );

        // Methode zum Abspielen des Frucht-Sounds
        public static void playFruitSound() {
            FRUIT_SOUND.play();
        }
    }
}
