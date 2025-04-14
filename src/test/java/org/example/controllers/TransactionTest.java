package org.example.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.example.utils.JdbcUtil;

public class TransactionTest {
    public static void main(String[] args) {
        try (Connection conn = JdbcUtil.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO images (name, path) VALUES (?, ?)");
            stmt1.setString(1, "trans-test");
            stmt1.setString(2, "/images/trans-test.jpg");
            stmt1.execute();

            // Simulate failure
            throw new RuntimeException("Failure after first insert");

        } catch (Exception e) {
            System.err.println("Rolling back due to error: " + e.getMessage());
        }
    }
}

