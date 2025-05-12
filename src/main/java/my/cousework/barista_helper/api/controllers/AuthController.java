package my.cousework.barista_helper.api.controllers;


import my.cousework.barista_helper.api.dto.JwtRequest;
import my.cousework.barista_helper.api.dto.JwtResponse;
import my.cousework.barista_helper.api.dto.UserDto;
import my.cousework.barista_helper.api.mappers.UserMapper;
import my.cousework.barista_helper.api.services.AuthService;
import my.cousework.barista_helper.api.services.UserService;
import my.cousework.barista_helper.api.validation.OnCreate;
import my.cousework.barista_helper.store.entities.UserEntity;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;



@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    AuthService authService;
    UserService userService;
    UserMapper userMapper;
    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        UserEntity user = userMapper.toEntity(userDto);
        UserEntity createdUser = userService.create(user);
        return userMapper.toDto(createdUser);
    }
    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }
    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
