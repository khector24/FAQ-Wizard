package com.rainbowacehardware.faqwizard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static final String DB_NAME = "your-database-name.db"; // Replace "your-database-name" with your database file name
    public static final String DB_URL = "jdbc:sqlite:" + DB_NAME;
    private Connection databaseLink;

    public Connection getConnection() {
        try {
            if (databaseLink == null || databaseLink.isClosed()) {
                // Load the SQLite JDBC driver (Ensure you have added the driver to your build tool)
                Class.forName("org.sqlite.JDBC");
                // Create a connection to the database
                databaseLink = DriverManager.getConnection(DB_URL);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return databaseLink;
    }
}

