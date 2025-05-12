package my.cousework.barista_helper.security;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import my.cousework.barista_helper.store.entities.JwtEntity;
import my.cousework.barista_helper.store.entities.UserEntity;
import my.cousework.barista_helper.store.enums.Role;

public class JwtEntityFactory {
    public static JwtEntity create(UserEntity user) {
        return new JwtEntity(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            mapToGrantedAuthorities(new ArrayList<> (user.getRoles()))
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }
}
