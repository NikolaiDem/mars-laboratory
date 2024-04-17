package ru.ase.mars.service;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ase.mars.enums.Statuses;
import ru.ase.mars.repository.ReportRepository;

@Slf4j
@Service
@AllArgsConstructor
public class CommunicationTask {

    private ReportRepository reportRepository;
    private BlockingQueue<Integer> reportQueue;

    public void run(LocalDateTime toDate) {
        LocalDateTime restTime = toDate.minusNanos(LocalDateTime.now().getNano());
        Integer nextId;
        try {
            while (restTime.isAfter(LocalDateTime.now())
                && (nextId = reportQueue.poll(restTime.getNano(), TimeUnit.NANOSECONDS)) != null) {
                reportRepository.findById(nextId)
                    .ifPresent(entity -> {
                        entity.setState(Statuses.SEND);
                        reportRepository.save(entity);
                        log.debug("Sent report with id {}", entity.getId());
                    });
                try {
                    Thread.sleep(60000);
                } catch (Exception e) {
                    log.error("Unable to sleep for 1 minute");
                }
                restTime = toDate.minusNanos(LocalDateTime.now().getNano());
            }

        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
    }
}
