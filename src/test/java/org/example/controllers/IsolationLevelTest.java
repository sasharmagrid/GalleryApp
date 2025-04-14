package org.example.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.example.utils.JdbcUtil;

public class IsolationLevelTest {
    public static void main(String[] args) throws Exception {
        Thread transaction1 = new Thread(() -> {
            try (Connection conn = JdbcUtil.getConnection()) {
                conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                conn.setAutoCommit(false);

                PreparedStatement stmt = conn.prepareStatement("SELECT path FROM images WHERE name = ?");
                stmt.setString(1, "image_1");

                ResultSet rs1 = stmt.executeQuery();
                if (rs1.next()) {
                    System.out.println("[T1] First read: " + rs1.getString("path"));
                }

                Thread.sleep(5000);

                ResultSet rs2 = stmt.executeQuery();
                if (rs2.next()) {
                    System.out.println("[T1] Second read: " + rs2.getString("path"));
                }

                conn.commit();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        Thread transaction2 = new Thread(() -> {
            try {
                Thread.sleep(2000);

                try (Connection conn = JdbcUtil.getConnection()) {
                    conn.setAutoCommit(false);

                    PreparedStatement stmt = conn.prepareStatement("UPDATE images SET path = ? WHERE name = ?");
                    stmt.setString(1, "/images/updated_by_T2.jpg");
                    stmt.setString(2, "image_1");

                    stmt.executeUpdate();
                    conn.commit();
                    System.out.println("[T2] Updated path for image_1");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });

        transaction1.start();
        transaction2.start();

        transaction1.join();
        transaction2.join();
    }
}

