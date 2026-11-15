package com.odabultumuniversity.odaonlinelearningsystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TeacherRegistrationServlet",
        urlPatterns = {"/teacher-register"})

public class TeacherRegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("teacher-register.html");
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("full_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String sql = "INSERT INTO teachers "
                + "(full_name, email, password) VALUES (?, ?, ?)";

        response.setContentType("text/html;charset=UTF-8");

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, fullName);
            ps.setString(2, email);
           ps.setString(3, BCrypt.hashpw(password, BCrypt.gensalt()));

            int result = ps.executeUpdate();

            if (result > 0) {
                response.getWriter().println(
                    "<h2>Teacher Registration Successful!</h2>");
            }

        } catch (Exception e) {
            response.getWriter().println(
                "Registration Error: " + e.getMessage());
        }
    }
}