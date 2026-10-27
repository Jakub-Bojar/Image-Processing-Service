package com.example.imageprocessingservice.storage;

import java.io.InputStream;

public interface StorageService {
    void store(String key, InputStream data, long size, String contentType);

    InputStream load(String key);

    void delete(String key);
}