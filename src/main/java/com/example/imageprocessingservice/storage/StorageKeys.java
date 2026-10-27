package com.example.imageprocessingservice.storage;

import java.util.UUID;

public final class StorageKeys {
    private StorageKeys() {
    }

    // Built from the user's id and a random UUID, never from the uploaded filename
    public static String forUpload(Long userId, String extension) {
        return "users/%d/%s.%s".formatted(userId, UUID.randomUUID(), extension);
    }
}