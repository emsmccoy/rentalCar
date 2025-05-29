package com.example.rentalCar.service;

import com.example.rentalCar.model.Booking;
import java.util.List;

public interface BookingService {
    void saveBooking(Booking booking);
    Booking getBookingById(String userId, String operation);
    List<Booking> getBookingsByUserId(String userId);
    void deleteBooking(String userId, String operation);
}
