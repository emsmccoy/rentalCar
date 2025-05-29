package com.example.rentalCar.service;

import com.example.rentalCar.model.Booking;
import com.example.rentalCar.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(String userId, String operation) {
        return bookingRepository.findById(userId, operation);
    }

    @Override
    public List<Booking> getBookingsByUserId(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public void deleteBooking(String userId, String operation) {
        bookingRepository.deleteById(userId, operation);
    }
}

