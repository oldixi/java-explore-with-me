package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Transactional
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class EventAdminController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@Positive @PathVariable int eventId,
                                           @Valid @RequestBody UpdateEventAdminRequest eventDto) {
        log.info("Request for update event {} ", eventId);
        return eventService.updateEventByAdmin(eventId, eventDto);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<EventFullDto> findAdminEvents(@RequestParam(required = false) Integer[] users,
                                              @RequestParam(required = false) String[] states,
                                              @RequestParam(required = false) Integer[] categories,
                                              @RequestParam(required = false) String rangeStart,
                                              @RequestParam(required = false) String rangeEnd,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                              @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Request for get events users={} categories={} states={} rangeStart={} rangeEnd={} from={} size={}",
                users, categories, states, rangeStart, rangeEnd, from, size);
        return eventService.findAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @GetMapping("/places/{placeId}")
    @Transactional(readOnly = true)
    public List<EventFullDto> findEventsByPlaceId(@Positive @PathVariable int placeId,
                                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                  @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Request for get events in location with id {} from={} size={}", placeId, from, size);
        return eventService.findEventsByPlaceId(placeId, from, size);
    }

    @GetMapping("/places")
    @Transactional(readOnly = true)
    public List<EventFullDto> findEventsByPlaceName(@RequestParam String placeName,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                    @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Request for get events in location with name {} from={} size={}", placeName, from, size);
        return eventService.findEventsByPlaceName(placeName, from, size);
    }
}