package recommendationengine.service;

import java.util.List;
import recommendationengine.model.Item;
import recommendationengine.model.User;
import recommendationengine.model.UserEvent;
import recommendationengine.recommender.Recommender;
import recommendationengine.recommender.RecommenderFactory;

public class RecommendationService {
    private RecommenderFactory recommenderFactory;
    private UserProfileService userProfileService;

    public RecommendationService(RecommenderFactory recommenderFactory, UserProfileService userProfileService) {
        this.recommenderFactory = recommenderFactory;
        this.userProfileService = userProfileService;
    }

    //this is method which our controller can call to get the recommendation
    public List<Item> recommend(User user){
        Recommender recommender = getRecommender(user);
        return recommender.getRecommendedItems(user);
    }

    public Recommender getRecommender(User user){
        List<UserEvent> userEvents = userProfileService.getEventsForUser(user);
        int userEventCount = userEvents.size();
        return recommenderFactory.getRecommender(userEventCount);
    }
    
}
