package ru.ase.mars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ase.mars.entity.ReportEntity;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {

    List<ReportEntity> findByAuthor(String author);
}
