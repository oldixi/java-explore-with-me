package ru.practicum.category;

import org.springframework.data.domain.Page;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static Category toCategory(NewCategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public static Category toCategory(Category category, CategoryDto categoryDto) {
        return Category.builder()
                .id(category.getId())
                .name(categoryDto.getName())
                .build();
    }

    public static Category toCategory(int categoryId, CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryId)
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static List<CategoryDto> toCategoryDto(Page<Category> category) {
        return category.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
}