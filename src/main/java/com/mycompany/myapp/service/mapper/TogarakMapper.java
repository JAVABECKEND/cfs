package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Togarak;
import com.mycompany.myapp.domain.Ustoz;
import com.mycompany.myapp.service.dto.TogarakDTO;
import com.mycompany.myapp.service.dto.UstozDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Togarak} and its DTO {@link TogarakDTO}.
 */
@Mapper(componentModel = "spring")
public interface TogarakMapper extends EntityMapper<TogarakDTO, Togarak> {
    @Mapping(target = "ustozs", source = "ustozs", qualifiedByName = "ustozIsmSet")
    TogarakDTO toDto(Togarak s);

    @Mapping(target = "removeUstoz", ignore = true)
    Togarak toEntity(TogarakDTO togarakDTO);

    @Named("ustozIsm")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "ism", source = "ism")
    UstozDTO toDtoUstozIsm(Ustoz ustoz);

    @Named("ustozIsmSet")
    default Set<UstozDTO> toDtoUstozIsmSet(Set<Ustoz> ustoz) {
        return ustoz.stream().map(this::toDtoUstozIsm).collect(Collectors.toSet());
    }
}
