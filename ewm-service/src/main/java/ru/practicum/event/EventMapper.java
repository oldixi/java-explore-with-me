package ru.practicum.event;

import org.springframework.data.domain.Page;
import ru.practicum.DateFormatter;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryMapper;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.location.Location;
import ru.practicum.location.LocationMapper;
import ru.practicum.user.User;
import ru.practicum.user.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {
    public static Event toEvent(NewEventDto eventDto, Category category, Location location) {
        return Event.builder()
                .title(eventDto.getTitle())
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .eventDate(DateFormatter.toLocalDateTime(eventDto.getEventDate()))
                .participantLimit(eventDto.getParticipantLimit() == null ? 0 : eventDto.getParticipantLimit())
                .paid(eventDto.getPaid() != null && eventDto.getPaid())
                .requestModeration(eventDto.getRequestModeration() == null || eventDto.getRequestModeration())
                .category(category)
                .location(location)
                .build();
    }

    public static Event toEvent(UpdateEventUserRequest eventDto,
                                Event event,
                                Category category,
                                Location location,
                                User user) {
        return Event.builder()
                .id(event.getId())
                .title(eventDto.getTitle() == null ? event.getTitle() : eventDto.getTitle())
                .annotation(eventDto.getAnnotation() == null ? event.getAnnotation() : eventDto.getAnnotation())
                .description(eventDto.getDescription() == null ? event.getDescription() : eventDto.getDescription())
                .eventDate(eventDto.getEventDate() == null ?
                        event.getEventDate() : DateFormatter.toLocalDateTime(eventDto.getEventDate()))
                .participantLimit(eventDto.getParticipantLimit() == null ?
                        event.getParticipantLimit() : eventDto.getParticipantLimit())
                .paid(eventDto.getPaid() == null ? event.isPaid() : eventDto.getPaid())
                .requestModeration(eventDto.getRequestModeration() == null ?
                        event.isRequestModeration() : eventDto.getRequestModeration())
                .category(eventDto.getCategory() == null ? event.getCategory() : category)
                .location(eventDto.getLocation() == null ? event.getLocation() : location)
                .user(event.getUser() == null ? user : event.getUser())
                .state(event.getState())
                .build();
    }

    public static Event toEvent(UpdateEventAdminRequest eventDto,
                                Event event,
                                Category category,
                                Location location,
                                User user) {
        return Event.builder()
                .id(event.getId())
                .title(eventDto.getTitle() == null ? event.getTitle() : eventDto.getTitle())
                .annotation(eventDto.getAnnotation() == null ? event.getAnnotation() : eventDto.getAnnotation())
                .description(eventDto.getDescription() == null ? event.getDescription() : eventDto.getDescription())
                .eventDate(eventDto.getEventDate() == null ?
                        event.getEventDate() : DateFormatter.toLocalDateTime(eventDto.getEventDate()))
                .participantLimit(eventDto.getParticipantLimit() == null ?
                        event.getParticipantLimit() : eventDto.getParticipantLimit())
                .paid(eventDto.getPaid() == null ? event.isPaid() : eventDto.getPaid())
                .requestModeration(eventDto.getRequestModeration() == null ?
                        event.isRequestModeration() : eventDto.getRequestModeration())
                .category(eventDto.getCategory() == null ? event.getCategory() : category)
                .location(eventDto.getLocation() == null ? event.getLocation() : location)
                .user(event.getUser() == null ? user : event.getUser())
                .state(event.getState())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(DateFormatter.toString(event.getEventDate()))
                .paid(event.isPaid())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .participantLimit(event.getParticipantLimit())
                .createdOn(event.getCreatedOn() == null ? null : DateFormatter.toString(event.getCreatedOn()))
                .publishedOn(event.getPublishedOn() == null ? null : DateFormatter.toString(event.getPublishedOn()))
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .initiator(event.getUser() == null ? null : UserMapper.toUserShortDto(event.getUser()))
                .category(event.getCategory() == null ? null : CategoryMapper.toCategoryDto(event.getCategory()))
                .location(event.getLocation() == null ? null : LocationMapper.toLocationDto(event.getLocation()))
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(DateFormatter.toString(event.getEventDate()))
                .paid(event.isPaid())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .initiator(event.getUser() == null ? null : UserMapper.toUserShortDto(event.getUser()))
                .category(event.getCategory() == null ? null : CategoryMapper.toCategoryDto(event.getCategory()))
                .build();
    }

    public static List<EventShortDto> toEventShortDto(Page<Event> events) {
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public static List<EventFullDto> toEventFullDto(Page<Event> events) {
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }
}