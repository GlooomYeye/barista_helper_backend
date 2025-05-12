package my.cousework.barista_helper.api.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.api.dto.RecipeDetailsDto;
import my.cousework.barista_helper.api.dto.RecipePreviewDto;
import my.cousework.barista_helper.api.mappers.RecipeMapper;
import my.cousework.barista_helper.api.services.RecipeService;
import my.cousework.barista_helper.store.entities.RecipeEntity;
import my.cousework.barista_helper.store.enums.BrewingMethod;




@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
@Validated
@RequestMapping("/api/recipes")
public class RecipeController {
    
    RecipeService recipeService;
    RecipeMapper recipeMapper;
    @GetMapping()
    public Page<RecipePreviewDto> getRecipes(
            @RequestParam(name = "method", required = false) BrewingMethod method,
            @RequestParam(name = "search", required = false) String searchQuery,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "perPage") int perPage,
            @RequestParam(defaultValue = "id", name = "sortBy") String sortBy,
            @RequestParam(defaultValue = "asc", name = "sortDir") String sortDir) {
        
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page - 1, perPage, sort);
        
        Page<RecipeEntity> recipes;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            recipes = recipeService.searchByName(searchQuery, pageable);
        } else if (method != null) {
            recipes = recipeService.getByBrewingMethod(method, pageable);
        } else {
            recipes = recipeService.getAll(pageable);
        }
        
        return recipeMapper.toPreviewDto(recipes);
    }
    @GetMapping("/{id}")
    public RecipeDetailsDto getDetailsById(@PathVariable("id") Long id) {
        RecipeEntity recipe = recipeService.getById(id);
        return recipeMapper.toDto(recipe);
    }
}
