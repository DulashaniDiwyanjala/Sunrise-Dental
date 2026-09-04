package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterDentistTest {

    private RegisterDentistServlet registerDentistServlet;

    @BeforeEach
    void setUp() {
        registerDentistServlet = new RegisterDentistServlet();
    }

    @Test
    @DisplayName("TC_DENT_REG_01: Verify Querying Dentist Table Columns")
    void testDentistTableStructure() {
        String sql = "SELECT name, contactnumber, charge FROM dentist";
        boolean isQueryValid = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            isQueryValid = true;

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertTrue(isQueryValid, "Should successfully query dentist table with required columns.");
    }

    @Test
    @DisplayName("TC_DENT_REG_02: Verify Searching Non-Existent Dentist")
    void testSearchNonExistentDentist() {
        String nonExistentContact = "0000000000";
        String sql = "SELECT * FROM dentist WHERE contactnumber = ?";
        boolean found = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nonExistentContact);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    found = true;
                }
            }

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertFalse(found, "Searching for a non-existent contact number should return no results.");
    }
}