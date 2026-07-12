package service;

import java.util.List;
import java.util.Optional;

import enums.PaymentStatus;
import model.Booking;
import model.Payment;
import model.Seat;
import model.Show;
import model.User;
import strategy.PaymentStrategy;

public class SeatBookingService {
    private final SeatLockingService seatLockingService;

    public SeatBookingService() {
        this.seatLockingService = new SeatLockingService();
    }

    public SeatBookingService(SeatLockingService seatLockingService) {
        this.seatLockingService = seatLockingService;
    }

    public Optional<Booking> createBooking(User user, Show show, List<Seat> seats, PaymentStrategy paymentStrategy) {
        if (user == null || show == null || seats == null || seats.isEmpty() || paymentStrategy == null) {
            return Optional.empty();
        }

        // Wire up seat locking before payment
        boolean locked = seatLockingService.lockSeats(show, seats, user);
        if (!locked) {
            System.out.println("Unable to lock seats. One or more seats are unavailable.");
            return Optional.empty();
        }

        try {
            double amount = show.getTicketPrice() * seats.size();
            Payment payment = paymentStrategy.payAmount(amount);

            if (payment == null || payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
                seatLockingService.unlockSeats(show, seats, user);
                System.out.println("Payment failed. Seats have been released.");
                return Optional.empty();
            }

            seatLockingService.confirmSeats(show, seats, user);
            Booking booking = new Booking(user, show, seats, payment);
            booking.confirmBooking();
            return Optional.of(booking);
        } catch (RuntimeException ex) {
            seatLockingService.unlockSeats(show, seats, user);
            throw ex;
        }
    }

    public SeatLockingService getSeatLockingService() {
        return seatLockingService;
    }
}
