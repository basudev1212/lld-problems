package recommendationengine.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import recommendationengine.model.Item;
import recommendationengine.model.User;
import recommendationengine.model.UserEvent;

public class UserProfileService {
    private final Map<User, Set<Item>> userItemsMap;
    private final Map<User, List<UserEvent>> userEventMap; 

    public UserProfileService() {
        userItemsMap = new HashMap<>();
        userEventMap = new HashMap<>();
    }

    public void addItem(Item item, User user){
        if(userItemsMap.containsKey(user)){
            Set<Item> currentItemsSet = userItemsMap.get(user);
            if(!currentItemsSet.contains(item)){
                currentItemsSet.add(item);
                userItemsMap.put(user, currentItemsSet);
            }
        }
        else {
            Set<Item> newItemSet = new HashSet<>();
            newItemSet.add(item);
            userItemsMap.put(user, newItemSet);
        }
    }

    public List<Item> getItemsForUser(User user){
        Set<Item> userItems = userItemsMap.get(user);
        if(userItems == null){
            return new ArrayList<>();
        }
        List<Item> listOfUserItems = new ArrayList<>(userItems);
        return listOfUserItems;
    }

    public void addEvent(User user, UserEvent userEvent){
        if(userEventMap.containsKey(user)){
            List<UserEvent> currentList = userEventMap.get(user);
            currentList.add(userEvent);
            userEventMap.put(user, currentList);
        }
        else{
            List<UserEvent> newList = new ArrayList<>();
            newList.add(userEvent);
            userEventMap.put(user, newList);
        }
    }

    public List<UserEvent> getEventsForUser(User user){
        List<UserEvent> events = userEventMap.get(user);
        if(events == null){
            return new ArrayList<>();
        }
        return events;
    }


}
