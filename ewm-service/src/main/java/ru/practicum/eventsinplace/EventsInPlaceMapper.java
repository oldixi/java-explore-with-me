package ru.practicum.eventsinplace;

import ru.practicum.event.EventMapper;
import ru.practicum.event.dto.EventFullDto;

import java.util.List;
import java.util.stream.Collectors;

public class EventsInPlaceMapper {
    public static EventFullDto toEventFullDto(EventsInPlace event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.isPaid())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .participantLimit(event.getParticipantLimit())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .initiator(event.getInitiator())
                .category(event.getCategory())
                .location(event.getLocation())
                .build();
    }

    public static List<EventFullDto> toEventFullDto(List<EventsInPlace> events) {
        return events.stream()
                .map(EventsInPlaceMapper::toEventFullDto)
                .collect(Collectors.toList());
    }
}