package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import enums.SeatStatus;

public class Booking {
    private String id;
    private Show show;
    private List<Seat> seats;
    private Payment payment;
    private User user;

    public Booking() {
        this.seats = new ArrayList<>();
    }

    public Booking(User user, Show show, List<Seat> seats, Payment payment) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.show = show;
        this.seats = new ArrayList<>(seats);
        this.payment = payment;
    }

    public void confirmBooking() {
        for (Seat seat : seats) {
            seat.setSeatStatus(SeatStatus.BOOKED);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
