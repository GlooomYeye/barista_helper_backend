package my.cousework.barista_helper.api.dto;



import lombok.Data;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.api.validation.OnCreate;
import my.cousework.barista_helper.api.validation.OnUpdateCredentials;
import my.cousework.barista_helper.api.validation.OnUpdatePassword;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotNull(message = "Id must not be null.", groups = OnUpdateCredentials.class)
    Long id;
    @NotNull(message = "Name not be null.", groups = {OnUpdateCredentials.class, OnCreate.class})
    @Length(max = 255, message = "Name not be longer than 255 characters.")
    String username;
    @NotNull(message = "Email not be null.", groups = {OnUpdateCredentials.class, OnCreate.class})
    @Length(max = 255, message = "Email not be longer than 255 characters.")
    String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Odl password not be null", groups = {OnUpdatePassword.class})
    String oldPassword;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password not be null", groups = {OnUpdatePassword.class, OnCreate.class})
    String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation not be null", groups = {OnCreate.class})
    String passwordConfirmation;
}
