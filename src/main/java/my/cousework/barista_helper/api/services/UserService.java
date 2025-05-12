package my.cousework.barista_helper.api.services;


import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.api.exceptions.NotFoundException;
import my.cousework.barista_helper.store.entities.RecipeEntity;
import my.cousework.barista_helper.store.entities.UserEntity;
import my.cousework.barista_helper.store.enums.Role;
import my.cousework.barista_helper.store.repository.UserRepository;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity create(UserEntity user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new IllegalStateException("User already exists");
        }
        if(!user.getPassword().equals(user.getPasswordConfirmation())){ 
            throw new IllegalStateException("Passwords do not match");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        userRepository.save(user);
        return user;
    };
    @Transactional
    public Page<RecipeEntity> getRecipesByUserId(Long userId, Pageable pageable){
        return userRepository.findCreatedByUserId(userId, pageable);
    };
    @Transactional
    public Page<RecipeEntity> getFavoriteRecipesByUserId(Long userId, Pageable pageable){
        return userRepository.findFavoriteByUserId(userId, pageable );
    };

    @Transactional(readOnly = true)
    public UserEntity getById(Long id){
        return userRepository.findById(id)
                            .orElseThrow(()->new NotFoundException("User not found"));
    };

    @Transactional(readOnly = true)
    public UserEntity getByEmail(String email){
        return userRepository.findByEmail(email)
                            .orElseThrow(()->new NotFoundException("User not found"));
    };

    @Transactional
    public UserEntity updateCredentials(UserEntity updatedUser) {
        UserEntity user = getById(updatedUser.getId());
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        userRepository.save(user);
        return user;
    }

    @Transactional
    public UserEntity updatePassword(UserEntity updatedUser) {
        UserEntity user = getById(updatedUser.getId());

        if (!passwordEncoder.matches(updatedUser.getOldPassword(), user.getPassword())) {
            throw new IllegalStateException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Transactional
    public void delete(Long id){userRepository.deleteById(id);};

    public boolean isRecipeOwner(Long userId, Long recipeId){
        return userRepository.isAuthorOfRecipe(userId, recipeId);
    };
}
