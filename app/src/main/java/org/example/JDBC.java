package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {
    static final String dbURL = "jdbc:mysql://localhost:3306/DailyTerms";
    static final String USER = "root";
    static final String PASS = "N3w4life!";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbURL, USER, PASS);
    }
}
