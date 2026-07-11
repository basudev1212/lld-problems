package recommendationengine;

import java.util.List;

import recommendationengine.enums.Category;
import recommendationengine.enums.EventType;
import recommendationengine.model.Item;
import recommendationengine.model.User;
import recommendationengine.model.UserEvent;
import recommendationengine.recommender.Recommender;
import recommendationengine.recommender.RecommenderFactory;
import recommendationengine.service.CatalogService;
import recommendationengine.service.EventIngestionService;
import recommendationengine.service.RecommendationService;
import recommendationengine.service.UserProfileService;

public class RecommendationEngineApplication {

    public static void main(String[] args) {
        CatalogService catalogService = new CatalogService();
        UserProfileService userProfileService = new UserProfileService();
        EventIngestionService eventIngestionService = new EventIngestionService(catalogService, userProfileService);
        RecommenderFactory recommenderFactory = new RecommenderFactory(catalogService, userProfileService);
        RecommendationService recommendationService = new RecommendationService(recommenderFactory, userProfileService);

        Item movieA = new Item("movieA", "2024-01-01", 0, Category.SCIFI);
        Item movieB = new Item("movieB", "2024-01-02", 0, Category.SCIFI);
        Item movieC = new Item("movieC", "2024-01-03", 0, Category.THRILLER);
        Item movieD = new Item("movieD", "2024-01-04", 0, Category.LOVE);
        Item movieE = new Item("movieE", "2024-01-05", 0, Category.ACTION);
        Item movieF = new Item("movieF", "2024-01-06", 0, Category.SCIFI);

        User backgroundUser = new User("backgroundUser", "2023-01-01");
        User newUser = new User("newUser", "2024-02-01");
        User warmUser = new User("warmUser", "2024-02-02");

        System.out.println("Step 1 - background events build up the catalog's popularity scores");
        eventIngestionService.processEvent(new UserEvent(backgroundUser, movieA, EventType.VIEW));
        eventIngestionService.processEvent(new UserEvent(backgroundUser, movieA, EventType.LIKE));
        eventIngestionService.processEvent(new UserEvent(backgroundUser, movieB, EventType.VIEW));
        eventIngestionService.processEvent(new UserEvent(backgroundUser, movieC, EventType.VIEW));
        eventIngestionService.processEvent(new UserEvent(backgroundUser, movieC, EventType.LIKE));
        eventIngestionService.processEvent(new UserEvent(backgroundUser, movieD, EventType.VIEW));
        eventIngestionService.processEvent(new UserEvent(backgroundUser, movieE, EventType.VIEW));
        eventIngestionService.processEvent(new UserEvent(backgroundUser, movieE, EventType.LIKE));
        eventIngestionService.processEvent(new UserEvent(backgroundUser, movieF, EventType.VIEW));
        System.out.println("Catalog after background events:");
        printCatalog(catalogService);

        System.out.println();
        System.out.println("Step 2 - newUser has zero history, so this is a cold start");
        Recommender recommenderForNewUser = recommendationService.getRecommender(newUser);
        System.out.println("Recommender chosen for newUser: " + recommenderForNewUser.getClass().getSimpleName());
        List<Item> newUserRecommendations = recommendationService.recommend(newUser);
        System.out.println("Recommendations for newUser:");
        printRecommendations(newUserRecommendations);

        System.out.println();
        System.out.println("Step 3 - warmUser likes several SCIFI movies, building up history");
        for(int i = 0; i < 5; i++){
            eventIngestionService.processEvent(new UserEvent(warmUser, movieA, EventType.LIKE));
            eventIngestionService.processEvent(new UserEvent(warmUser, movieB, EventType.LIKE));
        }

        System.out.println();
        System.out.println("Step 4 - warmUser now has enough history for content-based recommendations");
        Recommender recommenderForWarmUser = recommendationService.getRecommender(warmUser);
        System.out.println("Recommender chosen for warmUser: " + recommenderForWarmUser.getClass().getSimpleName());
        List<Item> warmUserRecommendations = recommendationService.recommend(warmUser);
        System.out.println("Recommendations for warmUser:");
        printRecommendations(warmUserRecommendations);
    }

    private static void printCatalog(CatalogService catalogService){
        List<Item> allItems = catalogService.getAllItems();
        for(int i = 0; i < allItems.size(); i++){
            Item item = allItems.get(i);
            System.out.println("  " + item.getItemId() + " | category=" + item.getCategory()
                    + " | popularityScore=" + item.getPopularityScore());
        }
    }

    private static void printRecommendations(List<Item> items){
        if(items.isEmpty()){
            System.out.println("  (no recommendations)");
            return;
        }
        for(int i = 0; i < items.size(); i++){
            Item item = items.get(i);
            System.out.println("  " + (i + 1) + ". " + item.getItemId() + " | category=" + item.getCategory()
                    + " | popularityScore=" + item.getPopularityScore());
        }
    }
}
