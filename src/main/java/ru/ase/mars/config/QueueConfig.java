package ru.ase.mars.config;

import io.minio.MinioClient;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Bean
    public BlockingQueue<Integer> reportQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("user1", "password1")
                .build();
    }
}