package com.bms.service;

import com.bms.dto.*;
import com.bms.exception.ResourceNotFoundException;
import com.bms.models.*;
import com.bms.repo.MovieRepository;
import com.bms.repo.ScreenRepository;
import com.bms.repo.ShowRepository;
import com.bms.repo.ShowSeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Transactional
    public ShowDTO createShow(ShowDTO showDTO){
        Show show = new Show();
        Movie movie = movieRepository.findById(showDTO.getMovie().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        Screen screen = screenRepository.findById(showDTO.getScreen().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen not found"));

        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(showDTO.getStartTime());
        show.setEndTime(showDTO.getEndTime());

        Show savedShow = showRepository.save(show);

        List<ShowSeat> availableSeats = showSeatRepository.findAll().stream()
                .filter(seat -> seat.getShow().getId().equals(savedShow.getId()))
                .filter(seat -> "AVAILABLE".equalsIgnoreCase(seat.getStatus()))
                .collect(Collectors.toList());

        return mapToDTO(savedShow, availableSeats);
    }

    @Transactional
    public ShowDTO getShowById(Long id){
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show Not Found with ID= " + id));

        List<ShowSeat> availableSeats = showSeatRepository.findAll().stream()
                .filter(seat -> seat.getShow().getId().equals(id))
                .filter(seat -> "AVAILABLE".equalsIgnoreCase(seat.getStatus()))
                .collect(Collectors.toList());

        return mapToDTO(show, availableSeats);
    }

    @Transactional
    public List<ShowDTO> getAllShow(){
        List<Show> allShows = showRepository.findAll();
        return allShows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findAll().stream()
                            .filter(seat -> seat.getShow().getId().equals(show.getId()))
                            .filter(seat -> "AVAILABLE".equalsIgnoreCase(seat.getStatus()))
                            .collect(Collectors.toList());
                    return mapToDTO(show, availableSeats);
                }).collect(Collectors.toList());
    }

    @Transactional
    public List<ShowDTO> getAllShowsByMovie(Long movieId){
        List<Show> allShows = showRepository.findByMovieId(movieId);
        return allShows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findAll().stream()
                            .filter(seat -> seat.getShow().getId().equals(show.getId()))
                            .filter(seat -> "AVAILABLE".equalsIgnoreCase(seat.getStatus()))
                            .collect(Collectors.toList());
                    return mapToDTO(show, availableSeats);
                }).collect(Collectors.toList());
    }

    @Transactional
    public List<ShowDTO> getAllShowsByMovieAndCity(Long movieId, String city){
        List<Show> allShows = showRepository.findByMovie_IdAndScreen_Theater_City(movieId, city);
        return allShows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findAll().stream()
                            .filter(seat -> seat.getShow().getId().equals(show.getId()))
                            .filter(seat -> "AVAILABLE".equalsIgnoreCase(seat.getStatus()))
                            .collect(Collectors.toList());
                    return mapToDTO(show, availableSeats);
                }).collect(Collectors.toList());
    }

    @Transactional
    public List<ShowDTO> getAllShowWithinRanges(LocalDateTime startTime, LocalDateTime endTime){
        List<Show> allShows = showRepository.findByStartTimeBetween(startTime, endTime);
        return allShows.stream()
                .map(show -> {
                    List<ShowSeat> availableSeats = showSeatRepository.findAll().stream()
                            .filter(seat -> seat.getShow().getId().equals(show.getId()))
                            .filter(seat -> "AVAILABLE".equalsIgnoreCase(seat.getStatus()))
                            .collect(Collectors.toList());
                    return mapToDTO(show, availableSeats);
                }).collect(Collectors.toList());
    }

    // --- Mapping method ---
    public ShowDTO mapToDTO(Show show, List<ShowSeat> availableSeats){
        ShowDTO showDTO = new ShowDTO();
        showDTO.setId(show.getId());
        showDTO.setStartTime(show.getStartTime());
        showDTO.setEndTime(show.getEndTime());

        // Movie DTO
        Movie movie = show.getMovie();
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setDescription(movie.getDescription());
        movieDTO.setReleaseDate(movie.getReleaseDate());
        movieDTO.setLanguage(movie.getLanguage());
        movieDTO.setGenre(movie.getGenre());
        movieDTO.setPosterUrl(movie.getPosterUrl());
        movieDTO.setDurationMins(movie.getDurationMins());
        showDTO.setMovie(movieDTO);

        // Screen DTO
        Screen screen = show.getScreen();
        ScreenDTO screenDTO = new ScreenDTO();
        screenDTO.setId(screen.getId());
        screenDTO.setName(screen.getName());
        screenDTO.setTotalSeats(screen.getTotalSeats());

        Theater theater = screen.getTheater();
        TheaterDTO theaterDTO = new TheaterDTO();
        theaterDTO.setId(theater.getId());
        theaterDTO.setName(theater.getName());
        theaterDTO.setCity(theater.getCity());
        theaterDTO.setAddress(theater.getAddress());
        theaterDTO.setTotalScreens(theater.getTotalScreens());
        screenDTO.setTheater(theaterDTO);

        showDTO.setScreen(screenDTO);

        // Available seats DTO
        List<ShowSeatDTO> availableSeatsDTO = availableSeats.stream()
                .map(seat -> {
                    ShowSeatDTO seatDTO = new ShowSeatDTO();
                    seatDTO.setId(seat.getId());
                    seatDTO.setStatus(seat.getStatus());
                    seatDTO.setPrice(seat.getPrice());

                    Seat baseSeat = seat.getSeat();
                    if(baseSeat != null){
                        SeatDTO baseSeatDTO = new SeatDTO();
                        baseSeatDTO.setId(baseSeat.getId());
                        baseSeatDTO.setSeatNumber(baseSeat.getSeatNumber());
                        baseSeatDTO.setSeatType(baseSeat.getSeatType());
                        baseSeatDTO.setBasePrice(baseSeat.getBasePrice());
                        seatDTO.setSeat(baseSeatDTO);
                    }

                    return seatDTO;
                }).collect(Collectors.toList());

        showDTO.setAvailableSeats(availableSeatsDTO);

        return showDTO;
    }
}
