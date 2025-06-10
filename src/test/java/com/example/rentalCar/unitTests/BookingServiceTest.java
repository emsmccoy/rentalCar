package com.example.rentalCar.unitTests;

import com.example.rentalCar.model.Booking;
import com.example.rentalCar.repository.BookingRepository;
import com.example.rentalCar.service.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveBooking() {
        Booking booking = new Booking();
        booking.setUserId("user123");
        booking.setOperation("booking#20250601#001");

        bookingService.saveBooking(booking);

        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void getBookingById() {
        String userId = "user123";
        String operation = "booking#20250601#001";
        Booking expectedBooking = new Booking();
        when(bookingRepository.findById(userId, operation)).thenReturn(expectedBooking);

        Booking result = bookingService.getBookingById(userId, operation);

        assertEquals(expectedBooking, result);
        verify(bookingRepository, times(1)).findById(userId, operation);
    }

    @Test
    void getBookingsByUserId() {
        String userId = "user123";
        List<Booking> expectedBookings = Arrays.asList(new Booking(), new Booking());
        when(bookingRepository.findByUserId(userId)).thenReturn(expectedBookings);

        List<Booking> result = bookingService.getBookingsByUserId(userId);

        assertEquals(2, result.size());
        verify(bookingRepository, times(1)).findByUserId(userId);
    }

    @Test
    void deleteBooking() {
        String userId = "user123";
        String operation = "booking#20250601#001";

        bookingService.deleteBooking(userId, operation);

        verify(bookingRepository, times(1)).deleteById(userId, operation);
    }
}

