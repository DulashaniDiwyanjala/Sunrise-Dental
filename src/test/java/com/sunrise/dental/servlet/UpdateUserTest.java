package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateUserTest {

    private UpdateUserServlet updateUserServlet;

    @BeforeEach
    void setUp() {
        updateUserServlet = new UpdateUserServlet();
    }

    @Test
    @DisplayName("TC_USER_UPD_01: Verify Handling Non-Existent User Update")
    void testUpdateNonExistentUser() {
        int nonExistentId = 999999;
        String sql = "UPDATE users SET fullname=?, email=?, username=?, password=? WHERE id=?";
        int rowsAffected = -1;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "Test Fullname");
            statement.setString(2, "test@example.com");
            statement.setString(3, "testusername");
            statement.setString(4, "testpassword");
            statement.setInt(5, nonExistentId);

            rowsAffected = statement.executeUpdate();

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertEquals(0, rowsAffected, "Updating a non-existent user ID should affect 0 rows.");
    }
}