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

@WebServlet(name = "StudentLiveClassesServlet",
        urlPatterns = {"/StudentLiveClassesServlet"})
public class StudentLiveClassesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String sql = "SELECT title, meeting_link, class_datetime "
                + "FROM live_classes ORDER BY class_datetime";

        try (PrintWriter out = response.getWriter();
             Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            out.println("<html><body>");
            out.println("<h2>ODA BULTUM UNIVERSITY</h2>");
            out.println("<h3>Available Live Classes</h3>");

            boolean found = false;

            while (rs.next()) {
                found = true;
                out.println("<hr>");
                out.println("<h3>" + rs.getString("title") + "</h3>");
                out.println("<p>Date: "
                        + rs.getString("class_datetime") + "</p>");
                out.println("<a href='"
                        + rs.getString("meeting_link")
                        + "' target='_blank'>Join Live Class</a>");
            }

            if (!found) {
                out.println("<p>No Live Classes Available.</p>");
            }

            out.println("</body></html>");

        } catch (Exception e) {
            response.getWriter().println(
                    "Error: " + e.getMessage());
        }
    }
}