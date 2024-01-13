package ru.practicum.request;

import ru.practicum.DateFormatter;
import ru.practicum.event.Event;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {
    public static Request toRequest(ParticipationRequestDto requestDto,
                                    User user,
                                    Event event,
                                    LocalDateTime created) {
        return Request.builder()
                .created(created)
                .event(event)
                .user(user)
                .status(requestDto.getStatus())
                .build();
    }

    public static ParticipationRequestDto toRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(DateFormatter.toString(request.getCreated()))
                .event(request.getEvent().getId())
                .requester(request.getUser().getId())
                .status(request.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> toRequestDto(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }
}