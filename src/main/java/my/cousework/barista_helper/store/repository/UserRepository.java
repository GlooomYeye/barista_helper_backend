package my.cousework.barista_helper.store.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import my.cousework.barista_helper.store.entities.RecipeEntity;
import my.cousework.barista_helper.store.entities.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
        
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findById(Long id);

    @Query(value = """
           SELECT r.* FROM recipe r
           JOIN user_favorite_recipes ufr ON r.id = ufr.recipe_id
           WHERE ufr.user_id = :userId
           """,
           countQuery = """
           SELECT COUNT(*) FROM user_favorite_recipes
           WHERE user_id = :userId
           """,
           nativeQuery = true)
    Page<RecipeEntity> findFavoriteByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT u.createdRecipes FROM UserEntity u WHERE u.id = :userId")
    Page<RecipeEntity> findCreatedByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
       "FROM RecipeEntity r WHERE r.id = :recipeId AND r.author.id = :userId")
    boolean isAuthorOfRecipe(@Param("userId") Long userId, @Param("recipeId") Long recipeId);
    
}
