package com.bms.controllers;


import com.bms.dto.BookingDTO;
import com.bms.dto.BookingRequestDTO;
import com.bms.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDTO> createNewBooking(@Valid @RequestBody BookingRequestDTO bookingRequest){
        return new ResponseEntity<>(bookingService.createBooking(bookingRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> findBookingDoneById(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/BookingNumber/{bookingNumber}")
    public ResponseEntity<BookingDTO> findBookingDoneById(@PathVariable String bookingNumber){
        return ResponseEntity.ok(bookingService.getBookingByNumber(bookingNumber));
    }

    @GetMapping("/DeleteBooking/{bookingId}")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable Long bookingId){
        return  ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }

}
