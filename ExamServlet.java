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

@WebServlet(name = "ExamServlet", urlPatterns = {"/ExamServlet"})
public class ExamServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Create Exam</title></head><body>");
        out.println("<h2>ODA BULTUM UNIVERSITY - CREATE EXAM</h2>");
        out.println("<form method='post' action='ExamServlet'>");
        out.println("Course ID: <input type='number' name='course_id' required><br><br>");
        out.println("Teacher ID: <input type='number' name='teacher_id' required><br><br>");
        out.println("Exam Title: <input type='text' name='exam_title' required><br><br>");
        out.println("Total Marks: <input type='number' name='total_marks' required><br><br>");
        out.println("Exam Date: <input type='datetime-local' name='exam_date'><br><br>");
        out.println("<button type='submit'>Create Exam</button>");
        out.println("</form></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int courseId = Integer.parseInt(request.getParameter("course_id"));
            int teacherId = Integer.parseInt(request.getParameter("teacher_id"));
            String title = request.getParameter("exam_title");
            int marks = Integer.parseInt(request.getParameter("total_marks"));
            String examDate = request.getParameter("exam_date");

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO exams (course_id, teacher_id, exam_title, total_marks, exam_date) VALUES (?, ?, ?, ?, ?)")) {

                ps.setInt(1, courseId);
                ps.setInt(2, teacherId);
                ps.setString(3, title);
                ps.setInt(4, marks);

                if (examDate == null || examDate.isEmpty()) {
                    ps.setNull(5, java.sql.Types.TIMESTAMP);
                } else {
                    ps.setTimestamp(5, java.sql.Timestamp.valueOf(examDate.replace("T", " ") + ":00"));
                }

                ps.executeUpdate();
                out.println("<h2>Exam Created Successfully!</h2>");
            }

        } catch (Exception e) {
            out.println("<h2>Exam Error: " + e.getMessage() + "</h2>");
        }
    }
}