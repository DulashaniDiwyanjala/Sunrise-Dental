package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class DisplayUsersTest {

    private DisplayUsersServlet displayUsersServlet;

    @BeforeEach
    void setUp() {
        displayUsersServlet = new DisplayUsersServlet();
    }

    @Test
    @DisplayName("TC_USER_DISP_01: Verify Fetching All Users")
    void testGetAllUsers() {
        String sql = "SELECT id, fullname, email, username, password FROM users";
        boolean isQueryValid = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            isQueryValid = true;

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertTrue(isQueryValid, "Should successfully execute query to fetch user details.");
    }

    @Test
    @DisplayName("TC_USER_DISP_02: Verify Search User with Non-Existent Fullname")
    void testSearchNonExistentUser() {
        String nameFilter = "NonExistentUser999";
        String sql = "SELECT id, fullname, email, username, password FROM users WHERE fullname LIKE ?";
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

        assertFalse(recordFound, "Searching for a non-existent user fullname should return 0 rows.");
    }
}