package ru.practicum.request;

import org.springframework.stereotype.Service;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

@Service
public interface RequestService {
    ParticipationRequestDto createRequest(int userId, int eventId);

    ParticipationRequestDto cancelRequest(int userId, int requestId);

    List<ParticipationRequestDto> getRequests(int userId);

    List<ParticipationRequestDto> findByUserIdAndEventId(int userId, int eventId);

    EventRequestStatusUpdateResult updateEventRequests(int userId, int eventId, EventRequestStatusUpdateRequest requests);
}