package com.odabultumuniversity.odaonlinelearningsystem;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "StudentProfileServlet",
        urlPatterns = {"/StudentProfileServlet"})

public class StudentProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("studentId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        String fullName = (String) session.getAttribute("fullName");
        String email = (String) session.getAttribute("email");
        String department = (String) session.getAttribute("department");

        response.getWriter().println(
            "<html><head><title>My Profile</title></head><body>");

        response.getWriter().println("<h1>ODA BULTUM UNIVERSITY</h1>");
        response.getWriter().println("<h2>MY PROFILE</h2>");
        response.getWriter().println("<h3>STUDENT INFORMATION</h3>");

        response.getWriter().println("<p>Full Name: " + fullName + "</p>");
        response.getWriter().println("<p>Email: " + email + "</p>");
        response.getWriter().println("<p>Department: " + department + "</p>");

        response.getWriter().println(
            "<a href='student-dashboard.html'>Back to Dashboard</a>");

        response.getWriter().println("</body></html>");
    }
}