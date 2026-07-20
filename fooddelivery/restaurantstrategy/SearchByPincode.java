package restaurantstrategy;

import java.util.ArrayList;
import java.util.List;
import model.Address;
import model.Restaurant;

public class SearchByPincode implements SearchRestaurantStrategy {
    private final String pincode;

    public SearchByPincode(String pincode) {
        this.pincode = pincode;
    }



    @Override
    public List<Restaurant> filterRestaurant(List<Restaurant> restaurants) {
        List<Restaurant> finalList = new ArrayList<>();
        for(Restaurant restaurant : restaurants){
            Address restaurantAddress = restaurant.getAddress();
            if(restaurantAddress.getPincode().equals(pincode))
                finalList.add(restaurant);
        }
        return finalList;
    }
    
}
