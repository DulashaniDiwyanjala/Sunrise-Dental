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

@WebServlet("/updateTreatment")
public class UpdateTreatmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String charge = request.getParameter("charge");

        String sql =
                "UPDATE treatment " +
                        "SET name=?, charge=? " +
                        "WHERE id=?";

        try {

            Connection connection =
                    DBConnection.getConnection();

            if (connection == null) {
                response.getWriter().print("failed");
                return;
            }

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, name);
            statement.setString(2, charge);

            // IMPORTANT: parameter is 4, not 5
            statement.setInt(3, Integer.parseInt(id));

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