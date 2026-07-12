package service;

import enums.SeatStatus;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import model.Seat;
import model.Show;
import model.User;

public class SeatLockingService {
    //map of show -> {{seat, user}}
    private Map<String, Map<String, String>> lockedSeats;
    private ScheduledExecutorService seatUnlockingService;
    private static final long LOCK_TIMEOUT_MS = 5000;

    public SeatLockingService() {
        lockedSeats = new ConcurrentHashMap<>();
        seatUnlockingService = Executors.newScheduledThreadPool(1);
    }

    public boolean lockSeats(Show show, List<Seat> seats, User user) {
        synchronized (show) {

            //checking to avoid TOCTOU issue
            for(Seat seat : seats){
                if(seat.getSeatStatus() != SeatStatus.AVAILABLE){
                    System.out.println("Seat with " + seat.getId() + " is not available.");
                    return false;
                } 
            }

            //locking the seats
            for(Seat seat : seats){
                seat.setSeatStatus(SeatStatus.LOCKED);
            }

            //put seat in the locked seats map
            String showId = show.getId();
            if(!lockedSeats.containsKey(showId))
                lockedSeats.put(showId, new ConcurrentHashMap<>());

            Map<String, String> showMap = lockedSeats.get(showId);

            for(Seat seat : seats){
                String seatId = seat.getId();
                String userId = user.getId();
                showMap.put(seatId, userId);
            }

            //the scheduler service take in a callable action, and schedules after some time

            seatUnlockingService.schedule(() ->  unlockSeats(show, seats, user), LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS);


            System.out.println("Seat(s) locked for the user id: " + user.getId());
            return true;
        }
    }

    public void unlockSeats(Show show, List<Seat> seats, User user) {
        synchronized (show) {
            String showId = show.getId();
            Map<String, String> showMap = lockedSeats.get(showId);
            if (showMap == null) {
                return;
            }

            for (Seat seat : seats) {
                String seatId = seat.getId();
                String lockedByUserId = showMap.get(seatId);

                if (lockedByUserId != null && lockedByUserId.equals(user.getId())) {
                    showMap.remove(seatId);

                    if (seat.getSeatStatus() == SeatStatus.LOCKED) {
                        seat.setSeatStatus(SeatStatus.AVAILABLE);
                        System.out.println("Unlocked seat: " + seatId);
                    }
                }
            }

            if (showMap.isEmpty()) {
                lockedSeats.remove(showId);
            }
        }
    }

    public void confirmSeats(Show show, List<Seat> seats, User user) {
        synchronized (show) {
            String showId = show.getId();
            Map<String, String> showMap = lockedSeats.get(showId);
            if (showMap == null) {
                return;
            }

            for (Seat seat : seats) {
                String seatId = seat.getId();
                String lockedByUserId = showMap.get(seatId);

                if (lockedByUserId != null && lockedByUserId.equals(user.getId())) {
                    showMap.remove(seatId);
                    seat.setSeatStatus(SeatStatus.BOOKED);
                    System.out.println("Confirmed seat: " + seatId);
                }
            }

            if (showMap.isEmpty()) {
                lockedSeats.remove(showId);
            }
        }
    }

    public boolean isSeatAvailableForShow(String showId, String seatId) {
        Map<String, String> showMap = lockedSeats.get(showId);
        if (showMap != null && showMap.containsKey(seatId)) {
            return false;
        }
        return true;
    }

    public void shutdown() {
        System.out.println("Shutting down seat unlocking scheduler.");
        seatUnlockingService.shutdown();
        try {
            if (!seatUnlockingService.awaitTermination(5, TimeUnit.SECONDS)) {
                seatUnlockingService.shutdownNow();
            }
        } catch (InterruptedException e) {
            seatUnlockingService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
