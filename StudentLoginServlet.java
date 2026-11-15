package com.odabultumuniversity.odaonlinelearningsystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.ResultSet;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "StudentLoginServlet", urlPatterns = {"/student-login"})
public class StudentLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("login.html");
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String sql = "SELECT student_id, full_name, email, department, password FROM students WHERE email=?";

        response.setContentType("text/html;charset=UTF-8");

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            

            try (ResultSet rs = ps.executeQuery()) {

              if (rs.next() && (rs.getString("password").startsWith("$2a$")
        ? BCrypt.checkpw(password, rs.getString("password"))
        : rs.getString("password").equals(password))) {
    HttpSession session = request.getSession();
    session.setAttribute("studentId", rs.getInt("student_id"));
session.setAttribute("fullName", rs.getString("full_name"));
session.setAttribute("email", rs.getString("email"));
session.setAttribute("department", rs.getString("department"));
   response.sendRedirect(request.getContextPath() + "/student-dashboard.html");
} else {
                    response.getWriter().println(
                        "<h2>Invalid Email or Password!</h2>");
                }
            }

        } catch (Exception e) {
            response.getWriter().println(
                "Login Error: " + e.getMessage());
        }
    }
}