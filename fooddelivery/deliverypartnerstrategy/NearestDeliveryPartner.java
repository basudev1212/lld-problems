package deliverypartnerstrategy;

import java.util.List;
import model.Address;
import model.Customer;
import model.DeliveryPartner;
import model.Order;
import model.Restaurant;

public class NearestDeliveryPartner implements SearchDeliveryPartnerStrategy {

    @Override
    public DeliveryPartner findDeliveryPartner(List<DeliveryPartner> deliveryPartners, Order order) {
        Restaurant restaurant = order.getRestaurant();
        Customer customer = order.getCustomer();

        Address restaurantAddress = restaurant.getAddress();
        Address customerAddress = customer.getAddress();

        Double minDistance = Double.MAX_VALUE;
        DeliveryPartner nearestDeliveryPartner = null;
        for(DeliveryPartner deliveryPartner : deliveryPartners){
            if(!deliveryPartner.isAvailable()){
                continue;
            }
            Address deliveryPartnerLocation = deliveryPartner.getAddress();
            Double currentDeliveryPartnerDistance = findDistance(restaurantAddress, customerAddress, deliveryPartnerLocation);
            if(currentDeliveryPartnerDistance < minDistance){
                minDistance = currentDeliveryPartnerDistance;
                nearestDeliveryPartner = deliveryPartner;
            }
        }
        return nearestDeliveryPartner;
    }

    private Double findDistance(Address restaurantAddress, Address customerAddress, Address deliveryPartnerLocation) {
        Double distanceBetweenCustomerAndRestaurant = restaurantAddress.distanceBetween(customerAddress);
        Double distanceBetweenDeliveryPartnerAndRestaurant = deliveryPartnerLocation.distanceBetween(restaurantAddress);
        return distanceBetweenCustomerAndRestaurant + distanceBetweenDeliveryPartnerAndRestaurant;
    }

    
}
