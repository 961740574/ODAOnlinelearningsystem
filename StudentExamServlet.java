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

@WebServlet(name = "StudentExamServlet", urlPatterns = {"/StudentExamServlet"})
public class StudentExamServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String examIdValue = request.getParameter("exam_id");
            int examId = (examIdValue == null) ? 1 : Integer.parseInt(examIdValue);

            out.println("<html><head><title>Student Online Exam</title></head><body>");
            out.println("<h2>ODA BULTUM UNIVERSITY - ONLINE EXAM</h2>");
            out.println("<form method='post' action='StudentExamServlet'>");
            out.println("<input type='hidden' name='exam_id' value='" + examId + "'>");

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                     "SELECT question_id, question_text, option_a, option_b, "
                     + "option_c, option_d FROM exam_questions WHERE exam_id=?")) {

                ps.setInt(1, examId);

                try (ResultSet rs = ps.executeQuery()) {
                    int count = 0;

                    while (rs.next()) {
                        count++;
                        int id = rs.getInt("question_id");

                        out.println("<hr><h3>" + count + ". "
                                + rs.getString("question_text") + "</h3>");

                        out.println("<input type='radio' name='q_" + id
                                + "' value='A' required> A. " + rs.getString("option_a") + "<br>");

                        out.println("<input type='radio' name='q_" + id
                                + "' value='B'> B. " + rs.getString("option_b") + "<br>");

                        out.println("<input type='radio' name='q_" + id
                                + "' value='C'> C. " + rs.getString("option_c") + "<br>");

                        out.println("<input type='radio' name='q_" + id
                                + "' value='D'> D. " + rs.getString("option_d") + "<br>");
                    }

                    if (count == 0) {
                        out.println("<h3>No questions available for this exam.</h3>");
                    } else {
                        out.println("<br><button type='submit'>Submit Exam</button>");
                    }
                }
            }

            out.println("</form></body></html>");

        } catch (Exception e) {
            out.println("<h3>Exam Error: " + e.getMessage() + "</h3>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("studentId") == null) {
            out.println("<h3>Please login as a student first.</h3>");
            return;
        }

        try {
            int studentId = (Integer) session.getAttribute("studentId");
            int examId = Integer.parseInt(request.getParameter("exam_id"));

            int score = 0;
            int totalMarks = 0;
            int answered = 0;
            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                     "SELECT question_id, correct_answer, marks "
                     + "FROM exam_questions WHERE exam_id=?")) {

                ps.setInt(1, examId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int questionId = rs.getInt("question_id");
                        int marks = rs.getInt("marks");
                        String correct = rs.getString("correct_answer");
                        String answer = request.getParameter("q_" + questionId);

                        totalMarks += marks;

                        if (answer != null) {
                            answered++;
                            if (answer.equalsIgnoreCase(correct)) {
                                score += marks;
                            }
                        }
                    }
                }

                if (answered == 0) {
                    out.println("<h3>Please answer the questions before submitting.</h3>");
                    return;
                }

                try (PreparedStatement save = con.prepareStatement(
                    "INSERT INTO exam_results "
                    + "(exam_id, student_id, score, total_marks) VALUES (?, ?, ?, ?)")) {

                    save.setInt(1, examId);
                    save.setInt(2, studentId);
                    save.setInt(3, score);
                    save.setInt(4, totalMarks);
                    save.executeUpdate();
                }
            }

            out.println("<html><body>");
            out.println("<h2>Exam Submitted Successfully!</h2>");
            out.println("<h3>Your Score: " + score + " / " + totalMarks + "</h3>");
            out.println("</body></html>");

        } catch (Exception e) {
            out.println("<h3>Exam Error: " + e.getMessage() + "</h3>");
        }
    }
}