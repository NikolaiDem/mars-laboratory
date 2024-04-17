package ru.ase.mars.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ase.mars.entity.Report;
import ru.ase.mars.enums.Statuses;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    List<Report> findByState(Statuses statuses);

}
