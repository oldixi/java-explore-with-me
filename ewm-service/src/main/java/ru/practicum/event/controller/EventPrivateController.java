package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.request.RequestService;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Transactional
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class EventPrivateController {
    private final EventService eventService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@Positive @PathVariable int userId, @Valid @RequestBody NewEventDto eventDto) {
        log.info("Request for create event {} from user {}", eventDto.getTitle(), userId);
        return eventService.createEvent(userId, eventDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateUserEvent(@Positive @PathVariable int userId,
                                        @Positive @PathVariable int eventId,
                                        @Valid @RequestBody UpdateEventUserRequest eventDto) {
        log.info("Request for update event {} from user {}", eventId, userId);
        return eventService.updateUserEvent(userId, eventId, eventDto);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequests(@Positive @PathVariable int userId,
                                                              @Positive @PathVariable int eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest requests) {
        log.info("Request for update event {} requests from user {}", eventId, userId);
        return requestService.updateEventRequests(userId, eventId, requests);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<EventShortDto> findByUserId(@Positive @PathVariable int userId,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                            @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Request from get events from user {} from={} size={}", userId, from, size);
        return eventService.findByUserId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @Transactional(readOnly = true)
    public EventFullDto getEventByIdAndUserId(@Positive @PathVariable int userId, @Positive @PathVariable int eventId) {
        log.info("Request from user {} for get event {}", userId, eventId);
        return eventService.findByIdAndUserId(eventId, userId);
    }

    @GetMapping("/{eventId}/requests")
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequests(@Positive @PathVariable int userId,
                                                     @Positive @PathVariable int eventId) {
        log.info("Request from user {} for get event {} requests", userId, eventId);
        return requestService.findByUserIdAndEventId(userId, eventId);
    }
}