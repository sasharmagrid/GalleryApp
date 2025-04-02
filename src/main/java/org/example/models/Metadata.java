package org.example.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Metadata implements Serializable {
    private final String id;
    private final String name;
    private final String location;
    private final int width;
    private final int height;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime modifiedDateTime;

    public Metadata(String id, String name, String location, int width, int height, LocalDateTime createdDateTime, LocalDateTime modifiedDateTime) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.width = width;
        this.height = height;
        this.createdDateTime = createdDateTime;
        this.modifiedDateTime = modifiedDateTime;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public LocalDateTime getCreatedDateTime() { return createdDateTime; }
    public LocalDateTime getModifiedDateTime() { return modifiedDateTime; }
}
