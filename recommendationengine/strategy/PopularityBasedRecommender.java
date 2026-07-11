package recommendationengine.strategy;

import java.util.List;

import recommendationengine.model.Item;
import recommendationengine.model.User;
import recommendationengine.pipeline.CandidateRetriever;
import recommendationengine.pipeline.Ranker;
import recommendationengine.pipeline.RecommendationFilter;
import recommendationengine.recommender.Recommender;

public class PopularityBasedRecommender implements Recommender {
    private CandidateRetriever candidateRetriever;
    private RecommendationFilter recommendationFilter;
    private Ranker ranker;

    public PopularityBasedRecommender(CandidateRetriever candidateRetriever,
                                      RecommendationFilter recommendationFilter,
                                      Ranker ranker) {
        this.candidateRetriever = candidateRetriever;
        this.recommendationFilter = recommendationFilter;
        this.ranker = ranker;
    }

    @Override
    public List<Item> getRecommendedItems(User user) {
        List<Item> candidates = candidateRetriever.retrieve(user);
        List<Item> filteredCandidates = recommendationFilter.filter(user, candidates);
        List<Item> rankedItems = ranker.rank(filteredCandidates);
        return rankedItems;
    }
    
}
