package model;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import enums.NotificationChannelEnum;
import enums.NotificationTypeEnum;

public class NotificationPreference {

    private final Set<NotificationTypeEnum> allChannelOptOuts;
    private final Map<NotificationTypeEnum, Set<NotificationChannelEnum>> channelOptOuts;

    public NotificationPreference() {
        this.allChannelOptOuts = EnumSet.noneOf(NotificationTypeEnum.class);
        this.channelOptOuts = new EnumMap<>(NotificationTypeEnum.class);
    }

    public void optOut(NotificationTypeEnum type) {
        allChannelOptOuts.add(type);
        channelOptOuts.remove(type);
    }

    public void optOut(NotificationTypeEnum type, NotificationChannelEnum channel) {
        if (allChannelOptOuts.contains(type)) {
            return;
        }
        channelOptOuts.computeIfAbsent(type, ignored -> EnumSet.noneOf(NotificationChannelEnum.class))
                .add(channel);
    }

    public void optIn(NotificationTypeEnum type) {
        allChannelOptOuts.remove(type);
        channelOptOuts.remove(type);
    }

    public void optIn(NotificationTypeEnum type, NotificationChannelEnum channel) {
        Set<NotificationChannelEnum> channels = channelOptOuts.get(type);
        if (channels == null) {
            return;
        }
        channels.remove(channel);
        if (channels.isEmpty()) {
            channelOptOuts.remove(type);
        }
    }

    public boolean hasOptedOut(NotificationTypeEnum type, NotificationChannelEnum channel) {
        if (allChannelOptOuts.contains(type)) {
            return true;
        }
        Set<NotificationChannelEnum> channels = channelOptOuts.get(type);
        return channels != null && channels.contains(channel);
    }
}
