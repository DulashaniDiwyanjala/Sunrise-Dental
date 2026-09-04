package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterAppoinmentTest {

    private RegisterAppoinmentServlet registerAppoinmentServlet;

    @BeforeEach
    void setUp() {
        registerAppoinmentServlet = new RegisterAppoinmentServlet();
    }

    @Test
    @DisplayName("TC_APPT_REG_01: Verify Retrieval of Dentists and Treatments for Dropdown")
    void testGetDentistsAndTreatments() {
        String dentistSql = "SELECT id, name FROM dentist ORDER BY name";
        String treatmentSql = "SELECT id, name FROM treatment ORDER BY name";
        boolean queriesExecutable = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement dentistStmt = connection.prepareStatement(dentistSql);
             ResultSet dentistRs = dentistStmt.executeQuery();
             PreparedStatement treatmentStmt = connection.prepareStatement(treatmentSql);
             ResultSet treatmentRs = treatmentStmt.executeQuery()) {

            queriesExecutable = true;

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertTrue(queriesExecutable, "Should successfully execute SELECT queries on dentist and treatment tables.");
    }

    @Test
    @DisplayName("TC_APPT_REG_02: Verify Appointment SQL Structure and Non-Existent Record Search")
    void testSearchNonExistentAppointment() {
        String testApptNumber = "NON_EXISTENT_APPT_999";
        String sql = "SELECT * FROM appointment WHERE appointment_number = ?";
        boolean recordFound = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, testApptNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    recordFound = true;
                }
            }

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertFalse(recordFound, "Querying a non-existent appointment number should return 0 records.");
    }
}