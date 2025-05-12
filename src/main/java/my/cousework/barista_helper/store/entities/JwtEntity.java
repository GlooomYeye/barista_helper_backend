package my.cousework.barista_helper.store.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;


@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtEntity implements UserDetails {
    final Long id;
    final String name;
    final String email;
    final String password;
    final Collection<? extends GrantedAuthority> authorities;
    public String getUsername() {
        return email;
    }
    
}
