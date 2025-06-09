package my.cousework.barista_helper.api.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCredentialsResponse {
    UserDto user;
    JwtResponse token;
}