package com.webapp.weconnect.service;

import com.webapp.weconnect.model.CommunityImage;
import com.webapp.weconnect.model.Event;
import com.webapp.weconnect.model.Message;
import com.webapp.weconnect.repository.EventRepository;
import com.webapp.weconnect.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void createEvent(String eventTitle, String eventContent, MultipartFile eventImage, String communityname,
                            String eventLocation, Date eventDate, String eventTime, int maxCapacity) {
        Event event = new Event();
        event.setTitle(eventTitle);
        event.setContent(eventContent);
        event.setLocation(eventLocation);
        event.setEventDate(eventDate);
        event.setEventTime(eventTime);
        event.setMaxCapacity(maxCapacity);
        try {
            byte[] imageBytes = eventImage.getBytes();
            event.setEventImage(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        event.setCommunityname(communityname);
        eventRepository.save(event);
    }

    public byte[] getEventImageById( Long id) {
        Optional<Event> optionalMessage = eventRepository.findById(id);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get().getEventImage();
        }
        return null;
    }

    /**
     * Retrieve all the events which are currently active in the community
     * Because need to show all the events that are organized to its members for registration.
     * */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}
