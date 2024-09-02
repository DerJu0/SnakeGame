package org.example;

import javax.swing.*;

public class SnakeGameApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game V.1.6");
        StartMenu startMenu = new StartMenu(frame);
        frame.setContentPane(startMenu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 630);
        frame.setLocationRelativeTo(null); // Fenster zentrieren
        frame.setVisible(true);
    }
}
