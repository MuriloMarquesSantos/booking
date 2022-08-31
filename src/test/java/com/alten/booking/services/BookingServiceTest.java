package com.alten.booking.services;

import com.alten.booking.dtos.AvailabilityDTO;
import com.alten.booking.dtos.BookingDTO;
import com.alten.booking.entities.Booking;
import com.alten.booking.exceptions.NotAvailableException;
import com.alten.booking.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

  private static final String BOOKING_AVAILABLE_MESSAGE = "Booking available for this date";
  private static final String BOOKING_NOT_AVAILABLE_MESSAGE = "Booking not available for this date";

  @InjectMocks
  private BookingService bookingService;

  @Mock
  private BookingRepository bookingRepository;


  @Test
  void successBookingGivenStartsTomorrowAndLength3() {
    when(bookingRepository.findByStartBookingDateBetween(any(), any()))
        .thenReturn(Collections.emptyList());

    LocalDateTime now = LocalDateTime.now().plus(1, ChronoUnit.DAYS);

    Booking booking = Booking
        .builder()
        .bookingId("123")
        .bookingLength("3")
        .startBookingDate(now)
        .endBookingDate(now.plus(3, ChronoUnit.DAYS))
        .build();

    when(bookingRepository.save(any())).thenReturn(booking);

    BookingDTO bookingDTO = BookingDTO
        .builder()
        .bookingLength(3L)
        .startBookingDate(now.toLocalDate())
        .build();

    BookingDTO bookingDTOResponse = bookingService.placeBooking(bookingDTO);

    assertNotNull(bookingDTOResponse);
    assertEquals(3L, bookingDTOResponse.getBookingLength());
  }

  @Test
  void errorBookingGivenStartsTodayAndLength3() {
    LocalDateTime now = LocalDateTime.now();

    BookingDTO bookingDTO = BookingDTO
        .builder()
        .bookingLength(3L)
        .startBookingDate(now.toLocalDate())
        .build();

    assertThrows(IllegalArgumentException.class, () -> bookingService.placeBooking(bookingDTO));
  }

  @Test
  void errorBookingGivenStartsTomorrowAndLengthMoreThan3() {
    LocalDateTime now = LocalDateTime.now().plus(1, ChronoUnit.DAYS);

    BookingDTO bookingDTO = BookingDTO
        .builder()
        .bookingLength(4L)
        .startBookingDate(now.toLocalDate())
        .build();

    assertThrows(IllegalArgumentException.class, () -> bookingService.placeBooking(bookingDTO));
  }

  @Test
  void errorBookingGivenBookingAlreadyExistsOnThatDate() {
    LocalDateTime now = LocalDateTime.now().plus(1, ChronoUnit.DAYS);

    Booking booking = Booking
        .builder()
        .bookingId("123")
        .bookingLength("3")
        .startBookingDate(now)
        .endBookingDate(now.plus(3, ChronoUnit.DAYS))
        .build();

    when(bookingRepository.findByStartBookingDateBetween(any(), any()))
        .thenReturn(Collections.singletonList(booking));

    BookingDTO bookingDTO = BookingDTO
        .builder()
        .bookingLength(3L)
        .startBookingDate(now.toLocalDate())
        .build();

    assertThrows(NotAvailableException.class, () -> bookingService.placeBooking(bookingDTO));
  }

  @Test
  void successCheckAvailabilityWhenThereIsNoBooking() {
    when(bookingRepository.findByStartBookingDateBetween(any(), any()))
        .thenReturn(Collections.emptyList());

    LocalDate now = LocalDate.now().plus(1, ChronoUnit.DAYS);

    AvailabilityDTO availabilityResponse = bookingService.checkRoomAvailability(now, now.plus(1, ChronoUnit.DAYS), "123");

    assertNotNull(availabilityResponse);
    assertEquals(BOOKING_AVAILABLE_MESSAGE, availabilityResponse.getMessage());
  }

  @Test
  void failureCheckAvailabilityWhenThereIsABooking() {
    Booking booking = Booking
        .builder()
        .bookingId("123")
        .bookingLength("3")
        .startBookingDate(LocalDateTime.now())
        .endBookingDate(LocalDateTime.now().plus(3, ChronoUnit.DAYS))
        .build();

    when(bookingRepository.findByStartBookingDateBetween(any(), any()))
        .thenReturn(Collections.singletonList(booking));

    LocalDate now = LocalDate.now().plus(1, ChronoUnit.DAYS);

    NotAvailableException exception = assertThrows(NotAvailableException.class, () -> {
      bookingService.checkRoomAvailability(now, now.plus(1, ChronoUnit.DAYS), "578");
    });

    assertEquals(BOOKING_NOT_AVAILABLE_MESSAGE, exception.getMessage());
  }
}
