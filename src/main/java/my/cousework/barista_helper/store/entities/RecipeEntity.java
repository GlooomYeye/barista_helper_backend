package my.cousework.barista_helper.store.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.store.enums.BrewingMethod;
import my.cousework.barista_helper.store.enums.Difficulty;
import my.cousework.barista_helper.store.enums.GrindSize;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "recipe")
public class RecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String name;
    @Column(length = 1000)
    String description;
    @Column(nullable = false)
    BrewingMethod brewingMethod;
    @Column(nullable = false)
    Double coffeeWeight;
    @Column(nullable = false)
    GrindSize grindSize;
    @Column(nullable = false)
    Integer waterAmount;
    @Column(nullable = false)
    Integer waterTemperature;
    @Column(nullable = false)
    Integer totalTime;
    @Column
    Integer likes;
    @Column
    Difficulty difficulty;

    @Builder.Default
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    List<StepEntity> steps = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    UserEntity author;
    @ManyToMany(mappedBy = "favoriteRecipes")
    @Builder.Default
    Set<UserEntity> likedByUsers = new HashSet<>();
    public void addStep(StepEntity step) {
        steps.add(step);
        step.setRecipe(this);
    }
    public void addLikedByUser(UserEntity user) {
        likedByUsers.add(user);
        likes++;
        user.getFavoriteRecipes().add(this);
    }
    public void removeLikedByUser(UserEntity user) {
        likedByUsers.remove(user);
        likes--;
        user.getFavoriteRecipes().remove(this);
    }
}
