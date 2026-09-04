package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateTreatmentTest {

    private UpdateTreatmentServlet updateTreatmentServlet;

    @BeforeEach
    void setUp() {
        updateTreatmentServlet = new UpdateTreatmentServlet();
    }

    @Test
    @DisplayName("TC_TRT_UPD_01: Verify Handling Non-Existent Treatment Update")
    void testUpdateNonExistentTreatment() {
        try (Connection connection = DBConnection.getConnection()) {
            assertNotNull(connection, "Database connection should be established");

            String sql = "UPDATE treatment SET name=?, charge=? WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, "Updated Name");
                statement.setString(2, "5000");
                statement.setInt(3, -1); // Invalid ID

                int rowsAffected = statement.executeUpdate();
                assertEquals(0, rowsAffected, "Updating non-existent treatment ID should affect 0 rows");
            }
        } catch (Exception e) {
            fail("Database update execution failed: " + e.getMessage());
        }
    }
}