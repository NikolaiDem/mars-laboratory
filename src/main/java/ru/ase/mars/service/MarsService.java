package ru.ase.mars.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ase.mars.dto.ReportDto;
import ru.ase.mars.entity.EmployeeEntity;
import ru.ase.mars.entity.Report;
import ru.ase.mars.enums.Statuses;
import ru.ase.mars.repository.ReportRepository;

@AllArgsConstructor
@Service
@Slf4j
public class MarsService {

    private BlockingQueue<Integer> reportQueue;
    private ReportRepository reportRepository;
    private MinioClient minioClient;

    public Report add(ReportDto reportDto, EmployeeEntity user, MultipartFile multipartFile) {
        String fileUuid = saveFile(multipartFile);
        Report report = mapToReportDto(reportDto, fileUuid);
        report.setAuthor(user);
        return reportRepository.save(report);
    }

    @NotNull
    private Report mapToReportDto(ReportDto reportDto, String fileUuid) {
        Report report = new Report();
        report.setLastUpdated(LocalDateTime.now());
        report.setState(Statuses.CREATE);
        report.setTitle(reportDto.getTitle());
        report.setFileUuid(fileUuid);
        return report;
    }

    private String saveFile(MultipartFile multipartFile) {
        if (multipartFile == null) {
            return "";
        }
        String fileUuid = UUID.randomUUID().toString();
        try {
            InputStream inputStream = multipartFile.getInputStream();
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs
                .builder()
                .bucket("bucket1")
                .object(fileUuid)
                .stream(inputStream, multipartFile.getSize(), 0).build());
            log.info("this file {} storage in bucket: {} on date: {}", objectWriteResponse.etag(), objectWriteResponse.bucket(), objectWriteResponse.versionId());
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage());
        }
        return fileUuid;
    }

    public byte[] download(String fileId) {
        try (InputStream obj = minioClient
                .getObject(GetObjectArgs.builder()
                        .bucket("bucket1")
                        .object(fileId)
                        .build())) {

            return IOUtils.toByteArray(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Report edit(ReportDto reportDto, MultipartFile multipartFile) {
        return reportRepository.findById(reportDto.getId())
            .filter(report -> Statuses.CREATE == report.getState()
            || Statuses.REJECT == report.getState())
            .map(report -> {
                String fileUuid = saveFile(multipartFile);
                report.setTitle(reportDto.getTitle());
                report.setFileUuid(fileUuid);
                report.setLastUpdated(LocalDateTime.now());

                return reportRepository.save(report);
            })
            .orElse(null);
    }

    public void approve(Report report) {
        report.setState(Statuses.APPROVE);
        reportRepository.save(report);
        reportQueue.offer(report.getId());
    }
}
