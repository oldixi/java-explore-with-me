package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.Event;
import ru.practicum.event.EventRepository;
import ru.practicum.event.EventService;
import ru.practicum.event.EventState;
import ru.practicum.exception.InvalidEventStateOrDate;
import ru.practicum.exception.InvalidParticipationRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.user.User;
import ru.practicum.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    private final RequestRepository requestRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final EventService eventService;
    private final EventRepository eventRepository;

    @Override
    public ParticipationRequestDto createRequest(int userId, int eventId) {
        if (eventRepository.findByIdAndUserId(eventId, userId).isPresent()) {
            throw new InvalidParticipationRequest("Wrong request for participation in event " +
                    eventId + " from initiator " + userId);
        }

        Event event = eventService.getEventById(eventId);

        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= event.getConfirmedRequests()
                || !event.getState().equals(EventState.PUBLISHED)
                || requestRepository.findByUserIdAndEventId(userId, eventId).size() > 0) {
            throw new InvalidParticipationRequest("Wrong request for participation in event " +
                    eventId + " from user " + userId);
        }

        User user = userService.getUserById(userId);

        RequestStatus status = RequestStatus.PENDING;
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        ParticipationRequestDto request = ParticipationRequestDto.builder()
                .status(status)
                .build();

        return RequestMapper.toRequestDto(requestRepository
                .save(RequestMapper.toRequest(request, user, event, LocalDateTime.now())));
    }

    @Override
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException(requestId, "Request with id " + requestId + " was not found"));

        if (requestRepository.findByUserId(userId).size() == 0) {
            throw new ObjectNotFoundException(requestId, "User " + userId + " have no access to request " + requestId);
        }
        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequests(int userId) {
        return RequestMapper.toRequestDto(requestRepository.findByUserId(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findByUserIdAndEventId(int userId, int eventId) {
        return RequestMapper.toRequestDto(requestRepository.findByEventIdAndEventUserId(eventId, userId));
    }

    @Override
    public EventRequestStatusUpdateResult updateEventRequests(int userId,
                                                              int eventId,
                                                              EventRequestStatusUpdateRequest requests) {
        boolean isNeedAllToConfirm = false;
        boolean isNeedAllToCancel = false;

        Event event = eventService.getEventById(eventId);

        if (event.getParticipantLimit() > 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
            throw new InvalidEventStateOrDate("The participant limit has been reached");
        }

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            isNeedAllToConfirm = true;
        }

        for (int requestId : requests.getRequestIds()) {
            Request request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new ObjectNotFoundException(requestId, "Request with id " + requestId + " was not found"));

            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new InvalidParticipationRequest("Request must have status PENDING");
            }

            if (isNeedAllToCancel) {
                request.setStatus(RequestStatus.CANCELED);
            } else {
                if (requests.getStatus().equals(RequestStatus.CONFIRMED)) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    event = eventRepository.save(event);
                    if (!isNeedAllToConfirm && event.getParticipantLimit() <= event.getConfirmedRequests()) {
                        isNeedAllToCancel = true;
                    }
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                }
            }
            requestRepository.save(request);
        }

        List<ParticipationRequestDto> confirmedRequests = RequestMapper.toRequestDto(requestRepository
                .findByEventIdAndStatus(eventId, RequestStatus.CONFIRMED));
        List<ParticipationRequestDto> rejectedRequests = RequestMapper.toRequestDto(requestRepository
                .findByEventIdAndStatus(eventId, RequestStatus.REJECTED));

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }
}