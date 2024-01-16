package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DateFormatter;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryMapper;
import ru.practicum.category.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.exception.InvalidEventStateOrDate;
import ru.practicum.exception.InvalidPathVariableException;
import ru.practicum.location.Location;
import ru.practicum.location.LocationDto;
import ru.practicum.location.LocationMapper;
import ru.practicum.location.LocationRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    @Override
    public EventFullDto findByIdAndState(int eventId) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event with id " + eventId + " was not found"));

        event.setViews(event.getViews() + 1);
        eventRepository.save(event);

        return EventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findPublicEvents(String text,
                                                Integer[] categories,
                                                Boolean paid,
                                                String rangeStart,
                                                String rangeEnd,
                                                boolean onlyAvailable,
                                                String sort,
                                                int from,
                                                int size) {
        Sort sortBy;
        if (sort.equalsIgnoreCase("EVENT_DATE")) {
            sortBy = Sort.by(Sort.Direction.DESC, "eventDate");
        } else if (sort.equalsIgnoreCase("VIEWS")) {
            sortBy = Sort.by(Sort.Direction.DESC, "views");
        } else {
            throw new InvalidPathVariableException("Event must be published");
        }
        Pageable page = PageRequest.of(from, size, sortBy);

        if (categories != null) {
            List<Integer> categoriesInList = new ArrayList<>(Arrays.asList(categories));
            if (categoriesInList.stream().anyMatch((category -> category <= 0))) {
                throw new InvalidPathVariableException("Invalid value of parameter categories");
            }
        }

        return EventMapper.toEventShortDto(eventRepository.findPublicEvents(text, paid, rangeStart, rangeEnd,
                onlyAvailable, categories == null ? null : Arrays.asList(categories), page));
    }

    @Override
    public EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest eventDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event with id " + eventId + " was not found"));

        if (eventDto.getEventDate() != null
                && LocalDateTime.now().plusHours(1).isAfter(DateFormatter.toLocalDateTime(eventDto.getEventDate()))) {
            throw new InvalidPathVariableException("Event date is too late");
        }

        if (!event.getState().equals(EventState.PENDING) && !eventDto.getStateAction().toString().isBlank()) {
            throw new InvalidEventStateOrDate("Cannot publish the event because it's not in the right state: PENDING");
        }

        EventStateActionAdmin eventStateActionUser = eventDto.getStateAction();
        if (eventStateActionUser != null) {
            if (eventStateActionUser.equals(EventStateActionAdmin.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
            } else if (eventStateActionUser.equals(EventStateActionAdmin.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }

        event.setPublishedOn(LocalDateTime.now());

        Integer catId = eventDto.getCategory();
        Category category = new Category();
        if (catId != null) {
            category = CategoryMapper.toCategory(catId, categoryService.getCategoryById(catId));
        }

        LocationDto locationDto = eventDto.getLocation();
        Location location = new Location();
        if (locationDto != null) {
            location = locationRepository.save(LocationMapper.toLocation(eventDto.getLocation()));
        }

        return EventMapper.toEventFullDto(eventRepository
                .save(EventMapper.toEvent(eventDto, event, category, location, event.getUser())));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findAdminEvents(Integer[] users,
                                              String[] states,
                                              Integer[] categories,
                                              String rangeStart,
                                              String rangeEnd,
                                              int from,
                                              int size) {
        Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));

        if (categories != null) {
            List<Integer> categoriesInList = new ArrayList<>(Arrays.asList(categories));
            if (categoriesInList.stream().anyMatch((category -> category <= 0))) {
                throw new InvalidPathVariableException("Invalid value of parameter categories");
            }
        }

        if (users != null) {
            List<Integer> usersInList = new ArrayList<>(Arrays.asList(users));
            if (usersInList.stream().anyMatch((user -> user <= 0))) {
                throw new InvalidPathVariableException("Invalid value of parameter users");
            }
        }

        return EventMapper.toEventFullDto(eventRepository
                .findAdminEvents(users == null ? null : Arrays.asList(users),
                        states == null ? null : Arrays.asList(states),
                        categories == null ? null : Arrays.asList(categories),
                        rangeStart, rangeEnd, page));
    }

    @Override
    public EventFullDto createEvent(int userId, NewEventDto eventDto) {
        LocalDateTime now = LocalDateTime.now();

        if (now.plusHours(2).isAfter(DateFormatter.toLocalDateTime(eventDto.getEventDate()))) {
            throw new InvalidPathVariableException("Field: eventDate. Error: должно содержать дату, " +
                    "которая еще не наступила. Value: " + eventDto.getEventDate());
        }

        User user = userService.getUserById(userId);

        int catId = eventDto.getCategory();
        Category category = CategoryMapper.toCategory(catId, categoryService.getCategoryById(catId));

        Location location = LocationMapper.toLocation(eventDto.getLocation());
        if (location != null) {
            locationRepository.save(location);
        }

        Event event = EventMapper.toEvent(eventDto, category, location);
        event.setState(EventState.PENDING);
        event.setUser(user);
        event.setCreatedOn(now);

        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> findByUserId(int userId, int from, int size) {
        Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return EventMapper.toEventShortDto(eventRepository.findByUserId(userId, page));
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto findByIdAndUserId(int eventId, int userId) {
        return EventMapper.toEventFullDto(eventRepository.findByIdAndUserId(eventId, userId)
                .orElseThrow(() -> new ObjectNotFoundException(eventId,
                        "Event with id " + eventId + ", registered by user " + userId + " was not found")));
    }

    @Override
    public EventFullDto updateUserEvent(int userId, int eventId, UpdateEventUserRequest eventDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event with id " + eventId + " was not found"));

        if (eventDto.getEventDate() != null
                && LocalDateTime.now().plusHours(2).isAfter(DateFormatter.toLocalDateTime(eventDto.getEventDate()))) {
            throw new InvalidPathVariableException("Event date is too late");
        }

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new InvalidEventStateOrDate("Only pending or canceled events can be changed");
        }

        User user = userService.getUserById(userId);

        Integer catId = eventDto.getCategory();
        Category category = new Category();
        if (catId != null) {
            category = CategoryMapper.toCategory(catId, categoryService.getCategoryById(catId));
        }

        LocationDto locationDto = eventDto.getLocation();
        Location location = new Location();
        if (locationDto != null) {
            location = locationRepository.save(LocationMapper.toLocation(eventDto.getLocation()));
        }

        EventStateActionUser eventStateActionUser = eventDto.getStateAction();
        if (eventStateActionUser != null) {
            if (eventStateActionUser.equals(EventStateActionUser.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else if (eventStateActionUser.equals(EventStateActionUser.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            }
        }

        return EventMapper.toEventFullDto(eventRepository
                .save(EventMapper.toEvent(eventDto, event, category, location, user)));
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event with id " + eventId + " was not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findEventsByPlaceId(int placeId, int from, int size) {
        Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return EventMapper.toEventFullDto(eventRepository.findEventsByPlaceId(placeId, page));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> findEventsByPlaceName(String placeName, int from, int size) {
        Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return EventMapper.toEventFullDto(eventRepository.findEventsByPlaceName(placeName, page));
    }
}