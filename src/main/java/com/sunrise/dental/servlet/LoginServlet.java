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
import java.sql.ResultSet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("Login Username: " + username);

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DBConnection.getConnection()) {

            if (connection == null) {
                response.setContentType("text/plain");
                response.getWriter().println("Database connection failed!");
                return;
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setString(1, username);
                statement.setString(2, password);

                try (ResultSet resultSet = statement.executeQuery()) {

                    if (resultSet.next()) {

                        System.out.println("Login Successful!");

                        HttpSession session = request.getSession();

                        session.setAttribute("username",
                                resultSet.getString("username"));

                        session.setAttribute("fullname",
                                resultSet.getString("fullname"));

                        String role;

                        if ("admin".equalsIgnoreCase(username)
                                && "123".equals(password)) {
                            role = "admin";
                        } else {
                            role = "staff";
                        }

                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        response.getWriter().print(
                                "{\"success\":true,\"role\":\"" + role + "\"}"
                        );

                    } else {

                        System.out.println("Invalid Username or Password");

                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        response.getWriter().print(
                                "{\"success\":false,\"message\":\"Invalid Username or Password\"}"
                        );
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/plain");
            response.getWriter().println(
                    "Login Error: " + e.getMessage()
            );
        }
    }
}