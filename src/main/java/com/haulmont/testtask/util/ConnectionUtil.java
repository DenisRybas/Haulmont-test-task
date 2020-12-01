package com.haulmont.testtask.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:testdb",
                "SA", "");
    }
}
