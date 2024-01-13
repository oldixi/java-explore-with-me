package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.EndpointHitStatDto;
import ru.practicum.stats.exception.InvalidPathVariableException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class EndpointHitController {
    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit addHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Request for add hit {}", endpointHitDto.getUri());
        return endpointHitService.addHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<EndpointHitStatDto> getStates(@RequestParam(name = "start", required = true) String start,
                                              @RequestParam(name = "end", required = true) String end,
                                              @RequestParam(name = "uris", required = false) String[] uris,
                                              @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        if (start == null || end == null) {
                throw new InvalidPathVariableException("Date-parameters shouldn't be null");
        }
        if (unique) {
            log.info("Get statistic from {} till {} with uris={} for distinct ips", start, end, uris);
        } else {
            log.info("Get unique statistic from {} till {} with uris={}", start, end, uris);
        }
        return endpointHitService.getStates(start, end, uris, unique);
    }
}