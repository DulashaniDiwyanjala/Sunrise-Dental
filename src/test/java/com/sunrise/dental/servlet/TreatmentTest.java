package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class TreatmentTest {

    private TreatmentServlet treatmentServlet;

    @BeforeEach
    void setUp() {
        treatmentServlet = new TreatmentServlet();
    }

    @Test
    @DisplayName("TC_TRT_01: Verify Treatment Table Column Structure")
    void testTreatmentTableStructure() {
        String sql = "SELECT name, charge FROM treatment";
        boolean isQueryValid = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            isQueryValid = true;

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertTrue(isQueryValid, "Should successfully query treatment table columns.");
    }

    @Test
    @DisplayName("TC_TRT_02: Verify Search for Non-Existent Treatment")
    void testSearchNonExistentTreatment() {
        String nonExistentTreatmentName = "Invalid_Treatment_Name_123";
        String sql = "SELECT * FROM treatment WHERE name = ?";
        boolean found = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nonExistentTreatmentName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    found = true;
                }
            }

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertFalse(found, "Searching for a non-existent treatment name should return no records.");
    }
}