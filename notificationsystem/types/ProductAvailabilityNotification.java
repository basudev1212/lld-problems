package types;

import enums.NotificationTypeEnum;
import model.User;

public class ProductAvailabilityNotification implements NotificationType {

    @Override
    public NotificationTypeEnum getNotificationType() {
        return NotificationTypeEnum.PRODUCT_AVAILABILITY;
    }

    @Override
    public String getContent(String baseContent, User user) {
        return user.getName() + ", good news! " + baseContent;
    }

    @Override
    public boolean needPersonalisation() {
        return true;
    }
}
