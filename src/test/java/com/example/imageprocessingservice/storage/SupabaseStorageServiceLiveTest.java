package com.example.imageprocessingservice.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

// Talks to your real Supabase bucket. Skipped unless the SUPABASE_S3_* variables are set.
@EnabledIfEnvironmentVariable(named = "SUPABASE_S3_ENDPOINT", matches = ".+")
class SupabaseStorageServiceLiveTest {

    @Test
    void storeLoadAndDeleteAgainstRealBucket() throws Exception {
        var s3 = new SupabaseStorageConfig().supabaseS3Client(
                System.getenv("SUPABASE_S3_ENDPOINT"),
                System.getenv("SUPABASE_S3_REGION"),
                System.getenv("SUPABASE_S3_ACCESS_KEY_ID"),
                System.getenv("SUPABASE_S3_SECRET_ACCESS_KEY"));
        String bucket = System.getenv().getOrDefault("SUPABASE_S3_BUCKET", "images");
        var storage = new SupabaseStorageService(s3, bucket);

        String key = StorageKeys.forUpload(0L, "txt");
        byte[] bytes = "hello supabase".getBytes();

        storage.store(key, new ByteArrayInputStream(bytes), bytes.length, "text/plain");
        try (InputStream loaded = storage.load(key)) {
            assertArrayEquals(bytes, loaded.readAllBytes());
        }

        storage.delete(key);
        assertThrows(StoredFileNotFoundException.class, () -> storage.load(key));
        s3.close();
    }
}