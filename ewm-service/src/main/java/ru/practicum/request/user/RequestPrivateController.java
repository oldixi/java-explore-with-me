package ru.practicum.request.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.request.RequestService;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class RequestPrivateController {
    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@Positive @PathVariable int userId, @Positive @RequestParam int eventId) {
        log.info("Request from user {} to participate in event {}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@Positive @PathVariable int userId, @Positive @PathVariable int requestId) {
        log.info("Request from cancelling request {} from user {}", requestId, userId);
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequests(@Positive @PathVariable int userId) {
        log.info("Request from get requests from user {}", userId);
        return requestService.getRequests(userId);
    }
}