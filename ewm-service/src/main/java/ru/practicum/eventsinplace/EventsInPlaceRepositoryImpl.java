package ru.practicum.eventsinplace;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.EventState;
import ru.practicum.location.LocationDto;
import ru.practicum.user.dto.UserShortDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EventsInPlaceRepositoryImpl implements EventsInPlaceRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<EventsInPlace> findEventsByPlaceId(int placeId, int from, int size) {
        String sql = "select * from getEventsByPlaceId(" + placeId + ", " +
                from + ", " + size + ")";

        return jdbcTemplate.query(sql, this::mapper);
    }

    @Override
    public List<EventsInPlace> findEventsByPlaceName(String placeName, int from, int size) {
        String sql = "select * from getEventsByPlaceName('" + placeName + "', " +
                from + ", " + size + ")";

        return jdbcTemplate.query(sql, this::mapper);
    }

    private EventsInPlace mapper(ResultSet resultSet, int rowNum) throws SQLException {
        CategoryDto category = CategoryDto.builder()
                .id(resultSet.getInt("category_id"))
                .name(resultSet.getString("category_name"))
                .build();

        UserShortDto initiator = UserShortDto.builder()
                .id(resultSet.getInt("user_id"))
                .name(resultSet.getString("user_name"))
                .build();

        LocationDto location = LocationDto.builder()
                .lat(resultSet.getFloat("lat"))
                .lon(resultSet.getFloat("lon"))
                .build();

        return EventsInPlace.builder()
                .id(resultSet.getInt("id"))
                .title(resultSet.getString("title"))
                .annotation(resultSet.getString("annotation"))
                .description(resultSet.getString("description"))
                .eventDate(resultSet.getString("event_date"))
                .createdOn(resultSet.getString("created_on"))
                .publishedOn(resultSet.getString("published_on"))
                .paid(resultSet.getBoolean("paid"))
                .requestModeration(resultSet.getBoolean("request_moderation"))
                .confirmedRequests(resultSet.getInt("confirmed_requests"))
                .participantLimit(resultSet.getInt("participant_limit"))
                .views(resultSet.getInt("views"))
                .state(EventState.valueOf(resultSet.getString("state")))
                .category(category)
                .initiator(initiator)
                .location(location)
                .build();
    }
}