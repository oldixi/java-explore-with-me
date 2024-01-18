package ru.practicum.place.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.place.PlaceService;
import ru.practicum.place.dto.NewPlaceDto;
import ru.practicum.place.dto.PlaceDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Transactional
@RequestMapping(path = "/admin/places")
public class PlaceAdminController {
    private final PlaceService placeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlaceDto createPlace(@Valid @RequestBody NewPlaceDto placeDto) {
        log.info("Request for create location {} ", placeDto.getName());
        return placeService.createPlace(placeDto);
    }

    @PatchMapping("/{placeId}")
    public PlaceDto updatePlace(@Positive @PathVariable int placeId, @Valid @RequestBody PlaceDto placeDto) {
        log.info("Request for update location {} ", placeId);
        return placeService.updatePlace(placeId, placeDto);
    }

    @DeleteMapping("/{placeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlace(@Positive @PathVariable int placeId) {
        log.info("Request for delete location {} ", placeId);
        placeService.deletePlace(placeId);
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<PlaceDto> getPlaces(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                    @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Request for get locations from={} size={}", from, size);
        return placeService.getPlaces(from, size);
    }

    @GetMapping("/{placeId}")
    @Transactional(readOnly = true)
    public PlaceDto getPlaceById(@Positive @PathVariable int placeId) {
        log.info("Request for get location {}", placeId);
        return placeService.getPlaceById(placeId);
    }
}