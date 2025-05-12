package my.cousework.barista_helper.store.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.store.enums.Role;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user", schema = "public")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String username;
    @Column(unique = true, nullable = false)
    String email;
    @Column(nullable = false)
    String password;
    @Column()
    @Transient
    String passwordConfirmation;
    @Column()
    @Transient
    String oldPassword;
    @Column()
    Set<Role> roles;
    @ManyToMany
    @JoinTable(
        name = "user_favorite_recipes",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    @Builder.Default
    Set<RecipeEntity> favoriteRecipes = new HashSet<>();
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    Set<RecipeEntity> createdRecipes = new HashSet<>();

    public void addCreatedRecipe(RecipeEntity recipe) {
        createdRecipes.add(recipe);
        recipe.setAuthor(this);
    }

    public void addFavoriteRecipe(RecipeEntity recipe) {
        favoriteRecipes.add(recipe);
        recipe.getLikedByUsers().add(this);
    }
}
