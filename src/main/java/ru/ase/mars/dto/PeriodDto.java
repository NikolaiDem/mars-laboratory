package ru.ase.mars.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ase.mars.utils.CustomLocalDateTimeDeserializer;

@Data
@NoArgsConstructor
public class PeriodDto {

    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime from;
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime to;
}
