package ru.ase.mars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ase.mars.entity.EmployeeEntity;
import ru.ase.mars.entity.TimeEntity;
import ru.ase.mars.enums.Roles;
import ru.ase.mars.repository.EmployeeRepository;
import ru.ase.mars.repository.ReportRepository;
import ru.ase.mars.entity.ReportEntity;
import ru.ase.mars.enums.Statuses;
import ru.ase.mars.repository.TimeRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class MarsController {

    private ReportRepository reportRepository;

    private TimeRepository timeRepository;

    private EmployeeRepository employeeRepository;

    @GetMapping(path = "/")
    public ResponseEntity<Object> healthCheck() {
        return ResponseEntity.ok("Just do it");
    }

    @GetMapping(path = "/report/list")
    public ResponseEntity<List<ReportEntity>> list() {
        EmployeeEntity employee = employeeRepository.getByName(SecurityContextHolder.getContext().getAuthentication().getName());
        List<ReportEntity> reports = employee.getRole().equals(Roles.INSPECTOR) ?
                reportRepository.findAll() :
                reportRepository.findByAuthor(employee.getName());
        return ResponseEntity.ok(reports);
    }

    @PostMapping(path = "/report")
    public ResponseEntity<ReportEntity> add(@RequestBody ReportEntity newReport, MultipartFile file) {
        newReport.setLastUpdated(LocalDateTime.now());
        newReport.setAuthor(SecurityContextHolder.getContext().getAuthentication().getName());
        newReport.setState(Statuses.CREATE);
        //newReport.setFile(file.getName());
        ReportEntity report = reportRepository.save(newReport);
        return ResponseEntity.ok(report);
    }

    @GetMapping(path = "/report/{id}")
    public ResponseEntity<ReportEntity> get(@PathVariable("id") Integer id) {
        ReportEntity report = reportRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(report);
    }

    @GetMapping(path = "/report/{id}/file")
    public ResponseEntity<Resource> download(@PathVariable("id") Integer id) {
        ReportEntity report = reportRepository.findById(id).orElseThrow();
        ByteArrayResource resource = new ByteArrayResource(new byte[0]);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PutMapping(path = "/report/{id}")
    public ResponseEntity<ReportEntity> edit(@PathVariable("id") Integer id, @RequestBody String title, MultipartFile file) {
        ReportEntity report = reportRepository.findById(id).orElseThrow();
        report.setTitle(title);
        //report.setFile(file.getName());
        report.setLastUpdated(LocalDateTime.now());
        report.setState(Statuses.CREATE);
        report = reportRepository.save(report);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(report);
    }

    @PostMapping(path = "/report/{id}/approve")
    public ResponseEntity<ReportEntity> approve(@PathVariable("id") Integer id) {
        ReportEntity report = reportRepository.findById(id).orElseThrow();
        report.setState(Statuses.APPROVE);
        report = reportRepository.save(report);

        return ResponseEntity.ok(report);
    }

    @PostMapping(path = "/report/{id}/reject")
    public ResponseEntity<ReportEntity> reject(@PathVariable("id") Integer id, @RequestBody String comment) {
        ReportEntity report = reportRepository.findById(id).orElseThrow();
        report.setState(Statuses.REJECT);
        report.setComment(comment);
        report = reportRepository.save(report);

        return ResponseEntity.ok(report);
    }

    @PostMapping(path = "/times")
    public ResponseEntity<ReportEntity> addTime(MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TimeEntity> times = (List<TimeEntity>) objectMapper.readValue(file.getBytes(), List.class);
        timeRepository.saveAll(times);

        return ResponseEntity.ok().build();
    }
}
