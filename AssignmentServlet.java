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

@WebServlet(name = "AssignmentServlet",
        urlPatterns = {"/AssignmentServlet"})
public class AssignmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            out.println("<html><body>");
            out.println("<h2>ODA BULTUM UNIVERSITY</h2>");
            out.println("<h3>Create Assignment</h3>");

            out.println("<form method='post' action='AssignmentServlet'>");

            out.println("Course ID:<br>");
            out.println("<input type='number' name='courseId' required><br><br>");

            out.println("Teacher ID:<br>");
            out.println("<input type='number' name='teacherId' required><br><br>");

            out.println("Assignment Title:<br>");
            out.println("<input type='text' name='title' required><br><br>");

            out.println("Description:<br>");
            out.println("<textarea name='description' required></textarea><br><br>");

            out.println("Due Date:<br>");
            out.println("<input type='date' name='dueDate' required><br><br>");

            out.println("<button type='submit'>Create Assignment</button>");

            out.println("</form>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String sql = "INSERT INTO assignments "
                + "(course_id, teacher_id, title, description, due_date) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PrintWriter out = response.getWriter()) {

            int courseId = Integer.parseInt(request.getParameter("courseId"));
            int teacherId = Integer.parseInt(request.getParameter("teacherId"));

            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String dueDate = request.getParameter("dueDate");

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, courseId);
                ps.setInt(2, teacherId);
                ps.setString(3, title);
                ps.setString(4, description);
                ps.setString(5, dueDate);

                int result = ps.executeUpdate();

                if (result > 0) {
                    out.println("<h2>Assignment Created Successfully!</h2>");
                    out.println("<a href='AssignmentServlet'>Create Another Assignment</a>");
                }

            } catch (Exception e) {
                out.println("<h3>Assignment Error: "
                        + e.getMessage() + "</h3>");
            }
        }
    }
}