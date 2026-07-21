package types;

import enums.NotificationTypeEnum;
import model.User;

public interface NotificationType {

    NotificationTypeEnum getNotificationType();

    String getContent(String baseContent, User user);

    boolean needPersonalisation();

    default boolean isMandatory() {
        return false;
    }
}
