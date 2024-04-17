package ru.ase.mars.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import ru.ase.mars.dto.ReportResponse;
import ru.ase.mars.entity.Report;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ReportMapper {

    ReportResponse toReportResponse(Report report);

}
