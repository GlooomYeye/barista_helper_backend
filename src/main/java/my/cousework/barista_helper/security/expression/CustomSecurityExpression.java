package my.cousework.barista_helper.security.expression;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.api.services.UserService;
import my.cousework.barista_helper.store.entities.JwtEntity;
import my.cousework.barista_helper.store.enums.Role;

@Service("customSecurityExpression")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomSecurityExpression {
    UserService userService;
    public boolean canAccessUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtEntity jwtEntity = (JwtEntity) authentication.getPrincipal();
        Long userId = jwtEntity.getId();
        return userId.equals(id) || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }
    private boolean hasAnyRole(Authentication authentication, Role... roles) {
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }
    public boolean canAccessRecipe(Long id,Long recipeId)  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtEntity jwtEntity = (JwtEntity) authentication.getPrincipal();
        Long userId = jwtEntity.getId();
        return (userId.equals(id) && userService.isRecipeOwner(userId, recipeId)) || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }
}
