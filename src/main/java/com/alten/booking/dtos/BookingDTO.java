package com.alten.booking.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {
  private String bookingId;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startBookingDate;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endBookingDate;
  private Long bookingLength;
}
