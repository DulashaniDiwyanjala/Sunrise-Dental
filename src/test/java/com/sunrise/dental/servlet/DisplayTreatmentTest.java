package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class DisplayTreatmentTest {

    private DisplayTreatmentServlet displayTreatmentServlet;

    @BeforeEach
    void setUp() {
        displayTreatmentServlet = new DisplayTreatmentServlet();
    }

    @Test
    @DisplayName("TC_TRT_DISP_01: Verify Fetching All Treatments")
    void testFetchAllTreatments() {
        try (Connection connection = DBConnection.getConnection()) {
            assertNotNull(connection, "Database connection should be established");

            String sql = "SELECT id, name, charge FROM treatment";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                assertNotNull(resultSet, "ResultSet should not be null");
                boolean hasData = resultSet.next();
                assertTrue(hasData, "Database should contain treatment records");
            }
        } catch (Exception e) {
            fail("Database operation failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("TC_TRT_DISP_02: Verify Searching Treatment with Non-Existent Name")
    void testSearchNonExistentTreatment() {
        try (Connection connection = DBConnection.getConnection()) {
            assertNotNull(connection, "Database connection should be established");

            String sql = "SELECT id, name, charge FROM treatment WHERE name LIKE ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, "%NonExistentTreatment999%");
                try (ResultSet resultSet = statement.executeQuery()) {
                    assertFalse(resultSet.next(), "Search for non-existent treatment should return no results");
                }
            }
        } catch (Exception e) {
            fail("Database operation failed: " + e.getMessage());
        }
    }
}