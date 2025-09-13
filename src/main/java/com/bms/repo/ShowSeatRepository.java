package com.bms.repo;

import com.bms.models.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {

    List<ShowSeat> findByShowIdIn(List<Long> showIds);

    List<ShowSeat> findByShowIdAndStatus(Long showId, String status);


    List<ShowSeat> findByShow_IdAndSeat_SeatType(Long showId, String seatType);


    List<ShowSeat> findByShowId(Long showId);
}
