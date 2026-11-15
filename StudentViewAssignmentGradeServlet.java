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

@WebServlet(name = "StudentViewAssignmentGradeServlet",
        urlPatterns = {"/StudentViewAssignmentGradeServlet"})
public class StudentViewAssignmentGradeServlet extends HttpServlet {

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

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("studentId") == null) {
            response.getWriter().println("Please login as a student first.");
            return;
        }

        int studentId = Integer.parseInt(
                session.getAttribute("studentId").toString());

        String sql = "SELECT a.title, g.marks, g.feedback, g.graded_at "
                + "FROM assignment_grades g "
                + "JOIN assignment_submissions s "
                + "ON g.submission_id = s.submission_id "
                + "JOIN assignments a "
                + "ON s.assignment_id = a.assignment_id "
                + "WHERE s.student_id = ? "
                + "ORDER BY g.graded_at DESC";

        try (PrintWriter out = response.getWriter();
             Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {

                out.println("<html><head><title>My Assignment Grades</title></head>");
                out.println("<body style='font-family:Arial;text-align:center;'>");

                out.println("<h1>ODA BULTUM UNIVERSITY</h1>");
                out.println("<h2>MY ASSIGNMENT GRADES</h2>");

                out.println("<table border='1' cellpadding='10' "
                        + "style='margin:auto;border-collapse:collapse;'>");

                out.println("<tr><th>Assignment Title</th>"
                        + "<th>Marks</th>"
                        + "<th>Feedback</th>"
                        + "<th>Graded Date</th></tr>");

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    out.println("<tr>");
                    out.println("<td>" + escape(rs.getString("title")) + "</td>");
                    out.println("<td>" + rs.getInt("marks") + "</td>");
                    out.println("<td>" + escape(rs.getString("feedback")) + "</td>");
                    out.println("<td>" + rs.getTimestamp("graded_at") + "</td>");
                    out.println("</tr>");
                }

                if (!found) {
                    out.println("<tr><td colspan='4'>No grades found.</td></tr>");
                }

                out.println("</table>");
                out.println("</body></html>");
            }

        } catch (SQLException e) {
            throw new ServletException("Error loading assignment grades: "
                    + e.getMessage(), e);
        }
    }
}