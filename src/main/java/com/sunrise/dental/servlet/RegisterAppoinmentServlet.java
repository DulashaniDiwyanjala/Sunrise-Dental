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

@WebServlet("/appoinment")
public class RegisterAppoinmentServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder json = new StringBuilder();

        try (Connection connection = DBConnection.getConnection()) {

            // =========================
            // GET DENTISTS
            // =========================

            String dentistSQL =
                    "SELECT id, name FROM dentist ORDER BY name";

            PreparedStatement dentistStatement =
                    connection.prepareStatement(dentistSQL);

            ResultSet dentistResult =
                    dentistStatement.executeQuery();

            json.append("{");
            json.append("\"dentists\":[");

            boolean firstDentist = true;

            while (dentistResult.next()) {

                if (!firstDentist) {
                    json.append(",");
                }

                json.append("{")
                        .append("\"id\":")
                        .append(dentistResult.getInt("id"))
                        .append(",")
                        .append("\"name\":\"")
                        .append(dentistResult.getString("name"))
                        .append("\"")
                        .append("}");

                firstDentist = false;
            }

            json.append("],");


            // =========================
            // GET TREATMENTS
            // =========================

            String treatmentSQL =
                    "SELECT id, name FROM treatment ORDER BY name";

            PreparedStatement treatmentStatement =
                    connection.prepareStatement(treatmentSQL);

            ResultSet treatmentResult =
                    treatmentStatement.executeQuery();

            json.append("\"treatments\":[");

            boolean firstTreatment = true;

            while (treatmentResult.next()) {

                if (!firstTreatment) {
                    json.append(",");
                }

                json.append("{")
                        .append("\"id\":")
                        .append(treatmentResult.getInt("id"))
                        .append(",")
                        .append("\"name\":\"")
                        .append(treatmentResult.getString("name"))
                        .append("\"")
                        .append("}");

                firstTreatment = false;
            }

            json.append("]");

            json.append("}");

            response.getWriter().print(json.toString());

            dentistResult.close();
            dentistStatement.close();

            treatmentResult.close();
            treatmentStatement.close();

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            response.getWriter().print(
                    "{\"error\":\"Failed to load dentists and treatments\"}"
            );
        }
    }



    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String appoinment_number = request.getParameter("appoinment_number");
        String fullname = request.getParameter("fullname");
        String address = request.getParameter("address");
        String contact = request.getParameter("contact");
        String dentist = request.getParameter("dentist");
        String type = request.getParameter("type");
        String date = request.getParameter("date");
        String time = request.getParameter("time");

        HttpSession session = request.getSession();

        // Get logged-in username from session
        String username = (String) session.getAttribute("username");

        // Data ටික Session එකේ Store කර ගැනීම
        session.setAttribute("apptNo", appoinment_number);
        session.setAttribute("patientName", fullname);
        session.setAttribute("date", date);
        session.setAttribute("time", time);
        session.setAttribute("dentist", dentist);

        // SQL query
        String sql = "INSERT INTO appointment " +
                "(appointment_number,fullname,address,contact,dentist_id,treatment_id,appointment_date,appointment_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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

            statement.setString(1, appoinment_number);
            statement.setString(2, fullname);
            statement.setString(3, address);
            statement.setString(4, contact);
            statement.setInt(5, Integer.parseInt(dentist));
            statement.setInt(6,Integer.parseInt(type));
            statement.setString(7, date);
            statement.setString(8, time);

            // Insert data
            int result =
                    statement.executeUpdate();

            if (result > 0) {

                // Admin → admin-billing.jsp
                if ("admin".equalsIgnoreCase(username)) {

                    response.sendRedirect(
                            request.getContextPath()
                                    + "/admin-billing.jsp?success=true"
                    );

                } else {

                    // Staff → staff-billing.jsp
                    response.sendRedirect(
                            request.getContextPath()
                                    + "/staff-billing.jsp?success=true"
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