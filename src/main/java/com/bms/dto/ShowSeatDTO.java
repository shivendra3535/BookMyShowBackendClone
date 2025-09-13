package com.bms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowSeatDTO {
    private Long id;
    private SeatDTO seat;
    private String status;
    private Double price;
}
