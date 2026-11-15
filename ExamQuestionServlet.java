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

@WebServlet(name = "ExamQuestionServlet", urlPatterns = {"/ExamQuestionServlet"})
public class ExamQuestionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Add Exam Question</title></head><body>");
        out.println("<h2>ODA BULTUM UNIVERSITY - ADD EXAM QUESTION</h2>");
        out.println("<form method='post' action='ExamQuestionServlet'>");

        out.println("Exam ID: <input type='number' name='exam_id' required><br><br>");
        out.println("Question: <textarea name='question_text' required></textarea><br><br>");
        out.println("Option A: <input type='text' name='option_a' required><br><br>");
        out.println("Option B: <input type='text' name='option_b' required><br><br>");
        out.println("Option C: <input type='text' name='option_c' required><br><br>");
        out.println("Option D: <input type='text' name='option_d' required><br><br>");

        out.println("Correct Answer: <select name='correct_answer'>");
        out.println("<option value='A'>A</option>");
        out.println("<option value='B'>B</option>");
        out.println("<option value='C'>C</option>");
        out.println("<option value='D'>D</option>");
        out.println("</select><br><br>");

        out.println("Marks: <input type='number' name='marks' required><br><br>");
        out.println("<button type='submit'>Add Question</button>");
        out.println("</form></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int examId = Integer.parseInt(request.getParameter("exam_id"));
            String question = request.getParameter("question_text");
            String a = request.getParameter("option_a");
            String b = request.getParameter("option_b");
            String c = request.getParameter("option_c");
            String d = request.getParameter("option_d");
            String answer = request.getParameter("correct_answer");
            int marks = Integer.parseInt(request.getParameter("marks"));

            String sql = "INSERT INTO exam_questions "
                    + "(exam_id, question_text, option_a, option_b, option_c, option_d, correct_answer, marks) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, examId);
                ps.setString(2, question);
                ps.setString(3, a);
                ps.setString(4, b);
                ps.setString(5, c);
                ps.setString(6, d);
                ps.setString(7, answer);
                ps.setInt(8, marks);

                ps.executeUpdate();
                out.println("<h2>Exam Question Added Successfully!</h2>");
            }

        } catch (Exception e) {
            out.println("<h2>Exam Question Error: " + e.getMessage() + "</h2>");
        }
    }
}