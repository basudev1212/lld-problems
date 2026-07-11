package recommendationengine.ranker;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import recommendationengine.model.Item;
import recommendationengine.pipeline.Ranker;

public class PopularityRanker implements Ranker {

    @Override
    public List<Item> rank(List<Item> candidates) {
        Collections.sort(candidates, new PopularityDescendingComparator());
        return candidates;
    }

    private static class PopularityDescendingComparator implements Comparator<Item> {
        @Override
        public int compare(Item firstItem, Item secondItem) {
            if(firstItem.getPopularityScore() < secondItem.getPopularityScore()){
                return 1;
            }
            else if(firstItem.getPopularityScore() > secondItem.getPopularityScore()){
                return -1;
            }
            else{
                return 0;
            }
        }
    }
}
