package recommendationengine.pipeline;

import java.util.List;

import recommendationengine.model.Item;
import recommendationengine.model.User;

public interface CandidateRetriever {
    List<Item> retrieve(User user);
}
