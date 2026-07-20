package restaurantstrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Menu;
import model.MenuItem;
import model.Restaurant;

public class SearchByMenuItem implements SearchRestaurantStrategy {
    private final String food;

    public SearchByMenuItem(String food){
        this.food = food;
    }

    @Override
    public List<Restaurant> filterRestaurant(List<Restaurant> restaurants) {
        List<Restaurant> finalList = new ArrayList<>();
        for(Restaurant currentRestaurant : restaurants){
            Menu currentMenu = currentRestaurant.getMenu();
            Map<String, MenuItem> menuItems = currentMenu.getAllItems();
            for(MenuItem menuItem :  menuItems.values()){
                if(menuItem.getIsAvailable() && menuItem.getName().equals(food)){
                    finalList.add(currentRestaurant);
                }
            }
        }
        return finalList;
    }
    
}
