package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteUserTest {

    private DeleteUserServlet deleteUserServlet;

    @BeforeEach
    void setUp() {
        deleteUserServlet = new DeleteUserServlet();
    }

    @Test
    @DisplayName("TC_DEL_01: Verify Handling Non-Existent User Deletion")
    void testDeleteNonExistentUser() {
        int nonExistentUserId = 999999;
        String sql = "DELETE FROM users WHERE id = ?";
        int rowsAffected = 0;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, nonExistentUserId);
            rowsAffected = statement.executeUpdate();

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertEquals(0, rowsAffected, "Deleting a non-existent user ID should not affect any rows.");
    }
}