package ru.ase.mars.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ase.mars.repository.ReportRepository;
import ru.ase.mars.entity.ReportEntity;
import ru.ase.mars.enums.Statuses;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class MarsController {

    private ReportRepository reportRepository;

    @GetMapping(path = "/")
    public ResponseEntity<Object> healthCheck() {
        return ResponseEntity.ok("Just do it");
    }

    @GetMapping(path = "/report/list")
    public ResponseEntity<List<ReportEntity>> list() {
        //todo scientist must get only his reports, inspector get all
        List<ReportEntity> reports = reportRepository.findAll();
        return ResponseEntity.ok(reports);
    }

    @PostMapping(path = "/report")
    public ResponseEntity<ReportEntity> add(@RequestBody ReportEntity newReport, MultipartFile file) {
        newReport.setLastUpdated(LocalDateTime.now());
        newReport.setAuthor(SecurityContextHolder.getContext().getAuthentication().getName());
        newReport.setState(Statuses.CREATE);
        newReport.setFile(file.getName());
        ReportEntity report = reportRepository.save(newReport);
        return ResponseEntity.ok(report);
    }

    @GetMapping(path = "/report/{id}")
    public ResponseEntity<ReportEntity> get(@PathVariable("id") Integer id) {
        ReportEntity report = reportRepository.getReferenceById(id);
        return ResponseEntity.ok(report);
    }

    @GetMapping(path = "/report/{id}/file")
    public ResponseEntity<Resource> download(@PathVariable("id") Integer id) {
        ReportEntity report = reportRepository.getReferenceById(id);
        ByteArrayResource resource = new ByteArrayResource(new byte[0]);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
