package com.haulmont.testtask.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    static Connection connection;

    public static Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "SA", "");
        return connection;
    }
}
