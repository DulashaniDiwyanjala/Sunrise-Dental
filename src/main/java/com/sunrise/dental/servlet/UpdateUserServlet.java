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

@WebServlet("/updateUser")
public class UpdateUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String sql = "UPDATE users SET fullname=?, email=?, username=?, password=? WHERE id=?";

        try {

            Connection connection = DBConnection.getConnection();

            if (connection == null) {
                response.getWriter().print("failed");
                return;
            }

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, fullname);
            statement.setString(2, email);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.setInt(5, Integer.parseInt(id));

            int result = statement.executeUpdate();

            if (result > 0) {
                response.getWriter().print("success");
            } else {
                response.getWriter().print("failed");
            }

            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();
            response.getWriter().print("failed");
        }
    }
}