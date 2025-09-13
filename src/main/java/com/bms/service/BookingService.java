package com.bms.service;

import com.bms.dto.*;
import com.bms.exception.ResourceNotFoundException;
import com.bms.exception.SeatUnavailableException;
import com.bms.models.*;
import com.bms.repo.BookingRepository;
import com.bms.repo.ShowRepository;
import com.bms.repo.ShowSeatRepository;
import com.bms.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Transactional
    public BookingDTO createBooking(BookingRequestDTO bookingRequest) {
        User user = userRepository.findById(bookingRequest.getUserId()).
                orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Show show = showRepository.findById(bookingRequest.getShowId()).orElseThrow(
                () -> new ResourceNotFoundException("Show not found"));

        List<ShowSeat> selectedSeats = showSeatRepository.findAllById(bookingRequest.getSeatIds());
        // check if seats are available
        for (ShowSeat seat : selectedSeats) {
            if (!"AVAILABLE".equalsIgnoreCase(seat.getStatus())) {
                throw new SeatUnavailableException("Seat " + seat.getSeat().getSeatNumber() + " is not available");
            }
            seat.setStatus("LOCKED");
        }


        showSeatRepository.saveAll(selectedSeats);

        //sum of total seats price
        Double totalAmount = selectedSeats.stream()
                .mapToDouble(ShowSeat::getPrice)
                .sum();

        Payment payment = new Payment();
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(bookingRequest.getPaymentMethod());
        payment.setPaymentTime(LocalDateTime.now());
        payment.setStatus("SUCCESS");
        payment.setTransactionId(UUID.randomUUID().toString());

        //create booking
        Booking booking = new Booking();
        booking.setBookingNumber(UUID.randomUUID().toString());
        booking.setBookingTime(LocalDateTime.now());
        booking.setUser(user);
        booking.setShow(show);
        booking.setStatus("CONFIRMED");
        booking.setTotalAmount(totalAmount);
        booking.setShowSeats(selectedSeats);
        booking.setPayment(payment);

        Booking saveBooking = bookingRepository.save(booking);

        selectedSeats.forEach(seat ->
                {
                    seat.setStatus("BOOKED");
                    seat.setBooking(saveBooking);
                }
        );

        showSeatRepository.saveAll(selectedSeats);
        return mapToDTO(booking, selectedSeats);
    }

    // get bookings by id
    public BookingDTO getBookingById(Long bookingId){
        Booking booking= bookingRepository.findById(bookingId).orElseThrow(
                ()-> new ResourceNotFoundException("Booking not found")
        );
        List<ShowSeat> seats= showSeatRepository.findAll().stream().filter(
                seat-> seat.getBooking()!=null && seat.getBooking().getId().equals(bookingId)
        ).toList();

        return mapToDTO(booking,seats);
    }

    // get bookings by booking number

    public BookingDTO getBookingByNumber(String bookingNumber){
        Booking booking= bookingRepository.findByBookingNumber(bookingNumber).orElseThrow(
                ()-> new ResourceNotFoundException("Booking not found")
        );

        List<ShowSeat> seats= showSeatRepository.findAll().stream().filter(
                seat-> seat.getBooking()!=null && seat.getBooking().getBookingNumber().equals(bookingNumber)
        ).toList();
        return mapToDTO(booking,seats);
    }

    // get booking by user id

    public List<BookingDTO> getBookingByUserId(Long userId){
        List<Booking>  bookings= bookingRepository.findByUserId(userId);
        return bookings.stream()
                .map(booking -> {
                    List<ShowSeat> seats= showSeatRepository.findAll().stream().filter(
                            seat-> seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId())
                    ).toList();
                    return mapToDTO(booking,seats);
                }).collect(Collectors.toList());
    }

    @Transactional
    public BookingDTO cancelBooking(Long bookingId){
        Booking booking= bookingRepository.findById(bookingId).orElseThrow(
                ()-> new ResourceNotFoundException("Booking not found")
        );
        List<ShowSeat> seats= showSeatRepository.findAll().stream().filter(
                seat-> seat.getBooking()!=null && seat.getBooking().getId().equals(bookingId)
        ).collect(Collectors.toList());

        seats.forEach(seat ->
        {
            seat.setStatus("AVAILABLE");
            seat.setBooking(null);
        });
        showSeatRepository.saveAll(seats);

        booking.setStatus("CANCELLED");
        if(booking.getPayment()!=null){
            booking.setStatus("REFUNDED");
        }
        Booking updatedBooking= bookingRepository.save(booking);
        return mapToDTO(booking,seats);
    }

    private BookingDTO mapToDTO(Booking booking, List<ShowSeat> bookedSeats){
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setBookingNumber(booking.getBookingNumber());
        dto.setBookingTime(booking.getBookingTime());
        dto.setStatus(booking.getStatus());
        dto.setTotalAmount(booking.getTotalAmount());

        // --- User DTO ---
        User userEntity = booking.getUser();
        UserDTO user = new UserDTO();
        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        user.setEmail(userEntity.getEmail());
        user.setPhone(userEntity.getPhoneNumber());
        dto.setUser(user);

        // --- Show DTO ---
        Show showEntity = booking.getShow();
        ShowDTO show = new ShowDTO();
        show.setId(showEntity.getId());
        show.setStartTime(showEntity.getStartTime());
        show.setEndTime(showEntity.getEndTime());

        // Movie DTO
        Movie movieEntity = showEntity.getMovie();
        MovieDTO movie = new MovieDTO();
        movie.setId(movieEntity.getId());
        movie.setTitle(movieEntity.getTitle());
        movie.setDescription(movieEntity.getDescription());
        movie.setLanguage(movieEntity.getLanguage());
        movie.setGenre(movieEntity.getGenre());
        movie.setDurationMins(movieEntity.getDurationMins());
        movie.setReleaseDate(movieEntity.getReleaseDate());
        movie.setPosterUrl(movieEntity.getPosterUrl());
        show.setMovie(movie);

        // Screen DTO
        Screen screenEntity = showEntity.getScreen();
        ScreenDTO screen = new ScreenDTO();
        screen.setId(screenEntity.getId());
        screen.setName(screenEntity.getName());
        screen.setTotalSeats(screenEntity.getTotalSeats());

        Theater theaterEntity = screenEntity.getTheater();
        TheaterDTO theater = new TheaterDTO();
        theater.setId(theaterEntity.getId());
        theater.setName(theaterEntity.getName());
        theater.setAddress(theaterEntity.getAddress());
        theater.setCity(theaterEntity.getCity());
        theater.setTotalScreens(theaterEntity.getTotalScreens());
        screen.setTheater(theater);

        show.setScreen(screen);
        dto.setShow(show);

        // --- Available Seats ---
        List<ShowSeat> availableSeatsList = showSeatRepository.findAll().stream()
                .filter(seat -> seat.getShow().getId().equals(showEntity.getId()))
                .filter(seat -> "AVAILABLE".equalsIgnoreCase(seat.getStatus()))
                .toList();

        List<ShowSeatDTO> availableSeatsDTO = availableSeatsList.stream().map(seat -> {
            ShowSeatDTO seatDTO = new ShowSeatDTO();
            seatDTO.setId(seat.getId());
            seatDTO.setStatus(seat.getStatus());
            seatDTO.setPrice(seat.getPrice());

            Seat baseSeat = seat.getSeat();
            if (baseSeat != null) {
                SeatDTO baseSeatDto = new SeatDTO();
                baseSeatDto.setId(baseSeat.getId());
                baseSeatDto.setSeatNumber(baseSeat.getSeatNumber());
                baseSeatDto.setSeatType(baseSeat.getSeatType());
                baseSeatDto.setBasePrice(baseSeat.getBasePrice());
                seatDTO.setSeat(baseSeatDto);
            }
            return seatDTO;
        }).toList();

        show.setAvailableSeats(availableSeatsDTO);

        // --- Seats booked in this booking ---
        List<ShowSeatDTO> bookedSeatsDTO = bookedSeats.stream().map(seat -> {
            ShowSeatDTO seatDTO = new ShowSeatDTO();
            seatDTO.setId(seat.getId());
            seatDTO.setStatus(seat.getStatus());
            seatDTO.setPrice(seat.getPrice());

            Seat baseSeat = seat.getSeat();
            if (baseSeat != null) {
                SeatDTO baseSeatDto = new SeatDTO();
                baseSeatDto.setId(baseSeat.getId());
                baseSeatDto.setSeatNumber(baseSeat.getSeatNumber());
                baseSeatDto.setSeatType(baseSeat.getSeatType());
                baseSeatDto.setBasePrice(baseSeat.getBasePrice());
                seatDTO.setSeat(baseSeatDto);
            }
            return seatDTO;
        }).toList();

        dto.setSeats(bookedSeatsDTO);

        // --- Payment DTO ---
        if (booking.getPayment() != null) {
            Payment p = booking.getPayment();
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setId(p.getId());
            paymentDTO.setTransactionId(p.getTransactionId());
            paymentDTO.setAmount(p.getAmount());
            paymentDTO.setPaymentTime(p.getPaymentTime());
            paymentDTO.setPaymentMethod(p.getPaymentMethod());
            paymentDTO.setStatus(p.getStatus());
            dto.setPaymentDTO(paymentDTO);
        } else {
            dto.setPaymentDTO(null);
        }

        return dto;
    }

}
