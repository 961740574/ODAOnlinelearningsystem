package com.odabultumuniversity.odaonlinelearningsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.cj.jdbc.Driver;

public class DatabaseConnection {

   private static final String URL =
        "jdbc:mysql://localhost:3306/oda_bultum_university";

private static final String USER = "root";

    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {

        if (PASSWORD == null || PASSWORD.isEmpty()) {
            throw new SQLException("DB_PASSWORD is not configured.");
        }
DriverManager.registerDriver(new Driver());
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}