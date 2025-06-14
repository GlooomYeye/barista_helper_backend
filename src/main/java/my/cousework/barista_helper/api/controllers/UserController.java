package my.cousework.barista_helper.api.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import my.cousework.barista_helper.api.dto.JwtResponse;
import my.cousework.barista_helper.api.dto.RecipeDetailsDto;
import my.cousework.barista_helper.api.dto.RecipePreviewDto;
import my.cousework.barista_helper.api.dto.UserCredentialsResponse;
import my.cousework.barista_helper.api.dto.UserDto;
import my.cousework.barista_helper.api.mappers.RecipeMapper;
import my.cousework.barista_helper.api.mappers.UserMapper;
import my.cousework.barista_helper.api.services.RecipeService;
import my.cousework.barista_helper.api.services.UserService;
import my.cousework.barista_helper.api.validation.OnCreate;
import my.cousework.barista_helper.api.validation.OnUpdateCredentials;
import my.cousework.barista_helper.api.validation.OnUpdatePassword;
import my.cousework.barista_helper.security.JwtTokenProvider;
import my.cousework.barista_helper.store.entities.RecipeEntity;
import my.cousework.barista_helper.store.entities.UserEntity;



@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
@RequestMapping("/api/users")
public class UserController {

    UserService userService;
    RecipeService recipeService;
    UserMapper userMapper;
    RecipeMapper recipeMapper;
    JwtTokenProvider jwtTokenProvider;
    
    @GetMapping("/me")
    @CrossOrigin(origins = "*")
    @PreAuthorize("isAuthenticated()")
    public UserDto getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userService.getByEmail(email);
        return userMapper.toDto(user);
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UserCredentialsResponse updateCredentials(
                            @Validated(OnUpdateCredentials.class) @RequestBody UserDto dto) {
        UserEntity user = userMapper.toEntity(dto);
        UserEntity updatedUser = userService.updateCredentials(user);
        UserDto updatedUserDto = userMapper.toDto(updatedUser);
        
        String accessToken = jwtTokenProvider.generateAccessToken(
            updatedUser.getId(),
            updatedUser.getEmail(),
            updatedUser.getRoles()
        );
        
        UserCredentialsResponse response = new UserCredentialsResponse();
        response.setUser(updatedUserDto);
        
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setId(updatedUser.getId());
        jwtResponse.setLogin(updatedUser.getEmail());
        jwtResponse.setAccessToken(accessToken);
        jwtResponse.setRefreshToken(jwtTokenProvider.generateRefreshToken(
            updatedUser.getId(),
            updatedUser.getEmail()
        ));
        
        response.setToken(jwtResponse);
        
        return response;
    }

    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    public UserDto updatePassword(
                            @Validated(OnUpdatePassword.class) @RequestBody UserDto dto) {
        UserEntity user = userMapper.toEntity(dto);
        UserEntity updatedUser = userService.updatePassword(user);
        return userMapper.toDto(updatedUser);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#p0)")
    public UserDto getById(@PathVariable("userId") Long userId) {
        UserEntity user = userService.getById(userId);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#p0)")
    public void deleteById(@PathVariable("userId") Long userId) {
        userService.delete(userId);
    }

    @GetMapping("/{userId}/favorites")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#p0)")
    public Page<RecipePreviewDto> getFavoritesByUserId(@PathVariable("userId") Long userId,
            @RequestParam(name = "search", required = false) String searchQuery,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "perPage") int perPage,
            @RequestParam(defaultValue = "id", name = "sortBy") String sortBy,
            @RequestParam(defaultValue = "asc", name = "sortDir") String sortDir) {

        Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        Pageable pageable = PageRequest.of(page - 1, perPage, sort);
        Page<RecipeEntity> recipes;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            recipes = recipeService.searchFavoritesByName(userId, searchQuery, pageable);
        } else {
            recipes = recipeService.getFavoritesByUserId(userId, pageable);
        }
        return recipeMapper.toPreviewDto(recipes);
    }

    @GetMapping("/{userId}/favorites/{recipeId}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#p0)")
    public RecipeDetailsDto getDetailsFavorites(@PathVariable("recipeId") Long recipeId) {
        RecipeEntity recipe = recipeService.getById(recipeId);
        return recipeMapper.toDto(recipe);
    }

    @GetMapping("/{userId}/recipes")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#p0)")
    public Page<RecipePreviewDto> getRecipesByUserId(@PathVariable("userId") Long userId,
            @RequestParam(name = "search", required = false) String searchQuery,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "perPage") int perPage,
            @RequestParam(defaultValue = "id", name = "sortBy") String sortBy,
            @RequestParam(defaultValue = "asc", name = "sortDir") String sortDir) {

        Sort sort = Sort.by(sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page - 1, perPage, sort);
        Page<RecipeEntity> recipes;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            recipes = recipeService.searchFavoritesByName(userId, searchQuery, pageable);
        } else {
            recipes = recipeService.getRecipesByUserId(userId, pageable);
        }
        return recipeMapper.toPreviewDto(recipes);
    }

    @GetMapping("/{userId}/recipes/{recipeId}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#p0)")
    public RecipeDetailsDto getDetailsRecipes(@PathVariable("recipeId") Long recipeId) {
        RecipeEntity recipe = recipeService.getById(recipeId);
        return recipeMapper.toDto(recipe);
    }

    @PostMapping("/{userId}/recipes")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#p0)")
    public RecipeDetailsDto createRecipe(@PathVariable("userId") Long userId, @Validated(OnCreate.class) @RequestBody RecipeDetailsDto dto  ) {
        RecipeEntity recipe = recipeMapper.toEntity(dto);
        RecipeEntity createdRecipe = recipeService.create(recipe, userId);
        return recipeMapper.toDto(createdRecipe);
    }
    
    @PutMapping("/{userId}/recipes/{recipeId}")
    @PreAuthorize("@customSecurityExpression.canAccessRecipe(#p0, #p1)")
    public RecipeDetailsDto update(@PathVariable("userId") Long userId,
                                   @PathVariable("recipeId") Long recipeId,
                                   @Validated() @RequestBody RecipeDetailsDto dto) {
        RecipeEntity recipe = recipeMapper.toEntity(dto);
        RecipeEntity updatedRecipe = recipeService.update(recipe);
        return recipeMapper.toDto(updatedRecipe);
    }

    @PostMapping("/{userId}/recipes/{recipeId}/like")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#p0)")
    public ResponseEntity<Void> likeRecipe(@PathVariable("userId") Long userId,
                             @PathVariable("recipeId") Long recipeId)
    {
        if (recipeService.hasUserLikedRecipe(recipeId, userId)) {
            return ResponseEntity.badRequest().build();
        }
        recipeService.addRecipeToFavorite(recipeId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/recipes/{recipeId}/like")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#p0)")
    public ResponseEntity<Void> dislikeRecipe(@PathVariable("userId") Long userId,
                             @PathVariable("recipeId") Long recipeId)
    {
        if (!recipeService.hasUserLikedRecipe(recipeId, userId)) {
            return ResponseEntity.badRequest().build();
        }
        recipeService.deleteRecipeFromFavorite(recipeId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/recipes/{recipeId}")
    @PreAuthorize("@customSecurityExpression.canAccessRecipe(#p0, #p1)")
    public ResponseEntity<Void> deleteRecipe(@PathVariable("userId") Long userId,
                             @PathVariable("recipeId") Long recipeId)
    {
        recipeService.delete(recipeId, userId);
        return ResponseEntity.ok().build();
    }

}
