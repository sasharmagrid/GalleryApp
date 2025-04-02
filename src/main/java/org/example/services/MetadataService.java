package org.example.services;

import org.example.models.Metadata;
import org.example.utils.FileUtil;

public class MetadataService {
    public static Metadata getMetadata(String id) {
        return FileUtil.loadMetadata(id);
    }

    public static void updateMetadata(String id, String name, String location) {
        FileUtil.updateMetadata(id, name, location);
    }
}
