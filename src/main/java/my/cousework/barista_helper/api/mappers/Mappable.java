package my.cousework.barista_helper.api.mappers;

import java.util.List;

public interface Mappable<E, D> {
    D toDto(E entity);
    List<D> toDto(List<E> entities);

    E toEntity(D dto);
    List<E> toEntity(List<D> dtos);
}
