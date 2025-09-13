package com.bms.repo;

import com.bms.models.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {

    List<Show> findByScreenId(Long screenId);

    List<Show> findByMovieId(Long movieId);

    List<Show> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<Show> findByMovie_IdAndScreen_Theater_City(Long movieId, String city);

}
