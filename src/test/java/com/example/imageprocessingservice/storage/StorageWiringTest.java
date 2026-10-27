package com.example.imageprocessingservice.storage;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

// Checks that spring.app.storage.type picks the right StorageService
class StorageWiringTest {

    @Nested
    @SpringBootTest
    class LocalByDefault {
        @Autowired
        private StorageService storageService;

        @Test
        void usesLocalStorage() {
            assertInstanceOf(LocalStorageService.class, storageService);
        }
    }

    @Nested
    @SpringBootTest(properties = {
            "spring.app.storage.type=supabase",
            "spring.app.storage.supabase.endpoint=http://localhost:1/storage/v1/s3",
            "spring.app.storage.supabase.region=eu-west-2",
            "spring.app.storage.supabase.access-key-id=test",
            "spring.app.storage.supabase.secret-access-key=test",
            "spring.app.storage.supabase.bucket=images"
    })
    class SupabaseWhenConfigured {
        @Autowired
        private StorageService storageService;

        @Test
        void usesSupabaseStorage() {
            assertInstanceOf(SupabaseStorageService.class, storageService);
        }
    }
}