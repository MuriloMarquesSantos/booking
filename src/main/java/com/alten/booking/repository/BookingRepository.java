package com.alten.booking.repository;

import com.alten.booking.entities.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
  @Query("{$or: [{'startBookingDate': {$gte: ?0,$lte: ?1}},{'endBookingDate': {$gte: ?0,$lte: ?1}}]}")
  List<Booking> findCustomDate(ZonedDateTime from, ZonedDateTime to);
}
