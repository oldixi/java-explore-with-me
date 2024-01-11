package ru.practicum.compilation;

import org.springframework.data.domain.Page;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.event.EventMapper;
import ru.practicum.event.dto.EventShortDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto compilationDto) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned() != null && compilationDto.getPinned())
                .build();
    }

    public static Compilation toCompilation(UpdateCompilationRequest compilationDto,
                                            Compilation compilation) {
        return Compilation.builder()
                .title(compilationDto.getTitle() == null ? compilation.getTitle() : compilationDto.getTitle())
                .pinned(compilationDto.getPinned() == null ? compilation.isPinned() : compilationDto.getPinned())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        Set<EventShortDto> eventsDto = new HashSet<>();
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            eventsDto = compilation.getEvents().stream()
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toSet());
        }
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .events(eventsDto)
                .build();
    }

    public static List<CompilationDto> toCompilationDto(Page<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }
}