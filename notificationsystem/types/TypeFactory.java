package types;

import enums.NotificationTypeEnum;

public class TypeFactory {

    public NotificationType getType(NotificationTypeEnum notificationType) {
        switch (notificationType) {
            case CONNECTION_REQUEST:
                return new ConnectionRequestNotification();
            case PROMOTIONAL:
                return new PromotionalNotification();
            case PRODUCT_AVAILABILITY:
                return new ProductAvailabilityNotification();
            case SYSTEM_ALERT:
                return new SystemAlertNotification();
            default:
                throw new IllegalArgumentException("Unsupported notification type: " + notificationType);
        }
    }
}
