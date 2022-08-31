package com.alten.booking.utils;

import com.alten.booking.dtos.BookingDTO;
import com.alten.booking.entities.Booking;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class MapperUtils {

  public static Booking dtoToEntity(BookingDTO createBookingDTO) {
    Long bookingLength = createBookingDTO.getBookingLength();
    return Booking
        .builder()
        .startBookingDate(LocalDateTime.of(createBookingDTO.getStartBookingDate(), LocalTime.MIDNIGHT).atZone(
                ZoneOffset.UTC))
        .endBookingDate(LocalDateTime.of(createBookingDTO.getStartBookingDate().plus(bookingLength, ChronoUnit.DAYS), LocalTime.MIDNIGHT)
            .atZone(ZoneOffset.UTC))
        .bookingLength(bookingLength.toString())
        .build();
  }

  public static BookingDTO entityToDTO(Booking booking) {
    return BookingDTO
        .builder()
        .bookingId(booking.getBookingId())
        .startBookingDate((booking.getStartBookingDate()).toLocalDate())
        .endBookingDate((booking.getEndBookingDate()).toLocalDate())
        .bookingLength(Long.valueOf(booking.getBookingLength()))
        .build();
  }
}
