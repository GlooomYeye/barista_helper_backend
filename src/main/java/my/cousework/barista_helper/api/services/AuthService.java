package my.cousework.barista_helper.api.services;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.api.dto.JwtRequest;
import my.cousework.barista_helper.api.dto.JwtResponse;
import my.cousework.barista_helper.security.JwtTokenProvider;
import my.cousework.barista_helper.store.entities.UserEntity;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthService {
    AuthenticationManager authenticationManager;
    UserService userService;
    JwtTokenProvider jwtTokenProvider;

    public JwtResponse login(JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));
        UserEntity user = userService.getByEmail(loginRequest.getLogin());
        jwtResponse.setId(user.getId());
        jwtResponse.setLogin(user.getEmail());
        jwtResponse.setAccessToken(jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail(), user.getRoles()));
        jwtResponse.setRefreshToken(jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail()));
        return jwtResponse;
    };

    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    };
}
