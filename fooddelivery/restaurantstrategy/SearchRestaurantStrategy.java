package restaurantstrategy;

import java.util.List;
import model.Restaurant;

public interface SearchRestaurantStrategy {
    public List<Restaurant> filterRestaurant(List<Restaurant> restaurant);
}
