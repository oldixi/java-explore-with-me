package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.CategoryService;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        log.info("Request for create category {} ", categoryDto.getName());
        return categoryService.createCategory(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@Positive @PathVariable int catId, @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Request for update category {} ", catId);
        return categoryService.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@Positive @PathVariable int catId) {
        log.info("Request for delete category {} ", catId);
        categoryService.deleteCategory(catId);
    }
}