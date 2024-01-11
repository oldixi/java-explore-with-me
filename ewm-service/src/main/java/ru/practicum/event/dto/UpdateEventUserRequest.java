package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.EventStateActionUser;
import ru.practicum.location.LocationDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {
    @Length(max = 120, min = 3)
    private String title;

    @Length(max = 2000, min = 20)
    private String annotation;

    @Length(max = 7000, min = 20)
    private String description;

    private Integer category;

    private String eventDate;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private LocationDto location;

    private EventStateActionUser stateAction;
}