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

@WebServlet(name = "TeacherViewSubmissionServlet",
        urlPatterns = {"/TeacherViewSubmissionServlet"})
public class TeacherViewSubmissionServlet extends HttpServlet {

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String sql = "SELECT s.submission_id, s.assignment_id, "
                + "st.full_name, a.title, s.submission_text, s.submitted_at "
                + "FROM assignment_submissions s "
                + "JOIN students st ON s.student_id = st.student_id "
                + "JOIN assignments a ON s.assignment_id = a.assignment_id "
                + "ORDER BY s.submitted_at DESC";

        try (PrintWriter out = response.getWriter();
             Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            out.println("<html><head><title>Student Submissions</title></head>");
            out.println("<body style='font-family:Arial;text-align:center;'>");

            out.println("<h1>ODA BULTUM UNIVERSITY</h1>");
            out.println("<h2>STUDENT ASSIGNMENT SUBMISSIONS</h2>");

            out.println("<table border='1' cellpadding='10' "
                    + "style='margin:auto;border-collapse:collapse;'>");

            out.println("<tr><th>Submission ID</th>"
                    + "<th>Student Name</th>"
                    + "<th>Assignment</th>"
                    + "<th>Student Answer</th>"
                    + "<th>Submitted Date</th></tr>");

            boolean found = false;

            while (rs.next()) {
                found = true;

                out.println("<tr>");
                out.println("<td>" + rs.getInt("submission_id") + "</td>");
                out.println("<td>" + escape(rs.getString("full_name")) + "</td>");
                out.println("<td>" + escape(rs.getString("title")) + "</td>");
                out.println("<td>" + escape(rs.getString("submission_text")) + "</td>");
                out.println("<td>" + rs.getTimestamp("submitted_at") + "</td>");
                out.println("</tr>");
            }

            if (!found) {
                out.println("<tr><td colspan='5'>No submissions found.</td></tr>");
            }

            out.println("</table>");
            out.println("</body></html>");

        } catch (SQLException e) {
            throw new ServletException("Error loading submissions: "
                    + e.getMessage(), e);
        }
    }
}