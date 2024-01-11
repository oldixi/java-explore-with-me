package ru.practicum.compilation;

import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;

import java.util.List;

@Service
public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto compilation);

    CompilationDto updateCompilation(int compId, UpdateCompilationRequest compilation);

    void deleteCompilation(int compId);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilationById(int compId);
}