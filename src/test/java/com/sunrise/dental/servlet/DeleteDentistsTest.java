package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteDentistsTest {

    private DeleteDentistsServlet deleteDentistsServlet;

    @BeforeEach
    void setUp() {
        deleteDentistsServlet = new DeleteDentistsServlet();
    }

    @Test
    @DisplayName("TC_DENT_DEL_01: Verify Handling Non-Existent Dentist Deletion")
    void testDeleteNonExistentDentist() {
        int nonExistentId = 999999;
        String sql = "DELETE FROM dentist WHERE id=?";
        int rowsAffected = -1;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, nonExistentId);
            rowsAffected = statement.executeUpdate();

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertEquals(0, rowsAffected, "Deleting a non-existent dentist ID should affect 0 rows.");
    }
}