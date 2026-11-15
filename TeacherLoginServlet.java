package com.odabultumuniversity.odaonlinelearningsystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "TeacherLoginServlet",
        urlPatterns = {"/teacher-login"})

public class TeacherLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("teacher-login.html");
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String sql = "SELECT teacher_id, full_name, email, password "
        + "FROM teachers WHERE email = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
           

            ResultSet rs = ps.executeQuery();

         if (rs.next() && (rs.getString("password").startsWith("$2a$")
        ? BCrypt.checkpw(password, rs.getString("password"))
        : rs.getString("password").equals(password))) {

                HttpSession session = request.getSession();

                session.setAttribute("teacherId",
                        rs.getInt("teacher_id"));

                session.setAttribute("teacherFullName",
                        rs.getString("full_name"));

                session.setAttribute("teacherEmail",
                        rs.getString("email"));

                response.sendRedirect("teacher-dashboard.html");

            } else {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println(
                        "<h2>Invalid Email or Password!</h2>");
                response.getWriter().println(
                        "<a href='teacher-login.html'>Try Again</a>");
            }

        } catch (Exception e) {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println(
                    "Login Error: " + e.getMessage());
        }
    }
}