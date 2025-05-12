package my.cousework.barista_helper.store.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import my.cousework.barista_helper.store.entities.RecipeEntity;
import my.cousework.barista_helper.store.enums.BrewingMethod;


@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {
    Page<RecipeEntity> findByBrewingMethod(BrewingMethod brewingMethod, Pageable pageable);

    Page<RecipeEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByIdAndAuthor_Id(Long recipeId, Long userId);

    boolean existsByIdAndLikedByUsers_Id(Long recipeId, Long userId);
}
