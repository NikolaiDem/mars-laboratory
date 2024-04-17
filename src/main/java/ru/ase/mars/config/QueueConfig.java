package ru.ase.mars.config;

import io.minio.MinioClient;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Value("${spring.datasource.minio.url}")
    private String minioUrl;

    @Value("${spring.datasource.minio.username}")
    private String minioUsername;

    @Value("${spring.datasource.minio.password}")
    private String minioPassword;

    @Bean
    public BlockingQueue<Integer> reportQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioUsername, minioPassword)
                .build();
    }
}