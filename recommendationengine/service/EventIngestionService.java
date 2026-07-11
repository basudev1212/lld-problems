package recommendationengine.service;

import recommendationengine.enums.EventType;
import static recommendationengine.enums.EventType.LIKE;
import static recommendationengine.enums.EventType.VIEW;
import recommendationengine.model.Item;
import recommendationengine.model.User;
import recommendationengine.model.UserEvent;

public class EventIngestionService {

    private CatalogService catalogService;
    private UserProfileService userProfileService;

    public EventIngestionService(CatalogService catalogService, UserProfileService userProfileService) {
        this.catalogService = catalogService;
        this.userProfileService = userProfileService;
    }

    public void processEvent(UserEvent userEvent){
        Item item = userEvent.getItem();
        User user = userEvent.getUser();

        EventType eventType = userEvent.getEventType();

        if(eventType.equals(VIEW)){
            catalogService.addItem(item, 1);
            userProfileService.addItem(item, user);
        }
        else if(eventType.equals(LIKE)){
            catalogService.addItem(item, 5);
            userProfileService.addItem(item, user);
        }

        userProfileService.addEvent(user, userEvent);
    }
    
}
