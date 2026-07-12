package service;

import enums.SeatStatus;
import enums.SeatType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import model.Booking;
import model.City;
import model.Movie;
import model.Screen;
import model.Seat;
import model.Show;
import model.Theatre;
import model.User;
import strategy.PaymentStrategy;

public class BookingService {
    private static BookingService instance;

    private final Map<String, City> cityMap;
    private final Map<String, Theatre> theatreMap;
    private final Map<String, Movie> movieMap;
    private final Map<String, Show> showMap;
    private final Map<String, User> userMap;

    private final SeatBookingService seatBookingService;

    public BookingService() {
        this.cityMap = new ConcurrentHashMap<>();
        this.theatreMap = new ConcurrentHashMap<>();
        this.movieMap = new ConcurrentHashMap<>();
        this.showMap = new ConcurrentHashMap<>();
        this.userMap = new ConcurrentHashMap<>();
        this.seatBookingService = new SeatBookingService();
    }

    public static BookingService getInstance() {
        if (instance == null) {
            instance = new BookingService();
        }
        return instance;
    }

    public void addCity(String id, String name) {
        cityMap.put(id, new City(id, name));
    }

    public void addMovie(String id, String movieName, Integer durationInMinutes) {
        movieMap.put(id, new Movie(id, movieName, durationInMinutes));
    }

    public void addUser(String id, String name, String email) {
        userMap.put(id, new User(id, name, email));
    }

    public void addTheatre(String id, String name, String cityId) {
        City city = cityMap.get(cityId);
        if (city == null) {
            throw new IllegalArgumentException("City not found: " + cityId);
        }
        theatreMap.put(id, new Theatre(id, name, city));
    }

    public Screen createScreen(String screenId, int seatCount) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= seatCount; i++) {
            SeatType seatType = i <= seatCount / 2 ? SeatType.NORMAL : SeatType.PREMIUM;
            seats.add(new Seat("seat-" + screenId + "-" + i, String.valueOf(i), seatType));
        }
        return new Screen(screenId, seats);
    }

    public void addShow(String showId, String movieId, String theatreId, Screen screen, String startTime, Double ticketPrice) {
        Movie movie = movieMap.get(movieId);
        Theatre theatre = theatreMap.get(theatreId);
        if (movie == null) {
            throw new IllegalArgumentException("Movie not found: " + movieId);
        }
        if (theatre == null) {
            throw new IllegalArgumentException("Theatre not found: " + theatreId);
        }

        Show show = new Show(showId, movie, screen, startTime, theatre, ticketPrice);
        showMap.put(showId, show);
        theatre.getShows().add(show);
    }

    public List<Show> findShows(String cityId, String movieId) {
        List<Show> matchingShows = new ArrayList<>();
        for (Show show : showMap.values()) {
            if(show.getMovie().getId().equals(movieId) && show.getTheatre().getCity().getId().equals(cityId))
                    matchingShows.add(show);
        }
        return matchingShows;
    }

    public List<Seat> getAvailableSeats(String showId) {
        Show show = showMap.get(showId);
        if (show == null || show.getScreen() == null) {
            return List.of();
        }

        List<Seat> availableSeats = new ArrayList<>();
        SeatLockingService lockingService = seatBookingService.getSeatLockingService();
        for (Seat seat : show.getScreen().getSeats()) {
            if (lockingService.isSeatAvailableForShow(showId, seat.getId())
                    && seat.getSeatStatus() == SeatStatus.AVAILABLE) {
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }

    public Optional<Booking> bookTickets(String userId, String showId, List<Seat> desiredSeats, PaymentStrategy paymentStrategy) {
        User user = userMap.get(userId);
        Show show = showMap.get(showId);
        if (user == null) {
            System.out.println("User not found: " + userId);
            return Optional.empty();
        }
        if (show == null) {
            System.out.println("Show not found: " + showId);
            return Optional.empty();
        }

        return seatBookingService.createBooking(user, show, desiredSeats, paymentStrategy);
    }

    public Optional<Show> getShow(String showId) {
        return Optional.ofNullable(showMap.get(showId));
    }

    public SeatLockingService getSeatLockingService() {
        return seatBookingService.getSeatLockingService();
    }
}
