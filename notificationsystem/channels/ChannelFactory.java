package channels;

import enums.NotificationChannelEnum;

public class ChannelFactory {

    public NotificationChannel getChannel(NotificationChannelEnum channelType) {
        switch (channelType) {
            case EMAIL:
                return new EmailChannel();
            case SMS:
                return new SmsChannel();
            case IN_APP:
                return new InAppChannel();
            case PUSH:
                return new PushChannel();
            case WHATSAPP:
                return new WhatsappChannel();
            default:
                throw new IllegalArgumentException("Unsupported channel: " + channelType);
        }
    }
}
