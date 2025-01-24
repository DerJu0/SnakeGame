package de.eternal5.gui;
import javafx.scene.paint.Color;
import de.eternal5.gui.allgmein.*;
import static de.eternal5.gui.StartMenu.speed;
import static de.eternal5.gui.allgmein.log;

public class powerups {
    //Desto weniger, desto schneller die schlange
    public static void PlusSpeedUpgrade(SnakeGame game, double newSpeed) {
        // Aktualisiere die Geschwindigkeit im SnakeGame
        game.updateGameSpeed(game, newSpeed);
        log("Event ausgelöst mit einer Geschwindigkeit von "+newSpeed);
    }
    //Desto do höher der wert, desto langsamer die Schlange

    public static void MinusSpeedDownUpgrade(SnakeGame game, double newSpeed){
        // Aktualisiere die Geschwindigkeit im SnakeGame
        game.updateGameSpeed(game, newSpeed);
        log("Event ausgelöst mit einer Geschwindigkeit von "+newSpeed);
    }
    public static void RainBow (Color c){
        //Unsterblichkeit bei Kollision
    }
}
