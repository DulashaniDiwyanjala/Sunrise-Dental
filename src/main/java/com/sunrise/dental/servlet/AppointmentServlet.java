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

@WebServlet("/appointments")
public class AppointmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String dentist = request.getParameter("dentist");
        String date = request.getParameter("date");

        StringBuilder sql = new StringBuilder(
                "SELECT " +
                        "a.appointment_number, " +
                        "a.fullname, " +
                        "d.name AS dentist_name, " +
                        "t.name AS treatment_name, " +
                        "a.appointment_date, " +
                        "a.appointment_time " +
                        "FROM appointment a " +
                        "LEFT JOIN dentist d ON a.dentist_id = d.id " +
                        "LEFT JOIN treatment t ON a.treatment_id = t.id " +
                        "WHERE 1=1 "
        );

        // Dentist filter
        if (dentist != null && !dentist.trim().isEmpty()) {
            sql.append("AND d.name LIKE ? ");
        }

        // Date filter
        if (date != null && !date.trim().isEmpty()) {
            sql.append("AND a.appointment_date = ? ");
        }

        sql.append(
                "ORDER BY a.appointment_date DESC, " +
                        "a.appointment_time DESC"
        );

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement statement =
                        conn.prepareStatement(sql.toString())
        ) {

            int index = 1;

            // Dentist search
            if (dentist != null && !dentist.trim().isEmpty()) {

                statement.setString(
                        index++,
                        "%" + dentist.trim() + "%"
                );
            }

            // Date search
            if (date != null && !date.trim().isEmpty()) {

                statement.setString(
                        index++,
                        date
                );
            }

            ResultSet rs = statement.executeQuery();

            PrintWriter out = response.getWriter();

            out.print("[");

            boolean first = true;

            while (rs.next()) {

                if (!first) {
                    out.print(",");
                }

                out.print("{");

                // Appointment number
                out.print(
                        "\"number\":" +
                                rs.getInt("appointment_number") +
                                ","
                );

                // Patient name
                out.print(
                        "\"fullname\":\"" +
                                escapeJson(rs.getString("fullname")) +
                                "\","
                );

                // Dentist name
                out.print(
                        "\"dentist\":\"" +
                                escapeJson(rs.getString("dentist_name")) +
                                "\","
                );

                // Treatment name
                out.print(
                        "\"treatmenttype\":\"" +
                                escapeJson(rs.getString("treatment_name")) +
                                "\","
                );

                // Date
                out.print(
                        "\"date\":\"" +
                                rs.getDate("appointment_date") +
                                "\","
                );

                // Time
                out.print(
                        "\"time\":\"" +
                                rs.getTime("appointment_time") +
                                "\""
                );

                out.print("}");

                first = false;
            }

            out.print("]");

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            response.getWriter().write(
                    "{\"error\":\"" +
                            escapeJson(e.getMessage()) +
                            "\"}"
            );
        }
    }

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