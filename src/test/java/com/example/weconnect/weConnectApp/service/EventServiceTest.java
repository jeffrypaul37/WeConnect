package com.example.weconnect.weConnectApp.service;

import com.webapp.weconnect.service.EventService;

import com.webapp.weconnect.model.Event;
import com.webapp.weconnect.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private MultipartFile eventImage;

    @InjectMocks
    private EventService eventService;

    @Captor
    private ArgumentCaptor<Event> eventCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createEvent_shouldSaveEventCorrectly() throws IOException {
        // Arrange
        String eventTitle = "Sample Event";
        String eventContent = "This is a sample event content.";
        String communityName = "Sample Community";
        String eventLocation = "Sample Location";
        Date eventDate = new Date();
        String eventTime = "10:00 AM";
        int maxCapacity = 50;
        byte[] imageContent = "imageContent".getBytes();
        when(eventImage.getBytes()).thenReturn(imageContent);

        // Act
        eventService.createEvent(eventTitle, eventContent, eventImage, communityName, eventLocation, eventDate, eventTime, maxCapacity);

        // Assert
        verify(eventRepository).save(eventCaptor.capture());
        Event savedEvent = eventCaptor.getValue();

        assertEquals(eventTitle, savedEvent.getTitle());
    }

    @Test
    public void getEventImageById_shouldReturnImage() {
        // Arrange
        Long eventId = 1L;
        byte[] imageContent = "imageContent".getBytes();
        Event event = new Event();
        event.setEventImage(imageContent);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // Act
        byte[] retrievedImage = eventService.getEventImageById(eventId);

        // Assert
        assertArrayEquals(imageContent, retrievedImage);
    }

    @Test
    public void getAllEvents_shouldReturnAllEvents() {
        // Arrange
        Event event1 = new Event();
        Event event2 = new Event();
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        // Act
        List<Event> events = eventService.getAllEvents();

        // Assert
        assertEquals(2, events.size());
    }
}
