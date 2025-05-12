package my.cousework.barista_helper.api.dto;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.api.validation.OnCreate;
import my.cousework.barista_helper.api.validation.OnUpdate;
import my.cousework.barista_helper.store.enums.BrewingMethod;
import my.cousework.barista_helper.store.enums.Difficulty;
import my.cousework.barista_helper.store.enums.GrindSize;
import lombok.AccessLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeDetailsDto {

    @NotNull(message = "Id must not be null.", groups = OnUpdate.class)
    Long id;

    @NotNull(message = "Name not be null.", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Name not be longer than 255 characters.")
    String name;

    @NotNull(message = "Description not be null.", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 1000, message = "Description not be longer than 1000 characters.")
    String description;

    @NotNull(message = "Brewing method not be null.", groups = {OnUpdate.class, OnCreate.class})
    BrewingMethod brewingMethod;

    @NotNull(message = "Weight of coffee not be null.", groups = {OnUpdate.class, OnCreate.class})
    Double coffeeWeight;

    @NotNull(message = "Grind size not be null.", groups = {OnUpdate.class, OnCreate.class})
    GrindSize grindSize;

    @NotNull(message = "Water amount not be null.", groups = {OnUpdate.class, OnCreate.class})
    Integer waterAmount;

    @NotNull(message = "Water temperature not be null.", groups = {OnUpdate.class, OnCreate.class})
    Integer waterTemperature;

    @NotNull(message = "Total time not be null.", groups = {OnUpdate.class, OnCreate.class})
    @DateTimeFormat(iso=DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:mm:ss")
    Integer totalTime;

    Integer likes;
    boolean liked;

    @NotNull(message = "Difficulty not be null.", groups = {OnUpdate.class, OnCreate.class})
    Difficulty difficulty;

    @NotNull(message = "Author not be null.", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Author not be longer than 255 characters.")
    String authorName;

    @NotNull(message = "Author id not be null.", groups = {OnUpdate.class, OnCreate.class})
    Integer authorId;

    @NotNull(message = "Steps not be null.", groups = {OnUpdate.class, OnCreate.class})
    List<StepDto> steps;
}
