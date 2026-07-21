package channels;

import enums.NotificationChannelEnum;
import model.Notification;
import model.User;

public class EmailChannel implements NotificationChannel {

    @Override
    public NotificationChannelEnum getChannelType() {
        return NotificationChannelEnum.EMAIL;
    }

    @Override
    public boolean canDeliver(User user) {
        return user.getEmail() != null && !user.getEmail().isBlank();
    }

    @Override
    public void sendNotification(User user, Notification notification) {
        System.out.println("[EMAIL] to " + user.getEmail() + " | " + notification.getContent());
    }
}
