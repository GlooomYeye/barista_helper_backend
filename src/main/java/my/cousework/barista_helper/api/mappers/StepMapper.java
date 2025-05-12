package my.cousework.barista_helper.api.mappers;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import my.cousework.barista_helper.api.dto.StepDto;

import my.cousework.barista_helper.store.entities.StepEntity;

@Mapper(componentModel = "spring")
public interface StepMapper extends Mappable<StepEntity, StepDto> {

    @Mapping(target = "recipe", ignore = true)
    StepEntity toEntity(StepDto dto);

    @Mapping(target = "recipe", ignore = true)
    List<StepEntity> toEntity(List<StepDto> dtos);

}
