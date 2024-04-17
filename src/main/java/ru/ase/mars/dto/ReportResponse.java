package ru.ase.mars.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ReportResponse {

    private int id;
    private String title;
    private LocalDateTime lastUpdated;
    private UserResponse author;
    private String state;
    private String file;
    private String comment;
}
