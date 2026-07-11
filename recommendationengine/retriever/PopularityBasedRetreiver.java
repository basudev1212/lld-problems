package recommendationengine.retriever;

import java.util.List;
import recommendationengine.model.Item;
import recommendationengine.model.User;
import recommendationengine.pipeline.CandidateRetriever;
import recommendationengine.service.CatalogService;

public class PopularityBasedRetreiver implements CandidateRetriever {
    private CatalogService catalogService;

    public PopularityBasedRetreiver(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @Override
    public List<Item> retrieve(User user) {
        return catalogService.getAllItems();
    }

}
