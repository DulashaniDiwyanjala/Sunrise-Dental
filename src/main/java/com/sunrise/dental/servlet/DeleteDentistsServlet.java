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

@WebServlet("/deleteDentist")
public class DeleteDentistsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        String sql =
                "DELETE FROM dentist WHERE id=?";

        try {

            Connection connection =
                    DBConnection.getConnection();

            if (connection == null) {
                response.getWriter().print("failed");
                return;
            }

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setInt(
                    1,
                    Integer.parseInt(id)
            );

            int result =
                    statement.executeUpdate();

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