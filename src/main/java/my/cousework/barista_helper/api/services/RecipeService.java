package my.cousework.barista_helper.api.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.api.exceptions.NotFoundException;
import my.cousework.barista_helper.store.entities.RecipeEntity;
import my.cousework.barista_helper.store.entities.UserEntity;
import my.cousework.barista_helper.store.enums.BrewingMethod;
import my.cousework.barista_helper.store.repository.RecipeRepository;



@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RecipeService {
    RecipeRepository recipeRepository;
    UserService userService;
    @Transactional(readOnly = true)
    public RecipeEntity getById(Long id){
        return recipeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Recipe not found"));
    };
    @Transactional(readOnly = true)
    public Page<RecipeEntity> getByBrewingMethod(BrewingMethod brewingMethod, Pageable pageable){
        return recipeRepository.findByBrewingMethod(brewingMethod, pageable);
    };

    @Transactional(readOnly = true)
    public Page<RecipeEntity> searchByName(String name, Pageable pageable) {
        return recipeRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional(readOnly = true)
    public Page<RecipeEntity> searchFavoritesByName(Long userId, String searchQuery, Pageable pageable){
        return recipeRepository.findByLikedByUsers_IdAndNameContainingIgnoreCase(userId, searchQuery, pageable);
    }

    @Transactional(readOnly = true)
    public Page<RecipeEntity> getAll(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }
    @Transactional
    public RecipeEntity create(RecipeEntity recipe, Long userId){
        UserEntity user = userService.getById(userId);
        recipe.setAuthor(user);
        
        var steps = recipe.getSteps();
        recipe.setSteps(null);
        
        RecipeEntity savedRecipe = recipeRepository.save(recipe);
        
        savedRecipe.setSteps(steps);
        savedRecipe.getSteps().forEach(step -> step.setRecipe(savedRecipe));
        
        return recipeRepository.save(savedRecipe);
    };

    @Transactional(readOnly = true)
    public Page<RecipeEntity> getRecipesByUserId(Long userId, Pageable pageable){
        return userService.getRecipesByUserId(userId, pageable);
    };

    @Transactional(readOnly = true)
    public Page<RecipeEntity> getFavoritesByUserId(Long userId, Pageable pageable){
        return userService.getFavoriteRecipesByUserId(userId, pageable);
    }

    @Transactional
    public RecipeEntity update(RecipeEntity recipe){
        RecipeEntity existingRecipe = recipeRepository.findById(recipe.getId())
            .orElseThrow(() -> new NotFoundException("Recipe not found"));

        // Обновляем все поля кроме шагов
        existingRecipe.setName(recipe.getName());
        existingRecipe.setDescription(recipe.getDescription());
        existingRecipe.setBrewingMethod(recipe.getBrewingMethod());
        existingRecipe.setCoffeeWeight(recipe.getCoffeeWeight());
        existingRecipe.setGrindSize(recipe.getGrindSize());
        existingRecipe.setWaterAmount(recipe.getWaterAmount());
        existingRecipe.setWaterTemperature(recipe.getWaterTemperature());
        existingRecipe.setTotalTime(recipe.getTotalTime());
        existingRecipe.setDifficulty(recipe.getDifficulty());
        
        existingRecipe.getSteps().clear();
        
        recipe.getSteps().forEach(step -> {
            step.setRecipe(existingRecipe);
            existingRecipe.getSteps().add(step);
        });
        
        return recipeRepository.save(existingRecipe);
    };

    public boolean hasUserLikedRecipe(Long recipeId, Long userId) {
        return recipeRepository.existsByIdAndLikedByUsers_Id(recipeId, userId);
    }

    @Transactional
    public void addRecipeToFavorite(Long recipeId, Long userId){
        UserEntity user = userService.getById(userId);
        RecipeEntity recipe = getById(recipeId);
        recipe.addLikedByUser(user);
        recipeRepository.save(recipe);
    }
    
    @Transactional
    public void deleteRecipeFromFavorite(Long recipeId, Long userId){
        UserEntity user = userService.getById(userId);
        RecipeEntity recipe = getById(recipeId);
        recipe.removeLikedByUser(user);
        recipeRepository.save(recipe);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        RecipeEntity recipe = getById(id);
        
        Set<UserEntity> users = new HashSet<>(recipe.getLikedByUsers());
        
        for (UserEntity user : users) {
            user.getFavoriteRecipes().remove(recipe);
            recipe.getLikedByUsers().remove(user);
        }
        
        recipeRepository.delete(recipe);
    };
}
