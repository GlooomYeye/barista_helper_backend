package my.cousework.barista_helper.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.store.enums.Difficulty;
import lombok.AccessLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipePreviewDto {
    Long id;
    String name;
    String description;
    Integer totalTime;
    Integer likes;
    boolean liked;
    Difficulty difficulty;
}
