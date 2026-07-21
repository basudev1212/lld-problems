package types;

import enums.NotificationTypeEnum;
import model.User;

public class SystemAlertNotification implements NotificationType {

    @Override
    public NotificationTypeEnum getNotificationType() {
        return NotificationTypeEnum.SYSTEM_ALERT;
    }

    @Override
    public String getContent(String baseContent, User user) {
        return "[ALERT] " + baseContent;
    }

    @Override
    public boolean needPersonalisation() {
        return false;
    }

    @Override
    public boolean isMandatory() {
        return true;
    }
}
