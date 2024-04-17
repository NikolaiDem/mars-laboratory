package ru.ase.mars.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.ase.mars.dto.PeriodDto;
import ru.ase.mars.dto.ReportDto;
import ru.ase.mars.entity.EmployeeEntity;
import ru.ase.mars.entity.Period;
import ru.ase.mars.entity.Report;
import ru.ase.mars.enums.Roles;
import ru.ase.mars.enums.Statuses;
import ru.ase.mars.repository.CustomReportRepository;
import ru.ase.mars.repository.EmployeeRepository;
import ru.ase.mars.repository.ReportRepository;
import ru.ase.mars.repository.TimeRepository;
import ru.ase.mars.service.MarsService;
import ru.ase.mars.service.Scheduler;

@RestController
@RequestMapping
@AllArgsConstructor
public class MarsController {

    private ReportRepository reportRepository;

    private TimeRepository timeRepository;
    private Scheduler scheduler;
    private MarsService marsService;
    private CustomReportRepository customReportRepository;
    private ObjectMapper objectMapper;

    private EmployeeRepository employeeRepository;

    @GetMapping(path = "/")
    public ResponseEntity<Object> healthCheck() {
        return ResponseEntity.ok("Just do it");
    }

    @GetMapping(path = "/report/list")
    public ResponseEntity<List<Report>> list(@RequestParam(required = false) String sortField,
                                             @RequestParam(required = false) String sortOrder,
                                             @RequestParam(required = false) Integer authorId,
                                             @RequestParam(required = false) String state,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "3") int size) {
        EmployeeEntity employee = employeeRepository.getByName(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!employee.getRole().equals(Roles.INSPECTOR)) {
            authorId = employee.getId();
        }

        PageRequest pageable = PageRequest.of(page, size);
        List<Report> reports = customReportRepository.findBy(authorId, state, sortOrder, sortField, pageable);
        return ResponseEntity.ok(reports);
    }

    @PostMapping(path = "/report")
    public ResponseEntity<Report> add(@RequestPart(value = "report") ReportDto reportDto,
                                      @RequestPart(value = "file", required = false) MultipartFile file) {
        EmployeeEntity employee = employeeRepository.getByName(SecurityContextHolder.getContext().getAuthentication().getName());
        Report report = marsService.add(reportDto, employee, file);
        return ResponseEntity.ok(report);
    }

    @GetMapping(path = "/report/{id}")
    public ResponseEntity<Report> get(@PathVariable("id") Integer id) {
        Report report = reportRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(report);
    }

    @GetMapping(path = "/report/{id}/file")
    public ResponseEntity<Resource> download(@PathVariable("id") Integer id) {
        Report report = reportRepository.findById(id).orElseThrow();
        byte[] file = marsService.download(report.getFileUuid());
        ByteArrayResource resource = new ByteArrayResource(file);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=report.txt")
                .body(resource);
    }

    @PutMapping(path = "/report/{id}")
    public ResponseEntity<Report> edit(@PathVariable("id") Integer id, @RequestPart(value = "report") ReportDto reportDto, @RequestPart(value = "file", required = false) MultipartFile file) {
        reportDto.setId(id);
        Report edit = marsService.edit(reportDto, file);

        return ResponseEntity.ok()
                .body(edit);
    }

    @PostMapping(path = "/report/{id}/approve")
    public ResponseEntity<Report> approve(@PathVariable("id") Integer id) {
        Report report = reportRepository.findById(id).orElseThrow();
        marsService.approve(report);

        return ResponseEntity.ok(report);
    }

    @PostMapping(path = "/report/{id}/reject")
    public ResponseEntity<Report> reject(@PathVariable("id") Integer id, @RequestBody String comment) {
        Report report = reportRepository.findById(id).orElseThrow();
        report.setState(Statuses.REJECT);
        report.setComment(comment);
        report = reportRepository.save(report);

        return ResponseEntity.ok(report);
    }

    @PostMapping(path = "/times")
    public ResponseEntity<Report> addTime(@RequestPart("file") MultipartFile file) throws IOException {
        List<PeriodDto> periodDtos = objectMapper.readValue(new String(file.getBytes(), StandardCharsets.UTF_8), new TypeReference<>() {
        });
        List<Period> times = periodDtos.stream()
            .map(period -> {
                Period timeEntity = new Period();
                timeEntity.setFromDate(period.getFrom());
                timeEntity.setToDate(period.getTo());

                return timeEntity;
            })
            .sorted(Comparator.comparing(Period::getFromDate))
            .collect(Collectors.toList());
        timeRepository.saveAll(times);
        scheduler.start();

        return ResponseEntity.ok().build();
    }
}
