package recommendationengine.filter;

import java.util.ArrayList;
import java.util.List;

import recommendationengine.model.Item;
import recommendationengine.model.User;
import recommendationengine.pipeline.RecommendationFilter;
import recommendationengine.service.UserProfileService;

public class AlreadySeenFilter implements RecommendationFilter {
    private UserProfileService userProfileService;

    public AlreadySeenFilter(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Override
    public List<Item> filter(User user, List<Item> candidates) {
        List<Item> userItems = userProfileService.getItemsForUser(user);
        List<Item> filteredItems = new ArrayList<>();
        for(int i = 0; i < candidates.size(); i++){
            Item candidateItem = candidates.get(i);
            if(!userItems.contains(candidateItem)){
                filteredItems.add(candidateItem);
            }
        }
        return filteredItems;
    }
}
