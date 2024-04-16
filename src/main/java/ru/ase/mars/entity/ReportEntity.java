package ru.ase.mars.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ase.mars.enums.Statuses;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "report")
public class ReportEntity {

    @Id
    private int id;
    private String title;
    private LocalDateTime lastUpdated;
    private String author;
    private Statuses state;
    private String file;
    private String comment;
}
