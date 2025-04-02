package org.example.models;

import java.io.Serializable;

public class Image implements Serializable {
    private final String id;
    private final String name;
    private final String filePath;

    public Image(String id, String name, String filePath) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getFilePath() { return filePath; }
}