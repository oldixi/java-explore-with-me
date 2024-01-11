package ru.practicum.category;

import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

@Service
public interface CategoryService {
    CategoryDto createCategory(NewCategoryDto categoryDto);

    CategoryDto updateCategory(int categoryId, CategoryDto categoryDto);

    void deleteCategory(int categoryId);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(int categoryId);
}