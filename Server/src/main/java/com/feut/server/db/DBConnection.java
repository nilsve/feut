package com.feut.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    static String url = "jdbc:mysql://127.0.0.1:3306/feut?useUnicode=true&characterEncoding=UTF8";
    static String user = "root";
    static String password = "";

    static DBConnection instance = null;

    Connection connection;

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    DBConnection() {
        System.out.println("Database verbinding opzetten...");

        // Load the Connector/J driver
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException e) {
            System.out.println("Kon de database driver niet vinden!");
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Kon de database driver niet laden!");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Kon niet verbinden met de database");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
