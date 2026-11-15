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

@WebServlet(name = "EnrollmentServlet",
        urlPatterns = {"/EnrollmentServlet"})

public class EnrollmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("studentId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int studentId = (Integer) session.getAttribute("studentId");

        try {
            int courseId = Integer.parseInt(
                    request.getParameter("courseId"));

            String sql = "INSERT INTO Enrollments "
                    + "(student_id, course_id, enrollment_date) "
                    + "VALUES (?, ?, CURDATE())";

            try (Connection con =
                    DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, studentId);
                ps.setInt(2, courseId);

                int result = ps.executeUpdate();

                response.setContentType("text/html;charset=UTF-8");

                if (result > 0) {
                    response.getWriter().println(
                        "<h2>Course Enrollment Successful!</h2>");
                    response.getWriter().println(
                        "<a href='courses.html'>Back to Courses</a>");
                }
            }

        } catch (NumberFormatException e) {
            response.sendError(400, "Invalid Course ID");
        } catch (Exception e) {
            throw new ServletException(
                    "Enrollment Error: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("courses.html");
    }
}