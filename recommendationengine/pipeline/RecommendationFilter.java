package recommendationengine.pipeline;

import java.util.List;

import recommendationengine.model.Item;
import recommendationengine.model.User;

public interface RecommendationFilter {
    List<Item> filter(User user, List<Item> candidates);
}
