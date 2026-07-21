package channels;

import enums.NotificationChannelEnum;
import model.Notification;
import model.User;

public class PushChannel implements NotificationChannel {

    @Override
    public NotificationChannelEnum getChannelType() {
        return NotificationChannelEnum.PUSH;
    }

    @Override
    public boolean canDeliver(User user) {
        return true;
    }

    @Override
    public void sendNotification(User user, Notification notification) {
        System.out.println("[PUSH] to device of " + user.getName() + " | " + notification.getContent());
    }
}
