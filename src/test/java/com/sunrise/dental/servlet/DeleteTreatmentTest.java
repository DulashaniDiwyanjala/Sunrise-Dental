package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteTreatmentTest {

    private DeleteTreatmentServlet deleteTreatmentServlet;

    @BeforeEach
    void setUp() {
        deleteTreatmentServlet = new DeleteTreatmentServlet();
    }

    @Test
    @DisplayName("TC_TRT_DEL_01: Verify Handling Non-Existent Treatment Deletion")
    void testDeleteNonExistentTreatment() {
        try (Connection connection = DBConnection.getConnection()) {
            assertNotNull(connection, "Database connection should be established");

            String sql = "DELETE FROM treatment WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, -1); // Invalid ID

                int rowsAffected = statement.executeUpdate();
                assertEquals(0, rowsAffected, "Deleting non-existent treatment ID should affect 0 rows");
            }
        } catch (Exception e) {
            fail("Database deletion execution failed: " + e.getMessage());
        }
    }
}