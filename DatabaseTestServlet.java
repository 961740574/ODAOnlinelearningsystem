package com.odabultumuniversity.odaonlinelearningsystem;

import java.io.IOException;
import java.sql.Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/db-test")
public class DatabaseTestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (Connection connection =
                DatabaseConnection.getConnection()) {

            if (connection != null) {
                response.getWriter().println(
                    "Database connected successfully!");
            } else {
                response.getWriter().println(
                    "Database connection failed!");
            }

        }catch (Exception e) {
    response.getWriter().println(
        "ERROR DETAILS: " + e.getClass().getSimpleName()
        + " - " + e.getMessage());
}
    }
}