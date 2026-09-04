package com.sunrise.dental.servlet;

import com.sunrise.dental.util.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateDentistsTest {

    private UpdateDentistsServlet updateDentistsServlet;

    @BeforeEach
    void setUp() {
        updateDentistsServlet = new UpdateDentistsServlet();
    }

    @Test
    @DisplayName("TC_DENT_UPD_01: Verify Handling Non-Existent Dentist Update")
    void testUpdateNonExistentDentist() {
        int nonExistentId = 999999;
        String sql = "UPDATE dentist SET name=?, contactnumber=?, charge=? WHERE id=?";
        int rowsAffected = -1;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "Updated Name");
            statement.setString(2, "0771234567");
            statement.setString(3, "2500");
            statement.setInt(4, nonExistentId);

            rowsAffected = statement.executeUpdate();

        } catch (Exception e) {
            fail("Database Connection Error: " + e.getMessage());
        }

        assertEquals(0, rowsAffected, "Updating a non-existent dentist ID should affect 0 rows.");
    }
}