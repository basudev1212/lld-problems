package service;

import channels.ChannelFactory;
import channels.NotificationChannel;
import enums.NotificationChannelEnum;
import enums.NotificationStatus;
import enums.NotificationTypeEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.Notification;
import model.Publisher;
import model.User;
import types.NotificationType;
import types.TypeFactory;

public class NotificationService {

    private static NotificationService instance;

    private final Map<String, User> userMap;
    private final Map<String, Publisher> publisherMap;
    private final List<Notification> notificationHistory;
    private final TypeFactory typeFactory;
    private final ChannelFactory channelFactory;

    public NotificationService() {
        this.userMap = new ConcurrentHashMap<>();
        this.publisherMap = new ConcurrentHashMap<>();
        this.notificationHistory = new ArrayList<>();
        this.typeFactory = new TypeFactory();
        this.channelFactory = new ChannelFactory();
    }

    public static NotificationService getInstance() {
        if (instance == null) {
            synchronized (NotificationService.class) {
                if (instance == null) {
                    instance = new NotificationService();
                }
            }
        }
        return instance;
    }

    public User registerUser(String name, String email, String phone) {
        User user = new User(name, email, phone);
        userMap.put(user.getId(), user);
        return user;
    }

    public Publisher registerPublisher(Publisher publisher) {
        publisherMap.put(publisher.getId(), publisher);
        return publisher;
    }

    public void optOut(String userId, NotificationTypeEnum type) {
        User user = requireUser(userId);
        if (typeFactory.getType(type).isMandatory()) {
            throw new IllegalStateException("Cannot opt out of mandatory notification type: " + type);
        }
        user.optOut(type);
    }

    public void optOut(String userId, NotificationTypeEnum type, NotificationChannelEnum channel) {
        User user = requireUser(userId);
        if (typeFactory.getType(type).isMandatory()) {
            throw new IllegalStateException("Cannot opt out of mandatory notification type: " + type);
        }
        user.optOut(type, channel);
    }

    public void optIn(String userId, NotificationTypeEnum type) {
        requireUser(userId).optIn(type);
    }

    public void optIn(String userId, NotificationTypeEnum type, NotificationChannelEnum channel) {
        requireUser(userId).optIn(type, channel);
    }

    public boolean canSend(User user, NotificationTypeEnum type, NotificationChannelEnum channel) {
        NotificationType notificationType = typeFactory.getType(type);
        if (notificationType.isMandatory()) {
            return true;
        }
        return !user.hasOptedOut(type, channel);
    }

    public List<Notification> send(String userId, String publisherId, NotificationTypeEnum type,
                                   String baseContent, List<NotificationChannelEnum> channels) {
        User user = requireUser(userId);
        Publisher publisher = requirePublisher(publisherId);
        return deliver(user, publisher, type, baseContent, channels);
    }

    public List<Notification> sendToMany(List<String> userIds, String publisherId,
                                         NotificationTypeEnum type, String baseContent,
                                         List<NotificationChannelEnum> channels) {
        Publisher publisher = requirePublisher(publisherId);
        List<Notification> results = new ArrayList<>();

        for (String userId : userIds) {
            User user = requireUser(userId);
            results.addAll(deliver(user, publisher, type, baseContent, channels));
        }

        return results;
    }

    public List<Notification> getNotificationHistory() {
        return new ArrayList<>(notificationHistory);
    }

    private List<Notification> deliver(User user, Publisher publisher, NotificationTypeEnum type,
                                       String baseContent, List<NotificationChannelEnum> channels) {
        NotificationType notificationType = typeFactory.getType(type);
        String content = notificationType.getContent(baseContent, user);
        List<Notification> results = new ArrayList<>();

        for (NotificationChannelEnum channelType : channels) {
            Notification notification = new Notification(type, content, user, channelType, publisher);
            NotificationChannel channel = channelFactory.getChannel(channelType);

            if (!canSend(user, type, channelType)) {
                notification.setStatus(NotificationStatus.SKIPPED_OPT_OUT);
                notificationHistory.add(notification);
                results.add(notification);
                continue;
            }

            if (!channel.canDeliver(user)) {
                notification.setStatus(NotificationStatus.SKIPPED_NO_CONTACT);
                notificationHistory.add(notification);
                results.add(notification);
                continue;
            }

            try {
                channel.sendNotification(user, notification);
                notification.setStatus(NotificationStatus.SENT);
            } catch (RuntimeException ex) {
                notification.setStatus(NotificationStatus.FAILED);
            }

            notificationHistory.add(notification);
            results.add(notification);
        }

        return results;
    }

    private User requireUser(String userId) {
        User user = userMap.get(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return user;
    }

    private Publisher requirePublisher(String publisherId) {
        Publisher publisher = publisherMap.get(publisherId);
        if (publisher == null) {
            throw new IllegalArgumentException("Publisher not found: " + publisherId);
        }
        return publisher;
    }
}
