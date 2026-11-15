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

@WebServlet(name = "StudentAssignmentServlet",
        urlPatterns = {"/StudentAssignmentServlet"})
public class StudentAssignmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String sql = "SELECT assignment_id, course_id, title, "
                + "description, due_date FROM assignments "
                + "ORDER BY due_date";

        try (PrintWriter out = response.getWriter();
             Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            out.println("<html><head><title>My Assignments</title></head><body>");
            out.println("<h2>ODA BULTUM UNIVERSITY</h2>");
            out.println("<h3>Available Assignments</h3>");

            boolean found = false;

            while (rs.next()) {
                found = true;

                out.println("<hr>");
                out.println("<h3>" + rs.getString("title") + "</h3>");
                out.println("<p>Course ID: "
                        + rs.getInt("course_id") + "</p>");
                out.println("<p>Description: "
                        + rs.getString("description") + "</p>");
                out.println("<p>Due Date: "
                        + rs.getString("due_date") + "</p>");
            }

            if (!found) {
                out.println("<p>No Assignments Available.</p>");
            }

            out.println("</body></html>");

        } catch (Exception e) {
            response.getWriter().println(
                    "Assignment Error: " + e.getMessage());
        }
    }
}