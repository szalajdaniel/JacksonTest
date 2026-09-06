package org.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record RobotReport(String serialNumber, @JsonFormat(pattern = "dd-MM-yyyy") LocalDate commissionDate,
                          int batettery) {
}
