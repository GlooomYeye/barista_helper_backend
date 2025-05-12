package my.cousework.barista_helper.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import my.cousework.barista_helper.api.dto.RecipeDetailsDto;
import my.cousework.barista_helper.api.dto.RecipePreviewDto;
import my.cousework.barista_helper.store.entities.RecipeEntity;
import my.cousework.barista_helper.store.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface RecipeMapper extends Mappable<RecipeEntity, RecipeDetailsDto> {

    @Mapping(target = "likedByUsers", ignore = true)
    @Mapping(target = "author", ignore = true)
    RecipeEntity toEntity(RecipeDetailsDto dto);
    
    @Mapping(target = "liked", expression = "java(isRecipeLikedByCurrentUser(entity))")
    @Mapping(target = "authorName", source = "author.username")
    @Mapping(target = "authorId", source = "author.id")
    RecipeDetailsDto toDto(RecipeEntity entity);

    @Mapping(target = "liked", expression = "java(isRecipeLikedByCurrentUser(recipe))")
    RecipePreviewDto toPreviewDto(RecipeEntity recipe);

    default Page<RecipePreviewDto> toPreviewDto(Page<RecipeEntity> page) {
        return page.map(this::toPreviewDto);
    }

    default boolean isRecipeLikedByCurrentUser(RecipeEntity recipe) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        String currentEmail = authentication.getName();
        return recipe.getLikedByUsers().stream()
                .map(UserEntity::getEmail)
                .anyMatch(Email -> Email.equals(currentEmail));
    }
}
