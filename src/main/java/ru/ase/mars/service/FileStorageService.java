package ru.ase.mars.service;

import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import java.nio.file.Path;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ase.mars.dto.FileResponse;


@Service
@AllArgsConstructor
@Slf4j
public class FileStorageService {

    public FileResponse addFile(MultipartFile file) {
        MinioClient minioClient =
            MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("user1", "password1")
                .build();

        Path path = Path.of(file.getOriginalFilename());
        try {
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs
                .builder()
                .bucket("bucket1")
                .object(file.getName() + LocalDateTime.now())
                .stream(file.getInputStream(), file.getSize(), 0).build());
            log.info("this file {} storage in bucket: {} on date: {}", objectWriteResponse.etag(), objectWriteResponse.bucket(), objectWriteResponse.versionId());
            return FileResponse.builder().build();
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }
}