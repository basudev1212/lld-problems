import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import model.Booking;
import model.Screen;
import model.Seat;
import service.BookingService;
import strategy.OnlinePayment;

public class BookMyShowApplication {

    public static void main(String[] args) throws InterruptedException {
        BookingService bookingService = new BookingService();

        bookingService.addCity("city-1", "Mumbai");
        bookingService.addMovie("movie-1", "Inception", 148);
        bookingService.addUser("user-1", "Alice", "alice@example.com");
        bookingService.addUser("user-2", "Bob", "bob@example.com");
        bookingService.addUser("user-3", "Charlie", "charlie@example.com");

        bookingService.addTheatre("theatre-1", "PVR Phoenix", "city-1");
        Screen screen = bookingService.createScreen("screen-1", 4);
        bookingService.addShow("show-1", "movie-1", "theatre-1", screen, "18:00", 250.0);

        String showId = "show-1";
        List<Seat> availableSeats = bookingService.getAvailableSeats(showId);
        Seat seatToBook = availableSeats.get(0);

        System.out.println("=== BookMyShow ===");
        System.out.println("Available seats: " + availableSeats.size());
        System.out.println("3 users trying to book seat " + seatToBook.getSeatNumber() + " at the same time...\n");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(() -> bookSeat(bookingService, "user-1", showId, seatToBook));
        executor.submit(() -> bookSeat(bookingService, "user-2", showId, seatToBook));
        executor.submit(() -> bookSeat(bookingService, "user-3", showId, seatToBook));

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\nSeat " + seatToBook.getSeatNumber() + " final status: " + seatToBook.getSeatStatus());
        bookingService.getSeatLockingService().shutdown();
    }

    private static void bookSeat(BookingService bookingService, String userId, String showId, Seat seat) {
        Optional<Booking> booking = bookingService.bookTickets(
                userId, showId, List.of(seat), new OnlinePayment());

        if (booking.isPresent()) {
            System.out.println(userId + " booked seat " + seat.getSeatNumber());
        } else {
            System.out.println(userId + " could not book seat " + seat.getSeatNumber());
        }
    }
}
