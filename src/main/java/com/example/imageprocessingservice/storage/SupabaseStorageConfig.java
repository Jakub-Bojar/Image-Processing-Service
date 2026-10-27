package com.example.imageprocessingservice.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@ConditionalOnProperty(name = "spring.app.storage.type", havingValue = "supabase")
public class SupabaseStorageConfig {

    // Supabase Storage speaks the S3 protocol, so the AWS SDK works once it's pointed at Supabase
    @Bean
    public S3Client supabaseS3Client(@Value("${spring.app.storage.supabase.endpoint}") String endpoint,
                                     @Value("${spring.app.storage.supabase.region}") String region,
                                     @Value("${spring.app.storage.supabase.access-key-id}") String accessKeyId,
                                     @Value("${spring.app.storage.supabase.secret-access-key}") String secretAccessKey) {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .forcePathStyle(true)
                .build();
    }
}