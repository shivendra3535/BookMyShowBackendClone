package com.bms.controllers;

import com.bms.dto.TheaterDTO;
import com.bms.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
@CrossOrigin
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    // Create a new theater
    @PostMapping
    public ResponseEntity<TheaterDTO> createTheater(@RequestBody TheaterDTO theaterDTO) {
        TheaterDTO createdTheater = theaterService.createTheater(theaterDTO);
        return ResponseEntity.ok(createdTheater);
    }

    // Get theater by ID
    @GetMapping("/{id}")
    public ResponseEntity<TheaterDTO> getTheaterById(@PathVariable Long id) {
        TheaterDTO theaterDTO = theaterService.getTheaterById(id);
        return ResponseEntity.ok(theaterDTO);
    }

    // Get all theaters
    @GetMapping
    public ResponseEntity<List<TheaterDTO>> getAllTheaters() {
        List<TheaterDTO> theaters = theaterService.getAllTheaters();
        return ResponseEntity.ok(theaters);
    }

    // Get theaters by city
    @GetMapping("/city/{city}")
    public ResponseEntity<List<TheaterDTO>> getTheatersByCity(@PathVariable String city) {
        List<TheaterDTO> theaters = theaterService.getByCity(city);
        return ResponseEntity.ok(theaters);
    }
}
