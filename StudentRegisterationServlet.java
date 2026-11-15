package com.odabultumuniversity.odaonlinelearningsystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/student-register")
public class StudentRegisterationServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("full_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String department = request.getParameter("department");
        String sql = "INSERT INTO students (full_name, email, password, department) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, email);
           ps.setString(3, BCrypt.hashpw(password, BCrypt.gensalt()));
            ps.setString(4,department);
            ps.executeUpdate();

            response.getWriter().println("Registration successful!");

        } catch (Exception e) {
            response.getWriter().println("Registration failed: "
                    + e.getMessage());
        }
    }
}