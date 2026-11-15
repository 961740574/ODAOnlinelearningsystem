package com.odabultumuniversity.odaonlinelearningsystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TeacherGradeAssignmentServlet",
        urlPatterns = {"/TeacherGradeAssignmentServlet"})
public class TeacherGradeAssignmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            out.println("<html><head><title>Grade Assignment</title></head>");
            out.println("<body style='font-family:Arial;text-align:center;'>");

            out.println("<h1>ODA BULTUM UNIVERSITY</h1>");
            out.println("<h2>TEACHER ASSIGNMENT GRADING</h2>");

            out.println("<form method='post' action='TeacherGradeAssignmentServlet'>");

            out.println("Submission ID:<br>");
            out.println("<input type='number' name='submission_id' required><br><br>");

            out.println("Teacher ID:<br>");
            out.println("<input type='number' name='teacher_id' required><br><br>");

            out.println("Marks:<br>");
            out.println("<input type='number' name='marks' min='0' required><br><br>");

            out.println("Feedback:<br>");
            out.println("<textarea name='feedback' rows='5' cols='35'></textarea><br><br>");

            out.println("<button type='submit'>Submit Grade</button>");

            out.println("</form>");
            out.println("</body></html>");

        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            int submissionId = Integer.parseInt(
                    request.getParameter("submission_id"));

            int teacherId = Integer.parseInt(
                    request.getParameter("teacher_id"));

            int marks = Integer.parseInt(
                    request.getParameter("marks"));

            String feedback = request.getParameter("feedback");

            if (submissionId <= 0 ||  teacherId <= 0 ||  marks <= 0)
            {
                out.println("<h2>Please enter valid information.</h2>");
                return;
            }

            String sql = "INSERT INTO Assignment_Grades "
                    + "(submission_id, teacher_id, marks, feedback) "
                    + "VALUES (?, ?, ?, ?)";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, submissionId);
                ps.setInt(2, teacherId);
                ps.setInt(3, marks);
                ps.setString(4, feedback);

                int result = ps.executeUpdate();

                if (result > 0) {
                    out.println("<h2>Assignment Graded Successfully!</h2>");
                    out.println("<p>Marks: " + marks + "</p>");
                    out.println("<p>Feedback: "
                            + (feedback == null ? "" : feedback)
                            + "</p>");
                } else {
                    out.println("<h2>Grading Failed!</h2>");
                }

            } catch (Exception e) {
                out.println("<h2>Grading Error: "
                        + e.getMessage() + "</h2>");
            }

        } catch (NumberFormatException e) {
            response.getWriter().println("Please enter valid numbers.");
        }
    }
}