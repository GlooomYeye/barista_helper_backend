package my.cousework.barista_helper.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import my.cousework.barista_helper.api.dto.TermDto;
import my.cousework.barista_helper.store.entities.TermEntity;

@Mapper(componentModel = "spring")
public interface TermMapper extends Mappable<TermEntity, TermDto> {

    @Mapping(target = "id", ignore = true) 
    TermEntity toEntity(TermDto dto);
}
