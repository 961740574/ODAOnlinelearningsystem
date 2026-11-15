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

@WebServlet(name = "LiveClassServlet", urlPatterns = {"/LiveClassServlet"})
public class LiveClassServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>Create Live Class</title></head><body>");
            out.println("<h2>ODA BULTUM UNIVERSITY</h2>");
            out.println("<h3>Create Live Online Class</h3>");

            out.println("<form method='post' action='LiveClassServlet'>");

            out.println("Course ID:<br>");
            out.println("<input type='number' name='courseId' required><br><br>");

            out.println("Teacher ID:<br>");
            out.println("<input type='number' name='teacherId' required><br><br>");

            out.println("Class Title:<br>");
            out.println("<input type='text' name='title' required><br><br>");

            out.println("Meeting Link:<br>");
            out.println("<input type='url' name='meetingLink' required><br><br>");

            out.println("Class Date and Time:<br>");
            out.println("<input type='datetime-local' name='classDatetime' required><br><br>");

            out.println("<button type='submit'>Create Live Class</button>");
            out.println("</form></body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            int courseId = Integer.parseInt(request.getParameter("courseId"));
            int teacherId = Integer.parseInt(request.getParameter("teacherId"));

            String title = request.getParameter("title");
            String meetingLink = request.getParameter("meetingLink");
            String classDatetime = request.getParameter("classDatetime")
                    .replace("T", " ") + ":00";

            String sql = "INSERT INTO live_classes "
                    + "(course_id, teacher_id, title, meeting_link, class_datetime) "
                    + "VALUES (?, ?, ?, ?, ?)";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, courseId);
                ps.setInt(2, teacherId);
                ps.setString(3, title);
                ps.setString(4, meetingLink);
                ps.setString(5, classDatetime);

                int result = ps.executeUpdate();

                if (result > 0) {
                    out.println("<h2>Live Class Created Successfully!</h2>");
                    out.println("<a href='LiveClassServlet'>Create Another Class</a>");
                }

            } catch (Exception e) {
                out.println("<h3>Live Class Error: " + e.getMessage() + "</h3>");
            }
        }
    }
}