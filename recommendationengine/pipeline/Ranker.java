package recommendationengine.pipeline;

import java.util.List;

import recommendationengine.model.Item;

public interface Ranker {
    List<Item> rank(List<Item> candidates);
}
