package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class DisplayDentistsTest {

    private DisplayDentistsServlet displayDentistsServlet;

    @BeforeEach
    void setUp() {
        displayDentistsServlet = new DisplayDentistsServlet();
    }

    @Test
    @DisplayName("TC_DENT_DISP_01: Verify Fetching All Dentists")
    void testGetAllDentists() {
        String sql = "SELECT id, name, contactnumber, charge FROM dentist";
        boolean isQueryValid = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            isQueryValid = true;

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertTrue(isQueryValid, "Should successfully execute query to fetch dentist details.");
    }

    @Test
    @DisplayName("TC_DENT_DISP_02: Verify Search Dentist with Non-Existent Name")
    void testSearchNonExistentDentist() {
        String nameFilter = "NonExistentDentist123";
        String sql = "SELECT id, name, contactnumber, charge FROM dentist WHERE name LIKE ?";
        boolean recordFound = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + nameFilter + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    recordFound = true;
                }
            }

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertFalse(recordFound, "Searching for a non-existent dentist name should return 0 rows.");
    }
}