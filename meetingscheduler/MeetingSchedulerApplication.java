import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import models.Meeting;
import models.MeetingRoom;
import service.MeetingSchedulerService;

public class MeetingSchedulerApplication {

    public static void main(String[] args) throws InterruptedException {
        MeetingSchedulerService scheduler = new MeetingSchedulerService();

        scheduler.registerUser("user-1", "Alice", "alice@example.com");
        scheduler.registerUser("user-2", "Bob", "bob@example.com");
        scheduler.registerUser("user-3", "Charlie", "charlie@example.com");

        scheduler.addMeetingRoom("room-1", "Conference A", 10);
        scheduler.addMeetingRoom("room-2", "Conference B", 6);
        scheduler.addMeetingRoom("room-3", "Huddle Room", 4);

        int startTime = 9;
        int endTime = 10;

        System.out.println("=== Meeting Scheduler ===");
        System.out.println("Available rooms from " + startTime + " to " + endTime + ":");
        printRooms(scheduler.getAllMeetingRooms(startTime, endTime));

        System.out.println("\n3 users trying to book Conference A for the same slot...\n");

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(() -> bookRoom(scheduler, "user-1", "room-1", "Alice's Meeting", startTime, endTime));
        executor.submit(() -> bookRoom(scheduler, "user-2", "room-1", "Bob's Meeting", startTime, endTime));
        executor.submit(() -> bookRoom(scheduler, "user-3", "room-1", "Charlie's Meeting", startTime, endTime));

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\nAvailable rooms after concurrent booking:");
        printRooms(scheduler.getAllMeetingRooms(startTime, endTime));

        System.out.println("\nTotal meetings booked: " + scheduler.getAllMeetings().size());
    }

    private static void bookRoom(MeetingSchedulerService scheduler, String userId, String roomId,
                                 String title, int startTime, int endTime) {
        Meeting meeting = scheduler.bookMeetingRoom(userId, roomId, title, startTime, endTime);
        if (meeting != null) {
            System.out.println(userId + " booked " + roomId);
        } else {
            System.out.println(userId + " could not book " + roomId);
        }
    }

    private static void printRooms(List<MeetingRoom> rooms) {
        for (MeetingRoom room : rooms) {
            System.out.println("  " + room.getName());
        }
    }
}
