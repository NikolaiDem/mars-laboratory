package ru.ase.mars.service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ase.mars.entity.Period;
import ru.ase.mars.entity.Report;
import ru.ase.mars.enums.Statuses;
import ru.ase.mars.repository.ReportRepository;
import ru.ase.mars.repository.TimeRepository;

@Service
@AllArgsConstructor
public class Scheduler {

    private TimeRepository timeRepository;
    private ReportRepository reportRepository;
    private BlockingQueue<Integer> reportQueue;
    private CommunicationTask communicationTask;


    @PostConstruct
    private void initReports() {
        List<Report> reportEntities = reportRepository.findByState(Statuses.APPROVE);
        List<Integer> reportIds = reportEntities.stream()
            .map(Report::getId)
            .sorted()
            .toList();
        reportQueue.addAll(reportIds);
        start();
    }

    public void start() {
        List<Period> periods = timeRepository.findByExecutedIsNullOrderByFromDate();
        for (var period : periods) {
            LocalDateTime fromDate = period.getFromDate();
            LocalDateTime now = LocalDateTime.now();
            int waitFor = fromDate.getNano() - now.getNano();
            try {
                if (waitFor > 0) {
                    Thread.sleep(waitFor / 1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            communicationTask.run(period.getToDate());
            period.setExecuted(true);
            timeRepository.save(period);
        }
    }
}
