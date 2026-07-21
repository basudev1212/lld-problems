package types;

import enums.NotificationTypeEnum;
import model.User;

public class PromotionalNotification implements NotificationType {

    @Override
    public NotificationTypeEnum getNotificationType() {
        return NotificationTypeEnum.PROMOTIONAL;
    }

    @Override
    public String getContent(String baseContent, User user) {
        return baseContent;
    }

    @Override
    public boolean needPersonalisation() {
        return false;
    }
}
