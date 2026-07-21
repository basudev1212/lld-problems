package channels;

import enums.NotificationChannelEnum;
import model.Notification;
import model.User;

public interface NotificationChannel {

    NotificationChannelEnum getChannelType();

    boolean canDeliver(User user);

    void sendNotification(User user, Notification notification);
}
