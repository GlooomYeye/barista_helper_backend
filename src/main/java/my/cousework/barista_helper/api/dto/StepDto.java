package my.cousework.barista_helper.api.dto;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.api.validation.OnCreate;
import my.cousework.barista_helper.api.validation.OnUpdate;
import my.cousework.barista_helper.store.enums.StepType;
 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StepDto {

    @NotNull(message = "Id must not be null.", groups = OnUpdate.class)
    Long id;

    @NotNull(message = "Type not be null.", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 255, message = "Type not be longer than 255 characters.")
    StepType type;

    Integer optional;
    
    @NotNull(message = "Description not be null.", groups = {OnUpdate.class, OnCreate.class})
    @Length(max = 1000, message = "Description not be longer than 1000 characters.")
    String description;

    @NotNull(message = "Time not be null.", groups = {OnUpdate.class, OnCreate.class})
    @DateTimeFormat(iso=DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:mm:ss")
    Integer time;

    
}
