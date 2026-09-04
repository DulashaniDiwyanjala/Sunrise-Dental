package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private LoginServlet loginServlet;

    @BeforeEach
    void setUp() {
        // Servlet instance එක සාදා ගැනීම
        loginServlet = new LoginServlet();
    }

    @Test
    @DisplayName("TC_AUTH_01: Verify Successful Login with Valid Admin Credentials")
    void testValidLogin() {
        String username = "admin";
        String password = "123";

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        boolean loginSuccess = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    loginSuccess = true;
                }
            }

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        // Output Assertion
        assertTrue(loginSuccess, "Login should be successful because valid credentials were provided");
    }

    @Test
    @DisplayName("TC_AUTH_02: IDeny login access when an invalid password is provided.")
    void testInvalidPasswordLogin() {
        String username = "admin";
        String password = "wrongpassword123"; // අසත්‍ය මුරපදය

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        boolean loginSuccess = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    loginSuccess = true;
                }
            }

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        // Output Assertion (False විය යුතුය)
        assertFalse(loginSuccess, "Login attempt should fail when an incorrect password is provided.");
    }

    @Test
    @DisplayName("TC_AUTH_03: Prevent login when a non-existent username is provided.")
    void testNonExistingUserLogin() {
        String username = "non_existing_user_999";
        String password = "123";

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        boolean loginSuccess = false;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    loginSuccess = true;
                }
            }

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertFalse(loginSuccess, "A user that does not exist in the system cannot log in.");
    }
}