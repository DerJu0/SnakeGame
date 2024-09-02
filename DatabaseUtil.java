package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUtil {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/SnakeGame";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    static {
        try {
            /* JDBC Driver ist erforderlich, um eine Datenbank-Verbindung herzustellen */
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC Driver not found.");
        }
    }

    /* Verbindung zur Datenbank herstellen */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /* Scores der Spieler hochladen */
    public static void saveScore(String username, int score) {
        String query = "INSERT INTO DB (Nutzername, Score) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setInt(2, score);
            stmt.executeUpdate();
            System.out.println("Score von "+username+" gespeichert!");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
