package com.example.imageprocessingservice.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.InputStream;

@Service
@ConditionalOnProperty(name = "spring.app.storage.type", havingValue = "supabase")
public class SupabaseStorageService implements StorageService {
    private final S3Client s3;
    private final String bucket;

    public SupabaseStorageService(S3Client s3, @Value("${spring.app.storage.supabase.bucket}") String bucket) {
        this.s3 = s3;
        this.bucket = bucket;
    }

    @Override
    public void store(String key, InputStream data, long size, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .contentLength(size)
                .build();
        try {
            s3.putObject(request, RequestBody.fromInputStream(data, size));
        } catch (SdkException e) {
            throw new StorageException("Could not store file " + key, e);
        }
    }

    @Override
    public InputStream load(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        try {
            return s3.getObject(request);
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                throw new StoredFileNotFoundException(key);
            }
            throw new StorageException("Could not read file " + key, e);
        } catch (SdkException e) {
            throw new StorageException("Could not read file " + key, e);
        }
    }

    @Override
    public void delete(String key) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        try {
            s3.deleteObject(request);
        } catch (S3Exception e) {
            // Already gone is fine, same as LocalStorageService
            if (e.statusCode() != 404) {
                throw new StorageException("Could not delete file " + key, e);
            }
        } catch (SdkException e) {
            throw new StorageException("Could not delete file " + key, e);
        }
    }
}