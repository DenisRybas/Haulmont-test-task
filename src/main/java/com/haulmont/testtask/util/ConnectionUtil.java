package com.haulmont.testtask.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Утилита для получения соединения
 */
public class ConnectionUtil {
    /**
     * @return соединение
     * @throws SQLException - исключение, которое может появиться
     *                      в ходе использования метода, его нужно обработать
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:testdb",
                "SA", "");
    }
}
