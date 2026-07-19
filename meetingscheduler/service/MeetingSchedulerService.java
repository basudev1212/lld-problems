package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import models.Calendar;
import models.Meeting;
import models.MeetingRoom;
import models.User;

public class MeetingSchedulerService {
    private final Map<String, User> userMap;
    private final Map<String, MeetingRoom> meetingRoomMap;
    private final Map<String, Meeting> meetingMap;
    private final MeetingRoomBookingService meetingRoomBookingService;

    public MeetingSchedulerService() {
        this.userMap = new ConcurrentHashMap<>();
        this.meetingRoomMap = new ConcurrentHashMap<>();
        this.meetingMap = new ConcurrentHashMap<>();
        this.meetingRoomBookingService = new MeetingRoomBookingService();
    }

    public void registerUser(String id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);

        Calendar calendar = new Calendar();
        calendar.setId("cal-" + id);
        calendar.setMeetings(new ArrayList<>());
        user.setCalendar(calendar);

        userMap.put(id, user);
    }

    public void addMeetingRoom(String id, String name, Integer capacity) {
        MeetingRoom room = new MeetingRoom();
        room.setId(id);
        room.setName(name);
        room.setCapacity(capacity);

        Calendar calendar = new Calendar();
        calendar.setId("cal-" + id);
        calendar.setMeetings(new ArrayList<>());
        room.setCalendar(calendar);

        meetingRoomMap.put(id, room);
    }

    public List<MeetingRoom> getAllMeetingRooms(Integer startTime, Integer endTime) {
        List<MeetingRoom> availableMeetingRooms = new ArrayList<>();
        for(MeetingRoom meetingRoom : meetingRoomMap.values()){
            if(meetingRoomBookingService.isRoomAvailable(meetingRoom, startTime, endTime)){
                availableMeetingRooms.add(meetingRoom);
            }
        }
        return availableMeetingRooms;
    }

    public Meeting bookMeetingRoom(String userId, String roomId, String title,
                                   Integer startTime, Integer endTime) {
        User user = userMap.get(userId);
        MeetingRoom room = meetingRoomMap.get(roomId);

        if (room == null) {
            return null;
        }

        Meeting meeting = new Meeting();
        meeting.setId(UUID.randomUUID().toString());
        meeting.setTitle(title);
        meeting.setStartTime(startTime);
        meeting.setEndTime(endTime);
        meeting.setMeetingRoom(room);
        meeting.setOrganizer(user);

        Meeting bookedMeeting = meetingRoomBookingService.bookRoom(room, user, meeting);
        if (bookedMeeting != null) {
            meetingMap.put(bookedMeeting.getId(), bookedMeeting);
        }

        return bookedMeeting;
    }

    public List<Meeting> getAllMeetings() {
        return new ArrayList<>(meetingMap.values());
    }

    public MeetingRoomBookingService getMeetingRoomBookingService() {
        return meetingRoomBookingService;
    }
}
