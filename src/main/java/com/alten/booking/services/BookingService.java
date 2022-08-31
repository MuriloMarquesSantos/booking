package com.alten.booking.services;

import com.alten.booking.dtos.AvailabilityDTO;
import com.alten.booking.dtos.BookingDTO;
import com.alten.booking.entities.Booking;
import com.alten.booking.exceptions.NotAvailableException;
import com.alten.booking.exceptions.ResourceNotFoundException;
import com.alten.booking.repository.BookingRepository;
import com.alten.booking.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

  private final BookingRepository bookingRepository;

  private static final String THIRTY_DAYS_EXCEPTION_MESSAGE = "Reservations can't be made less than one day prior to the start date or more than 30 days in advance";
  private static final String THREE_DAYS_LENGTH_EXCEPTION_MESSAGE = "The stay can’t be longer than 3 days";
  private static final String BOOKING_NOT_FOUND_MESSAGE = "No booking found with this id";
  private static final String BOOKING_NOT_AVAILABLE_MESSAGE = "Booking not available for this date";
  private static final String BOOKING_AVAILABLE_MESSAGE = "Booking available for this date";

  public BookingDTO placeBooking(BookingDTO createBookingDTO) {
    LocalDate startBookingDate = createBookingDTO.getStartBookingDate();
    LocalDate endBookingDate = startBookingDate.plus(createBookingDTO.getBookingLength(), ChronoUnit.DAYS);

    validateBookingDates(createBookingDTO);

    checkRoomAvailability(startBookingDate, endBookingDate, null);

    Booking savedBooking = bookingRepository.save(MapperUtils.dtoToEntity(createBookingDTO));

    return MapperUtils.entityToDTO(savedBooking);
  }

  public AvailabilityDTO checkRoomAvailability(LocalDate from, LocalDate to, String bookingId) {
    List<Booking> bookings = bookingRepository
        .findCustomDate(from.atStartOfDay(ZoneOffset.UTC), to.atStartOfDay(ZoneOffset.UTC));

    if (bookings.isEmpty() || bookings.get(0).getBookingId().equals(bookingId)) {
      return new AvailabilityDTO(BOOKING_AVAILABLE_MESSAGE);
    }

    throw new NotAvailableException(BOOKING_NOT_AVAILABLE_MESSAGE);
  }

  public BookingDTO getBookingById(String bookingId) {
    Booking booking = bookingRepository.findById(bookingId)
        .orElseThrow(() -> new ResourceNotFoundException(BOOKING_NOT_FOUND_MESSAGE));

    return MapperUtils.entityToDTO(booking);
  }

  public BookingDTO updateBooking(BookingDTO createBookingDTO, String bookingId) {
    Booking booking = bookingRepository.findById(bookingId)
        .orElseThrow(() -> new ResourceNotFoundException(BOOKING_NOT_FOUND_MESSAGE));

    validateBookingDates(createBookingDTO);

    checkRoomAvailability(createBookingDTO.getStartBookingDate(),
        createBookingDTO.getStartBookingDate().plus(createBookingDTO.getBookingLength(), ChronoUnit.DAYS),
        booking.getBookingId());

    Booking bookingToBeSaved = MapperUtils.dtoToEntity(createBookingDTO);
    bookingToBeSaved.setBookingId(bookingId);

    return MapperUtils.entityToDTO(bookingRepository.save(bookingToBeSaved));
  }

  public void deleteBooking(String id) {
    bookingRepository.deleteById(id);
  }

  private void validateBookingDates(BookingDTO createBookingDTO) {
    LocalDate thirtyDaysFromNow = LocalDate.now().plus(30, ChronoUnit.DAYS);
    LocalDate tomorrow = LocalDate.now().plus(1, ChronoUnit.DAYS);

    if (createBookingDTO.getStartBookingDate().isAfter(thirtyDaysFromNow) ||
    createBookingDTO.getStartBookingDate().isBefore(tomorrow)) {
      throw new IllegalArgumentException(THIRTY_DAYS_EXCEPTION_MESSAGE);
    }

    if (createBookingDTO.getBookingLength() > 3) {
      throw new IllegalArgumentException(THREE_DAYS_LENGTH_EXCEPTION_MESSAGE);
    }
  }
}
