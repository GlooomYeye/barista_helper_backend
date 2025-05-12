package my.cousework.barista_helper.api.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtRequest {

    @NotNull(message = "Логин не может быть пустым")
    String login;
    
    @NotNull(message = "Пароль не может быть пустым")
    String password;

}
