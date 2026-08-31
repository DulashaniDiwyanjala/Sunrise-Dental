package com.sunrise.dental.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/sunriseDentalDB";

    private static final String USERNAME = "rootAdmin";

    private static final String PASSWORD = "123@diwya";

    public static Connection getConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
                    URL,
                    USERNAME,
                    PASSWORD
            );

            System.out.println("Database Connected Successfully!");

            return connection;

        } catch (ClassNotFoundException | SQLException e) {

            System.out.println("Database Connection Failed!");
            e.printStackTrace();

            return null;
        }
    }
    // === Test කරගන්න මේ Main Method එක විතරක් එකතු කරන්න ===
    public static void main(String[] args) {
        getConnection();
    }
}