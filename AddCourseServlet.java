package com.odabultumuniversity.odaonlinelearningsystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AddCourseServlet",
        urlPatterns = {"/add-course"})

public class AddCourseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("teacherId") == null) {
            response.sendRedirect("teacher-login.html");
            return;
        }

        int teacherId = (Integer) session.getAttribute("teacherId");

        String courseName = request.getParameter("course_name");
        String courseCode = request.getParameter("course_code");
        String description = request.getParameter("description");

        String sql = "INSERT INTO courses "
                + "(course_name, course_code, description, teacher_id) "
                + "VALUES (?, ?, ?, ?)";

        response.setContentType("text/html;charset=UTF-8");

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, courseName);
            ps.setString(2, courseCode);
            ps.setString(3, description);
            ps.setInt(4, teacherId);

            int result = ps.executeUpdate();

            if (result > 0) {
                response.getWriter().println(
                        "<h2>Course Added Successfully!</h2>");
                response.getWriter().println(
                        "<a href='teacher-courses.html'>Add Another Course</a>");
            }

        } catch (Exception e) {
            response.getWriter().println(
                    "Error: " + e.getMessage());
        }
    }
}