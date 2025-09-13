package com.bms.controllers;

import com.bms.dto.ShowDTO;
import com.bms.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/shows")
@CrossOrigin
public class ShowController {

    @Autowired
    private ShowService showService;

    // Create a new show
    @PostMapping
    public ResponseEntity<ShowDTO> createShow(@RequestBody ShowDTO showDTO) {
        ShowDTO createdShow = showService.createShow(showDTO);
        return ResponseEntity.ok(createdShow);
    }

    // Get show by ID
    @GetMapping("/{id}")
    public ResponseEntity<ShowDTO> getShowById(@PathVariable Long id) {
        ShowDTO showDTO = showService.getShowById(id);
        return ResponseEntity.ok(showDTO);
    }

    // Get all shows
    @GetMapping
    public ResponseEntity<List<ShowDTO>> getAllShows() {
        List<ShowDTO> shows = showService.getAllShow();
        return ResponseEntity.ok(shows);
    }

    // Get all shows by movie ID
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowDTO>> getShowsByMovie(@PathVariable Long movieId) {
        List<ShowDTO> shows = showService.getAllShowsByMovie(movieId);
        return ResponseEntity.ok(shows);
    }

    // Get all shows by movie ID and city
    @GetMapping("/movie/{movieId}/city/{city}")
    public ResponseEntity<List<ShowDTO>> getShowsByMovieAndCity(@PathVariable Long movieId,
                                                                @PathVariable String city) {
        List<ShowDTO> shows = showService.getAllShowsByMovieAndCity(movieId, city);
        return ResponseEntity.ok(shows);
    }

    // Get all shows within a time range
    @GetMapping("/range")
    public ResponseEntity<List<ShowDTO>> getShowsWithinRange(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<ShowDTO> shows = showService.getAllShowWithinRanges(startTime, endTime);
        return ResponseEntity.ok(shows);
    }
}

