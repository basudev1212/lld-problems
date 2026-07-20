import java.util.List;
import model.Address;
import model.Customer;
import model.DeliveryPartner;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.Restaurant;
import enums.OrderStatus;
import service.OnboardingService;

public class FoodDeliveryApplication {

    public static void main(String[] args) {
        OnboardingService service = OnboardingService.getInstance();

        System.out.println("=== Food Delivery ===\n");

        // Onboard users
        Customer alice = service.addCustomer("Alice",
                new Address("Bangalore", "560001", 12.9716, 77.5946));
        Customer bob = service.addCustomer("Bob",
                new Address("Bangalore", "560002", 12.9352, 77.6245));

        Restaurant pizzaPalace = service.addRestaurant("Pizza Palace",
                new Address("Bangalore", "560001", 12.9750, 77.5900));
        Restaurant burgerHub = service.addRestaurant("Burger Hub",
                new Address("Bangalore", "560002", 12.9300, 77.6200));

        DeliveryPartner ravi = service.addDeliverPartner("Ravi",
                new Address("Bangalore", "560001", 12.9700, 77.5950));
        DeliveryPartner suresh = service.addDeliverPartner("Suresh",
                new Address("Bangalore", "560002", 12.9400, 77.6250));

        // Build menus
        MenuItem margherita = new MenuItem("item-1", "Margherita Pizza", 299.0, true);
        MenuItem pepperoni = new MenuItem("item-2", "Pepperoni Pizza", 399.0, true);
        MenuItem classicBurger = new MenuItem("item-3", "Classic Burger", 199.0, true);

        service.addMenuItem(pizzaPalace.getId(), margherita);
        service.addMenuItem(pizzaPalace.getId(), pepperoni);
        service.addMenuItem(burgerHub.getId(), classicBurger);

        // Search restaurants
        System.out.println("Restaurants near pincode 560001:");
        printRestaurants(service.findAllNearbyRestaurant("560001"));

        System.out.println("\nRestaurants serving Margherita Pizza:");
        printRestaurants(service.findAllRestaurantsByFood("Margherita Pizza"));

        // Place order
        System.out.println("\n--- Order Flow ---");
        OrderItem orderItem = new OrderItem("oi-1", margherita, 2);
        Order order = service.placeOrder(alice.getId(), pizzaPalace.getId(), List.of(orderItem));
        System.out.println("Order placed: " + order.getId() + " | Total: Rs " + order.getTotalAmount());

        // Restaurant workflow
        service.updateOrderStatus(order.getId(), OrderStatus.ACCEPTED);
        service.updateOrderStatus(order.getId(), OrderStatus.PREPARING);
        service.updateOrderStatus(order.getId(), OrderStatus.READY_FOR_PICKUP);

        // Assign nearest available delivery partner
        System.out.println("\nAssigning delivery partner...");
        service.assignDeliveryPartner(order.getId());

        // Complete delivery
        service.updateOrderStatus(order.getId(), OrderStatus.DELIVERED);

        System.out.println("\nFinal order status: " + order.getOrderStatus());
        System.out.println("Delivery partner available again: " + order.getDeliveryPartner().isAvailable());

        // Second order from Bob — Suresh is nearest to Burger Hub
        System.out.println("\n--- Second Order ---");
        OrderItem burgerItem = new OrderItem("oi-2", classicBurger, 1);
        Order order2 = service.placeOrder(bob.getId(), burgerHub.getId(), List.of(burgerItem));
        service.updateOrderStatus(order2.getId(), OrderStatus.ACCEPTED);
        service.updateOrderStatus(order2.getId(), OrderStatus.READY_FOR_PICKUP);
        service.assignDeliveryPartner(order2.getId());
        System.out.println("Order " + order2.getId() + " assigned to: " + order2.getDeliveryPartner().getName());

        System.out.println("\nAlice order history size: " + alice.getOrderHistory().size());
        System.out.println("Bob order history size: " + bob.getOrderHistory().size());
    }

    private static void printRestaurants(List<Restaurant> restaurants) {
        for (Restaurant restaurant : restaurants) {
            System.out.println("  " + restaurant.getName() + " (" + restaurant.getAddress().getPincode() + ")");
        }
    }
}
