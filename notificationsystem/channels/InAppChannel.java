package channels;

import enums.NotificationChannelEnum;
import model.Notification;
import model.User;

public class InAppChannel implements NotificationChannel {

    @Override
    public NotificationChannelEnum getChannelType() {
        return NotificationChannelEnum.IN_APP;
    }

    @Override
    public boolean canDeliver(User user) {
        return true;
    }

    @Override
    public void sendNotification(User user, Notification notification) {
        System.out.println("[IN_APP] for " + user.getName() + " | " + notification.getContent());
    }
}
