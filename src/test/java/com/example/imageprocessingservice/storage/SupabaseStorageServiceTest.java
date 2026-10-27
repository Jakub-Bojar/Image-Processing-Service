package com.example.imageprocessingservice.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Checks how the service talks to the S3 client, without a real Supabase project
class SupabaseStorageServiceTest {
    private S3Client s3;
    private SupabaseStorageService storage;

    @BeforeEach
    void setUp() {
        s3 = mock(S3Client.class);
        storage = new SupabaseStorageService(s3, "images");
    }

    @Test
    void storeUploadsToTheBucketWithKeyAndContentType() {
        storage.store("users/1/photo.png", new ByteArrayInputStream(new byte[]{1, 2}), 2, "image/png");

        ArgumentCaptor<PutObjectRequest> request = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3).putObject(request.capture(), any(RequestBody.class));
        assertEquals("images", request.getValue().bucket());
        assertEquals("users/1/photo.png", request.getValue().key());
        assertEquals("image/png", request.getValue().contentType());
        assertEquals(2L, request.getValue().contentLength());
    }

    @Test
    void loadingAMissingKeyThrowsNotFound() {
        when(s3.getObject(any(GetObjectRequest.class)))
                .thenThrow(S3Exception.builder().statusCode(404).build());

        assertThrows(StoredFileNotFoundException.class, () -> storage.load("users/1/missing.png"));
    }

    @Test
    void otherErrorsBecomeStorageExceptions() {
        when(s3.getObject(any(GetObjectRequest.class)))
                .thenThrow(S3Exception.builder().statusCode(403).build());

        StorageException e = assertThrows(StorageException.class, () -> storage.load("users/1/photo.png"));
        assertFalse(e instanceof StoredFileNotFoundException);
    }

    @Test
    void deletingAMissingKeyDoesNothing() {
        when(s3.deleteObject(any(DeleteObjectRequest.class)))
                .thenThrow(S3Exception.builder().statusCode(404).build());

        assertDoesNotThrow(() -> storage.delete("users/1/missing.png"));
    }
}