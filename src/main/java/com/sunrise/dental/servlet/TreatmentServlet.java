package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
@WebServlet("/treatment")
public class TreatmentServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Get data from HTML form

        String name = request.getParameter("name");
        String charge  = request.getParameter("charge");

        HttpSession session = request.getSession();

        // Get logged-in username from session
        String username = (String) session.getAttribute("username");
        // SQL query

        String sql = "INSERT INTO treatment" +
                "(name,charge) " +
                "VALUES (?, ?)";

        try {

            // Connect to database

            Connection connection =
                    DBConnection.getConnection();

            if (connection == null) {

                response.getWriter().println(
                        "Database connection failed!"
                );

                return;
            }
            // Prepare SQL

            PreparedStatement statement =
                    connection.prepareStatement(sql);


            statement.setString(1, name);
            statement.setString(2, charge);


            // Insert data
            int result =
                    statement.executeUpdate();

            if (result > 0) {

                // Admin → admin-billing.jsp
                if ("admin".equalsIgnoreCase(username)) {

                    response.sendRedirect(
                            request.getContextPath()
                                    + "/admin-treatment.html?success=true"
                    );

                } else {

                    // Staff → staff-billing.jsp
                    response.sendRedirect(
                            request.getContextPath()
                                    + "/staff-treatment.html?success=true"
                    );
                }

            } else {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin-registerAppoinment.html?success=false"
                );
            }

            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                    "Database Error: " + e.getMessage()
            );
        }
    }
}
