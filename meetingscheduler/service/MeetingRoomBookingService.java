package service;

import java.util.List;
import javax.crypto.NullCipher;

import models.Calendar;
import models.Meeting;
import models.MeetingRoom;
import models.User;

public class MeetingRoomBookingService {

    public Meeting bookRoom(MeetingRoom room, User user, Meeting meeting) {
        synchronized (room) {
            if (!isRoomAvailable(room, meeting.getStartTime(), meeting.getEndTime())) {
                System.out.println("Room " + room.getName() + " is not available for this slot.");
                return null;
            }

            if (user != null && !isUserAvailable(user, meeting.getStartTime(), meeting.getEndTime())) {
                System.out.println("User " + user.getName() + " already has a meeting in this slot.");
                return null;
            }

            room.getCalendar().getMeetings().add(meeting);

            if (user != null) {
                user.getCalendar().getMeetings().add(meeting);
            }

            System.out.println("Booked room " + room.getName() + " for " + meeting.getTitle());
            return meeting;
        }
    }

    public boolean isRoomAvailable(MeetingRoom meetingRoom, Integer startTime, Integer endTime) {
        Calendar meetingRoomCalendar = meetingRoom.getCalendar();
        if(meetingRoomCalendar == null || meetingRoomCalendar.getMeetings() == null)
            return true;
        for(Meeting meeting : meetingRoomCalendar.getMeetings()){
            Integer currentMeetingStartTime = meeting.getStartTime();
            Integer currentMeetingEndTime = meeting.getEndTime();
            if(hasTimeOverlap(startTime, endTime, currentMeetingStartTime, currentMeetingEndTime))
                return false;
        }
        return true;
    }

    public boolean isUserAvailable(User user, Integer startTime, Integer endTime) {
        if (user == null || user.getCalendar() == null || user.getCalendar().getMeetings() == null) {
            return true;
        }

        List<Meeting> meetings = user.getCalendar().getMeetings();
        for (int i = 0; i < meetings.size(); i++) {
            Meeting meeting = meetings.get(i);
            if (hasTimeOverlap(meeting.getStartTime(), meeting.getEndTime(), startTime, endTime)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasTimeOverlap(Integer start1, Integer end1, Integer start2, Integer end2) {
        return start1 < end2 && end1 > start2;
    }
}
