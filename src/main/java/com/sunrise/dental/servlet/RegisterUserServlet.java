package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/register")
public class RegisterUserServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Get data from HTML form

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username  = request.getParameter("username");
        String password = request.getParameter("password");


        // SQL query

        String sql = "INSERT INTO users " +
                "(fullname,email,username ,password) " +
                "VALUES (?, ?, ?, ?)";


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


            statement.setString(1, fullName);
            statement.setString(2, email);
            statement.setString(3, username);
            statement.setString(4, password);


            // Insert data

            int result =
                    statement.executeUpdate();


            if (result > 0) {

                // Registration successful

                response.sendRedirect(
                        request.getContextPath() + "/users.html?success=true"
                );
            } else {

                response.sendRedirect(
                        request.getContextPath() + "/users.html?success=false"
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