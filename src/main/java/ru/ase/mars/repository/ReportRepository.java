package ru.ase.mars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ase.mars.entity.ReportEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {
}
