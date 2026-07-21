package model;

import java.util.UUID;
import enums.NotificationChannelEnum;
import enums.NotificationTypeEnum;

public class User {

    private final String id;
    private final String name;
    private final String email;
    private final String phone;
    private final NotificationPreference notificationPreference;

    public User(String name, String email, String phone) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.notificationPreference = new NotificationPreference();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public NotificationPreference getNotificationPreference() {
        return notificationPreference;
    }

    public void optOut(NotificationTypeEnum type) {
        notificationPreference.optOut(type);
    }

    public void optOut(NotificationTypeEnum type, NotificationChannelEnum channel) {
        notificationPreference.optOut(type, channel);
    }

    public void optIn(NotificationTypeEnum type) {
        notificationPreference.optIn(type);
    }

    public void optIn(NotificationTypeEnum type, NotificationChannelEnum channel) {
        notificationPreference.optIn(type, channel);
    }

    public boolean hasOptedOut(NotificationTypeEnum type, NotificationChannelEnum channel) {
        return notificationPreference.hasOptedOut(type, channel);
    }
}
