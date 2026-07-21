package types;

import enums.NotificationTypeEnum;
import model.User;

public class ConnectionRequestNotification implements NotificationType {

    @Override
    public NotificationTypeEnum getNotificationType() {
        return NotificationTypeEnum.CONNECTION_REQUEST;
    }

    @Override
    public String getContent(String baseContent, User user) {
        return "Hi " + user.getName() + ", " + baseContent;
    }

    @Override
    public boolean needPersonalisation() {
        return true;
    }
}
