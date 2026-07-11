package recommendationengine.recommender;

import recommendationengine.filter.AlreadySeenFilter;
import recommendationengine.pipeline.CandidateRetriever;
import recommendationengine.pipeline.Ranker;
import recommendationengine.pipeline.RecommendationFilter;
import recommendationengine.ranker.PopularityRanker;
import recommendationengine.retriever.ContentBasedRetriever;
import recommendationengine.retriever.PopularityBasedRetreiver;
import recommendationengine.service.CatalogService;
import recommendationengine.service.UserProfileService;
import recommendationengine.strategy.ContentBasedRecommender;
import recommendationengine.strategy.HybridRecommender;
import recommendationengine.strategy.PopularityBasedRecommender;

public class RecommenderFactory {
    private CatalogService catalogService;
    private UserProfileService userProfileService;

    public RecommenderFactory(CatalogService catalogService, UserProfileService userProfileService) {
        this.catalogService = catalogService;
        this.userProfileService = userProfileService;
    }

    public Recommender getRecommender(int userEventCount){
        if(userEventCount == 0)
            return createPopularityBasedRecommender();
        else if(userEventCount < 10)
            return new HybridRecommender();
        else
            return createContentBasedRecommender();
    }

    private Recommender createPopularityBasedRecommender(){
        CandidateRetriever candidateRetriever = new PopularityBasedRetreiver(catalogService);
        RecommendationFilter recommendationFilter = new AlreadySeenFilter(userProfileService);
        Ranker ranker = new PopularityRanker();
        return new PopularityBasedRecommender(candidateRetriever, recommendationFilter, ranker);
    }

    private Recommender createContentBasedRecommender(){
        CandidateRetriever candidateRetriever = new ContentBasedRetriever(userProfileService, catalogService);
        RecommendationFilter recommendationFilter = new AlreadySeenFilter(userProfileService);
        Ranker ranker = new PopularityRanker();
        return new ContentBasedRecommender(candidateRetriever, recommendationFilter, ranker);
    }
}
