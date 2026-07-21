package model;

import java.time.LocalDateTime;
import java.util.UUID;
import enums.NotificationChannelEnum;
import enums.NotificationStatus;
import enums.NotificationTypeEnum;

public class Notification {

    private final String id;
    private final NotificationTypeEnum type;
    private final String content;
    private final User recipient;
    private final NotificationChannelEnum channel;
    private NotificationStatus status;
    private final Publisher publisher;
    private final LocalDateTime createdAt;

    public Notification(NotificationTypeEnum type, String content, User recipient,
                        NotificationChannelEnum channel, Publisher publisher) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.content = content;
        this.recipient = recipient;
        this.channel = channel;
        this.publisher = publisher;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public NotificationTypeEnum getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public User getRecipient() {
        return recipient;
    }

    public NotificationChannelEnum getChannel() {
        return channel;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
