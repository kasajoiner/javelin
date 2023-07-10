package javelin.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ClientImportRequest(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
}
