package com.odabultumuniversity.odaonlinelearningsystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AssignmentSubmissionServlet",
        urlPatterns = {"/AssignmentSubmissionServlet"})
public class AssignmentSubmissionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("studentId") == null) {
            response.getWriter().println("Please login as a student first.");
            return;
        }

        try (PrintWriter out = response.getWriter();
             Connection con = DatabaseConnection.getConnection()) {

            out.println("<html><head><title>Submit Assignment</title></head>");
            out.println("<body style='font-family:Arial;text-align:center;'>");
            out.println("<h1>ODA BULTUM UNIVERSITY</h1>");
            out.println("<h2>SUBMIT ASSIGNMENT</h2>");

            out.println("<form method='post' action='AssignmentSubmissionServlet'>");
            out.println("Assignment ID:<br>");
            out.println("<input type='number' name='assignment_id' required><br><br>");

            out.println("Your Assignment Answer:<br>");
            out.println("<textarea name='submission_text' rows='8' cols='45' required></textarea><br><br>");

            out.println("<button type='submit'>Submit Assignment</button>");
            out.println("</form>");

            out.println("</body></html>");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("studentId") == null) {
            response.getWriter().println("Please login as a student first.");
            return;
        }

        int studentId = Integer.parseInt(
                session.getAttribute("studentId").toString());

        int assignmentId = Integer.parseInt(
                request.getParameter("assignment_id"));

        String submissionText = request.getParameter("submission_text");

        String sql = "INSERT INTO assignment_submissions "
                + "(assignment_id, student_id, submission_text) "
                + "VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             PrintWriter out = response.getWriter()) {

            ps.setInt(1, assignmentId);
            ps.setInt(2, studentId);
            ps.setString(3, submissionText);

            int result = ps.executeUpdate();

            out.println("<html><body style='font-family:Arial;text-align:center;'>");

            if (result > 0) {
                out.println("<h2>Assignment Submitted Successfully!</h2>");
            } else {
                out.println("<h2>Assignment Submission Failed!</h2>");
            }

            out.println("<a href='AssignmentSubmissionServlet'>Submit Another Assignment</a>");
            out.println("</body></html>");

        } catch (SQLException e) {
            throw new ServletException("Assignment Submission Error: "
                    + e.getMessage(), e);
        }
    }
}