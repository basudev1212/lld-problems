package recommendationengine.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import recommendationengine.enums.Category;
import recommendationengine.model.Item;

public class CatalogService {
    private final Set<Item> itemSet;

    public CatalogService() {
        this.itemSet = new HashSet<>();
    }

    public void addItem(Item item, Integer addedScore){
        if(itemSet.contains(item)){
            Integer currentScore = item.getPopularityScore();
            item.setPopularityScore(currentScore+addedScore);
        }
        else{
            item.setPopularityScore(addedScore);
            itemSet.add(item);
        }
    }

    public List<Item> getAllItems(){
        List<Item> allItemsList = new ArrayList<>(itemSet);
        return allItemsList;
    }

    public List<Item> getItemsByCategory(Category category){
        List<Item> matchingItems = new ArrayList<>();
        for(Item item : itemSet){
            if(item.getCategory() == category){
                matchingItems.add(item);
            }
        }
        return matchingItems;
    }
}
