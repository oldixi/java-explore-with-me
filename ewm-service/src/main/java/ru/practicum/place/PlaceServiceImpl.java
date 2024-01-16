package ru.practicum.place;

import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.place.dto.NewPlaceDto;
import ru.practicum.place.dto.PlaceDto;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;

    @Override
    public PlaceDto createPlace(NewPlaceDto placeDto) {
        return PlaceMapper.toPlaceDto(placeRepository.save(PlaceMapper.toPlace(placeDto)));
    }

    @Override
    public PlaceDto updatePlace(int placeId, PlaceDto placeDto) {
        return PlaceMapper.toPlaceDto(placeRepository.save(PlaceMapper.toPlace(placeRepository.findById(placeId)
                        .orElseThrow(() -> new ObjectNotFoundException(placeId,
                                "Category with id " + placeId + " was not found")),
                placeDto)));
    }

    @Override
    public void deletePlace(int placeId) {
        placeRepository.findById(placeId)
                .orElseThrow(() -> new ObjectNotFoundException(placeId,
                        "Location with id " + placeId + " was not found"));

        placeRepository.deleteById(placeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlaceDto> getPlaces(int from, int size) {
        final Pageable page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return PlaceMapper.toPlaceDto(placeRepository.findAll(page));
    }

    @Override
    @Transactional(readOnly = true)
    public PlaceDto getPlaceById(int placeId) {
        return PlaceMapper.toPlaceDto(placeRepository.findById(placeId)
                .orElseThrow(() -> new ObjectNotFoundException(placeId,
                        "Location with id " + placeId + " was not found")));
    }
}