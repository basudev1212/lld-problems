package recommendationengine.recommender;

import java.util.List;
import recommendationengine.model.Item;
import recommendationengine.model.User;

public interface Recommender {
    public List<Item> getRecommendedItems(User user);
}
