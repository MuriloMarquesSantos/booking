package com.alten.booking.repository;

import com.alten.booking.entities.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
  List<Booking> findByStartBookingDateBetween(LocalDateTime from, LocalDateTime to);
}
