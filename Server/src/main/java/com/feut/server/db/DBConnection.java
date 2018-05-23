package com.feut.server.db;

import com.feut.shared.connection.LogHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    static String url = "jdbc:mysql://127.0.0.1:3306/feut";
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
        LogHelper.Log("Database verbinding opzetten...");

        // Load the Connector/J driver
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException e) {
            LogHelper.Log("Kon de database driver niet vinden!");
            System.exit(1);
        } catch (Exception e) {
            LogHelper.Log("Kon de database driver niet laden!");
            LogHelper.Log(e.getMessage());
            System.exit(1);
        }

        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            LogHelper.Log("Kon niet verbinden met de database");
            LogHelper.Log(e.getMessage());
            System.exit(1);
        }
    }
}
