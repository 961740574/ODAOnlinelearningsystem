package com.odabultumuniversity.odaonlinelearningsystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "StudentExamResultsServlet",
        urlPatterns = {"/StudentExamResultsServlet"})
public class StudentExamResultsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("studentId") == null) {
            out.println("<h2>Please login as a student first.</h2>");
            return;
        }

        try {
            int studentId = Integer.parseInt(
                    session.getAttribute("studentId").toString());

            out.println("<html><head><title>Exam Results</title></head><body>");
            out.println("<h2>ODA BULTUM UNIVERSITY</h2>");
            out.println("<h3>MY EXAM RESULTS</h3>");

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                    "SELECT r.result_id, e.exam_title, r.score, "
                    + "r.total_marks, r.submitted_at "
                    + "FROM exam_results r "
                    + "JOIN exams e ON r.exam_id = e.exam_id "
                    + "WHERE r.student_id = ? "
                    + "ORDER BY r.submitted_at DESC")) {

                ps.setInt(1, studentId);

                try (ResultSet rs = ps.executeQuery()) {

                    out.println("<table border='1' cellpadding='10'>");
                    out.println("<tr><th>Exam</th><th>Score</th>"
                            + "<th>Total Marks</th><th>Date</th></tr>");

                    boolean found = false;

                    while (rs.next()) {
                        found = true;
                        out.println("<tr>");
                        out.println("<td>" + rs.getString("exam_title") + "</td>");
                        out.println("<td>" + rs.getInt("score") + "</td>");
                        out.println("<td>" + rs.getInt("total_marks") + "</td>");
                        out.println("<td>" + rs.getTimestamp("submitted_at") + "</td>");
                        out.println("</tr>");
                    }

                    out.println("</table>");

                    if (!found) {
                        out.println("<p>No exam results available.</p>");
                    }
                }
            }

            out.println("</body></html>");

        } catch (Exception e) {
            out.println("<h3>Results Error: " + e.getMessage() + "</h3>");
        }
    }
}