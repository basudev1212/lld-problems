package model;

import java.util.HashMap;
import java.util.Map;

public class Menu {
    private final Map<String, MenuItem> menuItems = new HashMap<>();
    
    public void addItem(MenuItem menuItem){
        menuItems.put(menuItem.getId(), menuItem);
    }

    public MenuItem getItem(String id){
        MenuItem menuItem = menuItems.get(id);
        return menuItem;
    }

    public Map<String, MenuItem> getAllItems(){
        return menuItems;
    }
}
