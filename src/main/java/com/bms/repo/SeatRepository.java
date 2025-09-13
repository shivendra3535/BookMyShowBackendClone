package com.bms.repo;

import com.bms.models.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,Long> {

    List<Seat> findByScreenId(Long screenId);


    List<Seat> findBySeatNumberAndScreenId(String seatNumber, Long screenId);
    List<Seat> findBySeatNumberInAndScreenId(List<String> seatNumbers, Long screenId);

    List<Seat> findBySeatType(String seatType);


}
