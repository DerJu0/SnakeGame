package de.eternal5.gui;

import javafx.scene.control.Alert;

public class Utilities {
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
