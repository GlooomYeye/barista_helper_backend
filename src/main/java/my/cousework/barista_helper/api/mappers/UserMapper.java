package my.cousework.barista_helper.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import my.cousework.barista_helper.api.dto.UserDto;
import my.cousework.barista_helper.store.entities.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<UserEntity, UserDto> {

    @Mapping(target = "createdRecipes", ignore = true) 
    @Mapping(target = "favoriteRecipes", ignore = true) 
    @Mapping(target = "roles", ignore = true) 
    UserEntity toEntity(UserDto dto);
    
}
