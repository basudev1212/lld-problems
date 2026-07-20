package service;

import deliverypartnerstrategy.NearestDeliveryPartner;
import deliverypartnerstrategy.SearchDeliveryPartnerStrategy;
import enums.OrderStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.Address;
import model.Customer;
import model.DeliveryPartner;
import model.Menu;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.Restaurant;
import restaurantstrategy.SearchByMenuItem;
import restaurantstrategy.SearchByPincode;
import restaurantstrategy.SearchRestaurantStrategy;

public class OnboardingService {
    private static OnboardingService instance;
    private final Map<String, Restaurant> restaurantMap;
    private final Map<String, Customer> customerMap;
    private final Map<String, DeliveryPartner> deliveryPartnerMap;
    private final Map<String, Order> orderMap;
    private SearchDeliveryPartnerStrategy searchDeliveryPartnerStrategy;

    
    public OnboardingService(){
        restaurantMap = new ConcurrentHashMap<>();
        customerMap = new ConcurrentHashMap<>();
        deliveryPartnerMap = new ConcurrentHashMap<>();
        orderMap = new ConcurrentHashMap<>();
        searchDeliveryPartnerStrategy = new NearestDeliveryPartner();
    }

    //singleton
    public static OnboardingService getInstance(){
        if(instance == null){
            synchronized (OnboardingService.class) {
                if(instance == null)
                    instance = new OnboardingService();
            }
        }
        return instance;
    }

    public Customer addCustomer(String name, Address address){
        Customer customer = new Customer(name, address);
        customerMap.put(customer.getId(), customer);
        return customer;
    }

    public Restaurant addRestaurant(String name, Address address){
        Restaurant restaurant = new Restaurant(name, address);
        restaurantMap.put(restaurant.getId(), restaurant);
        return restaurant;
    }

    public DeliveryPartner addDeliverPartner(String name, Address currentLocation){
        DeliveryPartner deliveryPartner = new DeliveryPartner(name, currentLocation);
        deliveryPartnerMap.put(deliveryPartner.getId(), deliveryPartner);
        return deliveryPartner;
    }

    //changing location of partner
    public void updateDeliveryPartnerLocation(String deliveryPartnerId, Address address){
        DeliveryPartner deliveryPartner = deliveryPartnerMap.get(deliveryPartnerId);
        deliveryPartner.updateLocation(address);
    }

    public List<Restaurant> findAllNearbyRestaurant(String pincode){
        List<Restaurant> allRestaurants = new ArrayList<>(restaurantMap.values());
        SearchRestaurantStrategy searchRestaurantStrategy = new SearchByPincode(pincode);
        return searchRestaurantStrategy.filterRestaurant(allRestaurants);
    }

    public List<Restaurant> findAllRestaurantsByFood(String food){
        List<Restaurant> allRestaurants = new ArrayList<>(restaurantMap.values());
        SearchRestaurantStrategy searchRestaurantStrategy = new SearchByMenuItem(food);
        return searchRestaurantStrategy.filterRestaurant(allRestaurants);
    }

    public void addMenuItem(String restaurantId, MenuItem menuItem){
        Restaurant restaurant = restaurantMap.get(restaurantId);
        if(restaurant == null){
            System.out.println("Restaurant not found: " + restaurantId);
            return;
        }
        restaurant.addMenuItem(menuItem);
    }

    public List<MenuItem> getRestaurantMenuItems(String restaurantId){
        Restaurant restaurant = restaurantMap.get(restaurantId);
        if(restaurant == null){
            return List.of();
        }
        Menu restaurantMenu = restaurant.getMenu();
        Map<String, MenuItem> menuItemsMap = restaurantMenu.getAllItems();
        return new ArrayList<>(menuItemsMap.values());
    }

    public Order placeOrder(String customerId, String restaurantId, List<OrderItem> items){
        Customer customer = customerMap.get(customerId);
        Restaurant restaurant = restaurantMap.get(restaurantId);
        if(restaurant == null || customer == null){
            System.out.println("Invalid customer or restaurant");
            return null;
        }
        Order order = new Order(customer, restaurant, items);
        order.setOrderStatus(OrderStatus.PLACED);
        customer.addOrderToHistory(order);
        orderMap.put(order.getId(), order);
        return order;
    }

    public Order getOrder(String orderId){
        return orderMap.get(orderId);
    }

    public void updateOrderStatus(String orderId, OrderStatus newStatus){
        Order order = orderMap.get(orderId);
        if(order == null){
            System.out.println("Order not found: " + orderId);
            return;
        }
        order.setOrderStatus(newStatus);
        if(newStatus == OrderStatus.DELIVERED){
            DeliveryPartner partner = order.getDeliveryPartner();
            if(partner != null){
                partner.setAvailable(true);
            }
        }
    }

    public void assignDeliveryPartner(String orderId){
        Order order = orderMap.get(orderId);
        if(order == null){
            System.out.println("Order not found: " + orderId);
            return;
        }
        List<DeliveryPartner> deliveryPartners = new ArrayList<>(deliveryPartnerMap.values());
        DeliveryPartner nearestDeliveryPartner = searchDeliveryPartnerStrategy.findDeliveryPartner(deliveryPartners, order);
        if(nearestDeliveryPartner == null){
            System.out.println("No delivery partner available for order: " + orderId);
            return;
        }
        nearestDeliveryPartner.setAvailable(false);
        order.setDeliveryPartner(nearestDeliveryPartner);
        order.addObserver(nearestDeliveryPartner);
        order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
    }

    public void setSearchDeliveryPartnerStrategy(SearchDeliveryPartnerStrategy strategy){
        this.searchDeliveryPartnerStrategy = strategy;
    }
}
