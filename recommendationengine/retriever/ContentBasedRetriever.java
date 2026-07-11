package recommendationengine.retriever;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import recommendationengine.enums.Category;
import recommendationengine.model.Item;
import recommendationengine.model.User;
import recommendationengine.pipeline.CandidateRetriever;
import recommendationengine.service.CatalogService;
import recommendationengine.service.UserProfileService;

public class ContentBasedRetriever implements CandidateRetriever {
    private UserProfileService userProfileService;
    private CatalogService catalogService;

    public ContentBasedRetriever(UserProfileService userProfileService, CatalogService catalogService) {
        this.userProfileService = userProfileService;
        this.catalogService = catalogService;
    }

    @Override
    public List<Item> retrieve(User user) {
        List<Item> userItems = userProfileService.getItemsForUser(user);

        Set<Category> likedCategories = new HashSet<>();
        for(int i = 0; i < userItems.size(); i++){
            Item userItem = userItems.get(i);
            likedCategories.add(userItem.getCategory());
        }

        List<Item> candidates = new ArrayList<>();
        for(Category category : likedCategories){
            List<Item> itemsInCategory = catalogService.getItemsByCategory(category);
            for(int i = 0; i < itemsInCategory.size(); i++){
                Item candidateItem = itemsInCategory.get(i);
                if(!candidates.contains(candidateItem)){
                    candidates.add(candidateItem);
                }
            }
        }

        return candidates;
    }
}
