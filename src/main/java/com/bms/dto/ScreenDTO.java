package com.bms.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenDTO {
    private Long id;
    private String name;
    private Integer totalSeats;
    private TheaterDTO theater;
}
