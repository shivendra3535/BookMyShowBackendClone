package com.bms.service;

import com.bms.dto.MovieDTO;
import com.bms.exception.ResourceNotFoundException;
import com.bms.models.Movie;
import com.bms.repo.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    // Buisness logic related to movie entity

     @Autowired
     private MovieRepository movieRepository;

     public MovieDTO createMovie(MovieDTO movieDTO){
        Movie movie= mapToEntity(movieDTO);
        Movie newMovie= movieRepository.save(movie);
        return mapToDTO(newMovie);
     }

     public MovieDTO getMovieById(Long id){
         Movie movie= movieRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Movie not found with id: "+id));
         return mapToDTO(movie);
     }

     public List<MovieDTO> getAllMovies(){
         List<Movie> movies= movieRepository.findAll();
         return movies.stream().map(movie -> mapToDTO(movie)).toList();
     }

     public List<MovieDTO> getMoviesByTitle(String title){
         List<Movie> movies= movieRepository.findByTitleContainingIgnoreCase(title);
         return movies.stream().map(this::mapToDTO).collect(Collectors.toList());
     }

     public List<MovieDTO> getMoviesByGenre(String genre){
         List<Movie> movies= movieRepository.findByGenre(genre);
         return movies.stream().map(this::mapToDTO).toList();
     }

     public List<MovieDTO> getMoviesByLanguage(String language){
         List<Movie> movies= movieRepository.findByLanguage(language).stream().toList();
         return movies.stream().map(this::mapToDTO).toList();
     }

     public MovieDTO updateMovie(Long id, MovieDTO movieDTO){
         Movie movie= movieRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Movie not found with id: "+id));
         movie.setTitle(movieDTO.getTitle());
         movie.setDescription(movieDTO.getDescription());
         movie.setLanguage(movieDTO.getLanguage());
         movie.setGenre(movieDTO.getGenre());
         movie.setDurationMins(movieDTO.getDurationMins());
         movie.setReleaseDate(movieDTO.getReleaseDate());
         movie.setPosterUrl(movieDTO.getPosterUrl());
         Movie updatedMovie= movieRepository.save(movie);
         return mapToDTO(updatedMovie);
     }

     public void deletMovie(Long id){
         Movie movie= movieRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Movie not found with id: "+id));
         movieRepository.delete(movie);
     }

     public Movie mapToEntity(MovieDTO movieDTO){
         Movie movie= new Movie();
         movie.setTitle(movieDTO.getTitle());
         movie.setDescription(movieDTO.getDescription());
         movie.setLanguage(movieDTO.getLanguage());
         movie.setGenre(movieDTO.getGenre());
         movie.setDurationMins(movieDTO.getDurationMins());
         movie.setReleaseDate(movieDTO.getReleaseDate());
         movie.setPosterUrl(movieDTO.getPosterUrl());
         return movie;
     }

     public MovieDTO mapToDTO(Movie movie){
         MovieDTO movieDTO= new MovieDTO();
         movieDTO.setId(movie.getId());
         movieDTO.setTitle(movie.getTitle());
         movieDTO.setDescription(movie.getDescription());
         movieDTO.setLanguage(movie.getLanguage());
         movieDTO.setGenre(movie.getGenre());
         movieDTO.setReleaseDate(movie.getReleaseDate());
         movieDTO.setDurationMins(movie.getDurationMins());
         movieDTO.setPosterUrl(movie.getPosterUrl());
         return movieDTO;
     }
}
