package ru.practicum.place;

import org.springframework.data.domain.Page;
import ru.practicum.place.dto.NewPlaceDto;
import ru.practicum.place.dto.PlaceDto;

import java.util.List;
import java.util.stream.Collectors;

public class PlaceMapper {
    public static Place toPlace(NewPlaceDto placeDto) {
        return Place.builder()
                .name(placeDto.getName())
                .lat(placeDto.getLat())
                .lon(placeDto.getLon())
                .radius(placeDto.getRadius())
                .build();
    }

    public static Place toPlace(Place place, PlaceDto placeDto) {
        return Place.builder()
                .id(place.getId())
                .name(placeDto.getName() == null ? place.getName() : placeDto.getName())
                .lat(placeDto.getLat() == null ? place.getLat() : placeDto.getLat())
                .lon(placeDto.getLon() == null ? place.getLon() : placeDto.getLon())
                .radius(placeDto.getRadius() == null ? place.getRadius() : placeDto.getRadius())
                .build();
    }

    public static Place toPlace(int placeId, PlaceDto placeDto) {
        return Place.builder()
                .id(placeId)
                .name(placeDto.getName())
                .lat(placeDto.getLat())
                .lon(placeDto.getLon())
                .radius(placeDto.getRadius())
                .build();
    }

    public static PlaceDto toPlaceDto(Place place) {
        return PlaceDto.builder()
                .id(place.getId())
                .name(place.getName())
                .lat(place.getLat())
                .lon(place.getLon())
                .radius(place.getRadius())
                .build();
    }

    public static List<PlaceDto> toPlaceDto(Page<Place> place) {
        return place.stream()
                .map(PlaceMapper::toPlaceDto)
                .collect(Collectors.toList());
    }
}