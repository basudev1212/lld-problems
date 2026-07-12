package model;

import java.util.ArrayList;
import java.util.List;

public class Screen {
    private String id;
    private List<Seat> seats;

    public Screen() {
        this.seats = new ArrayList<>();
    }

    public Screen(String id, List<Seat> seats) {
        this.id = id;
        this.seats = seats != null ? seats : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }
}
