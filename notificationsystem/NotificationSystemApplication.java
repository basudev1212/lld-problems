import java.util.List;
import enums.NotificationChannelEnum;
import enums.NotificationStatus;
import enums.NotificationTypeEnum;
import enums.PublisherType;
import model.Notification;
import model.Publisher;
import model.User;
import service.NotificationService;

public class NotificationSystemApplication {

    public static void main(String[] args) {
        NotificationService service = NotificationService.getInstance();

        System.out.println("=== Notification System ===\n");

        User alice = service.registerUser("Alice", "alice@example.com", "9876543210");
        User bob = service.registerUser("Bob", "bob@example.com", "9123456780");

        Publisher marketing = service.registerPublisher(new Publisher("Marketing Team", PublisherType.MARKETING));
        Publisher system = service.registerPublisher(new Publisher("System", PublisherType.SYSTEM));
        Publisher social = service.registerPublisher(new Publisher("Social Network", PublisherType.USER));

        // Bob opts out of promotional emails
        service.optOut(bob.getId(), NotificationTypeEnum.PROMOTIONAL, NotificationChannelEnum.EMAIL);
        System.out.println("Bob opted out of promotional emails.\n");

        // Generic promotional notification to both users
        System.out.println("--- Promotional (generic) via EMAIL + IN_APP ---");
        List<Notification> promoResults = service.sendToMany(
                List.of(alice.getId(), bob.getId()),
                marketing.getId(),
                NotificationTypeEnum.PROMOTIONAL,
                "Flat 50% off on all items this weekend!",
                List.of(NotificationChannelEnum.EMAIL, NotificationChannelEnum.IN_APP));
        printResults(promoResults);

        // Personalized connection request
        System.out.println("\n--- Connection request (personalised) via SMS + IN_APP ---");
        List<Notification> connectionResults = service.send(
                alice.getId(),
                social.getId(),
                NotificationTypeEnum.CONNECTION_REQUEST,
                "Charlie wants to connect with you.",
                List.of(NotificationChannelEnum.SMS, NotificationChannelEnum.IN_APP));
        printResults(connectionResults);

        // Product availability alert
        System.out.println("\n--- Product availability (personalised) via PUSH ---");
        List<Notification> productResults = service.send(
                bob.getId(),
                system.getId(),
                NotificationTypeEnum.PRODUCT_AVAILABILITY,
                "Your saved item 'Wireless Headphones' is back in stock.",
                List.of(NotificationChannelEnum.PUSH));
        printResults(productResults);

        // Mandatory system alert — always delivered (cannot be opted out)
        System.out.println("\n--- System alert (mandatory) ---");
        List<Notification> alertBeforeOptOut = service.send(
                bob.getId(),
                system.getId(),
                NotificationTypeEnum.SYSTEM_ALERT,
                "Scheduled maintenance tonight at 11 PM.",
                List.of(NotificationChannelEnum.EMAIL, NotificationChannelEnum.IN_APP));
        printResults(alertBeforeOptOut);

        // Attempt to opt out of mandatory notification
        System.out.println("\n--- Opt-out guard for mandatory notifications ---");
        try {
            service.optOut(alice.getId(), NotificationTypeEnum.SYSTEM_ALERT);
            System.out.println("Unexpected: opt-out succeeded for SYSTEM_ALERT");
        } catch (IllegalStateException ex) {
            System.out.println("Blocked: " + ex.getMessage());
        }

        System.out.println("\nTotal notifications recorded: " + service.getNotificationHistory().size());
    }

    private static void printResults(List<Notification> notifications) {
        for (Notification notification : notifications) {
            System.out.println("  " + notification.getRecipient().getName()
                    + " | " + notification.getChannel()
                    + " | " + notification.getStatus()
                    + " | " + notification.getContent());
        }
    }
}
