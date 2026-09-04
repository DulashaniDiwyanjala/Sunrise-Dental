package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterUserTest {

    private RegisterUserServlet registerUserServlet;

    @BeforeEach
    void setUp() {
        registerUserServlet = new RegisterUserServlet();
    }

    @Test
    @DisplayName("TC_USER_REG_01: Verify User Registration Table Structure")
    void testUserTableStructure() {
        String sql = "SELECT fullname, email, username, password FROM users";
        boolean isQueryValid = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            isQueryValid = true;

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertTrue(isQueryValid, "Should successfully execute query on users table columns.");
    }

    @Test
    @DisplayName("TC_USER_REG_02: Verify Uniqueness Search for Existing Username")
    void testCheckNonExistentUsername() {
        String nonExistentUsername = "user_does_not_exist_9999";
        String sql = "SELECT * FROM users WHERE username = ?";
        boolean exists = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nonExistentUsername);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exists = true;
                }
            }

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertFalse(exists, "Checking a non-existent username should return false.");
    }
}