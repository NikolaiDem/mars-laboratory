package ru.ase.mars.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "period_time")
public class TimeEntity {

    @Id
    private int id;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
