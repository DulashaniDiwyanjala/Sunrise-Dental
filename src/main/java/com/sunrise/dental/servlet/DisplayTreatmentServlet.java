package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/displaytreatment")
public class DisplayTreatmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");

        String sql;

        if (name != null && !name.trim().isEmpty()) {

            sql = "SELECT id, name, charge " +
                    "FROM treatment " +
                    "WHERE name LIKE ?";

        } else {

            sql = "SELECT id, name, charge " +
                    "FROM treatment";
        }

        try {

            Connection connection =
                    DBConnection.getConnection();

            if (connection == null) {

                response.setStatus(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                );

                out.print(
                        "{\"error\":\"Database connection failed\"}"
                );

                return;
            }

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            if (name != null && !name.trim().isEmpty()) {

                statement.setString(
                        1,
                        "%" + name.trim() + "%"
                );
            }

            ResultSet resultSet =
                    statement.executeQuery();

            // Start JSON array
            out.print("[");

            boolean first = true;

            while (resultSet.next()) {

                if (!first) {
                    out.print(",");
                }

                out.print("{");

                // ID
                out.print("\"id\":" + resultSet.getInt("id") + ",");

                // NAME
                out.print("\"name\":\"" + escapeJson(resultSet.getString("name")) + "\",");



                // CHARGE (Quoted properly as string)
                out.print("\"charge\":\"" + escapeJson(resultSet.getString("charge")) + "\"");

                out.print("}");

                first = false;
            }

            // End JSON array
            out.print("]");

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            out.print(
                    "{\"error\":\"" +
                            escapeJson(e.getMessage()) +
                            "\"}"
            );
        }
    }

    // Escape special JSON characters
    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}