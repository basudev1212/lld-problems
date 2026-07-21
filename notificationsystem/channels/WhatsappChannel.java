package channels;

import enums.NotificationChannelEnum;
import model.Notification;
import model.User;

public class WhatsappChannel implements NotificationChannel {

    @Override
    public NotificationChannelEnum getChannelType() {
        return NotificationChannelEnum.WHATSAPP;
    }

    @Override
    public boolean canDeliver(User user) {
        return user.getPhone() != null && !user.getPhone().isBlank();
    }

    @Override
    public void sendNotification(User user, Notification notification) {
        System.out.println("[WHATSAPP] to " + user.getPhone() + " | " + notification.getContent());
    }
}
