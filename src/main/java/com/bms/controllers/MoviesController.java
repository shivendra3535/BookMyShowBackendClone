package com.bms.controllers;


import com.bms.dto.BookingDTO;
import com.bms.dto.BookingRequestDTO;
import com.bms.dto.MovieDTO;
import com.bms.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin
public class MoviesController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieDTO> createNewMovie(@Valid @RequestBody MovieDTO movieDTO){
        return new ResponseEntity<>(movieService.createMovie(movieDTO), HttpStatus.CREATED);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<MovieDTO> findMovieById(@PathVariable Long id){
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<MovieDTO> >findMovieByName(@PathVariable String name){
        return ResponseEntity.ok(movieService.getMoviesByTitle(name));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieDTO> >findMovieGenre(@PathVariable String genre){
        return ResponseEntity.ok(movieService.getMoviesByGenre(genre));
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<List<MovieDTO>> findMovieLanguage(@PathVariable String language){
        return ResponseEntity.ok(movieService.getMoviesByLanguage(language));
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> findAllMovies(){
        return ResponseEntity.ok(movieService.getAllMovies());
    }
}
