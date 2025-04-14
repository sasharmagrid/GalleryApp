package org.example.controllers;

import org.example.utils.JdbcUtil;
import org.junit.jupiter.api.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Test Queries
//EXPLAIN ANALYZE SELECT * FROM images WHERE name = 'image_999999';
//EXPLAIN ANALYZE SELECT * FROM images WHERE name = 'image_500' AND path = '/images/image_500.jpg';

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdbcUtilTest {

    @BeforeAll
    public void setupH2() {
        JdbcUtil.overrideConnectionConfig(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
                "sa",
                "",
                "org.h2.Driver"
        );
        JdbcUtil.execute(
                "CREATE TABLE IF NOT EXISTS images (" +
                        "id SERIAL PRIMARY KEY, " +
                        "name VARCHAR(255), " +
                        "path VARCHAR(1024)" +
                        ");"
        );

        JdbcUtil.execute("INSERT INTO images (name, path) VALUES (?, ?)", "test-image-1", "/images/inserted-image.jpg");
        JdbcUtil.execute("INSERT INTO images (name, path) VALUES (?, ?)", "test-image-2", "/images/test2.jpg");
    }

    @AfterAll
    public void cleanup() {
        JdbcUtil.execute("DROP TABLE images");
    }

    @Test
    public void testFindOneImage() {
        String name = JdbcUtil.findOne(
                "SELECT path FROM images WHERE name = ?",
                rs -> {
                    try {
                        return rs.getString("path");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                },
                "inserted-image"
        );
        assertNull(name);
    }

    @Test
    public void testFindManyImages() {
        List<String> names = JdbcUtil.findMany(
                "SELECT name FROM images",
                rs -> {
                    try {
                        return rs.getString("name");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        assertEquals(6, names.size());
        assertTrue(names.contains("test-image-1"));
        assertTrue(names.contains("test-image-2"));
    }

    @Test
    public void testInsertAndRetrieve() {
        JdbcUtil.execute("INSERT INTO images (name, path) VALUES (?, ?)", "inserted-image", "/images/inserted.jpg");

        String path = JdbcUtil.findOne(
                "SELECT path FROM images WHERE name = ?",
                rs -> getString(rs, "path"),
                "inserted-image"
        );
        assertEquals("/images/inserted.jpg", path);
    }

    @Test
    public void testFindOneMultipleResultsThrows() {
        JdbcUtil.execute("INSERT INTO images (name, path) VALUES (?, ?)", "duplicate", "/a.jpg");
        JdbcUtil.execute("INSERT INTO images (name, path) VALUES (?, ?)", "duplicate", "/b.jpg");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            JdbcUtil.findOne(
                    "SELECT path FROM images WHERE name = ?",
                    rs -> getString(rs, "path"),
                    "duplicate"
            );
        });
        assertTrue(exception.getMessage().contains("More than one result found"));
    }

    @Test
    public void testFindManyReturnsAll() {
        JdbcUtil.execute("INSERT INTO images (name, path) VALUES (?, ?)", "one", "/1.jpg");
        JdbcUtil.execute("INSERT INTO images (name, path) VALUES (?, ?)", "two", "/2.jpg");

        List<String> names = JdbcUtil.findMany(
                "SELECT name FROM images ORDER BY name",
                rs -> getString(rs, "name")
        );

        assertTrue(names.contains("one"));
        assertTrue(names.contains("two"));
    }

    private static String getString(ResultSet rs, String column) {
        try {
            return rs.getString(column);
        } catch (SQLException e) {
            throw new RuntimeException("Error reading column: " + column, e);
        }
    }
}
