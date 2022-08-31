package com.alten.booking.controllers;

import com.alten.booking.dtos.AvailabilityDTO;
import com.alten.booking.dtos.BookingDTO;
import com.alten.booking.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1/booking")
public class BookingController {

  private final BookingService bookingService;

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<BookingDTO> placeBooking(@RequestBody BookingDTO bookingInformation) {
    BookingDTO bookingResponse = bookingService.placeBooking(bookingInformation);
    return new ResponseEntity<>(bookingResponse, HttpStatus.CREATED);
  }

  @GetMapping("/availability")
  @ResponseStatus(HttpStatus.OK)
  public AvailabilityDTO getBookingAvailability(@RequestParam String startBookingDate, @RequestParam String length) {
    return bookingService.checkRoomAvailability(LocalDate.parse(startBookingDate),
        LocalDate.parse(startBookingDate).plus(Long.parseLong(length), ChronoUnit.DAYS), null);
  }

  @GetMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BookingDTO> getBookingById(@PathVariable String id) {
    return new ResponseEntity<>(bookingService.getBookingById(id), HttpStatus.OK);
  }

  @PutMapping("{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BookingDTO> updateBooking(@RequestBody BookingDTO bookingInformation,
      @PathVariable String id) {
    BookingDTO bookingDTO = bookingService.updateBooking(bookingInformation, id);
    return new ResponseEntity<>(bookingDTO, HttpStatus.OK);
  }

  @DeleteMapping("{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> deleteBooking(@PathVariable String id) {
    bookingService.deleteBooking(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
