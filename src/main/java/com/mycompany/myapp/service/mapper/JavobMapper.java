package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Javob;
import com.mycompany.myapp.domain.Ustoz;
import com.mycompany.myapp.service.dto.JavobDTO;
import com.mycompany.myapp.service.dto.UstozDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Javob} and its DTO {@link JavobDTO}.
 */
@Mapper(componentModel = "spring")
public interface JavobMapper extends EntityMapper<JavobDTO, Javob> {
    @Mapping(target = "ustoz", source = "ustoz", qualifiedByName = "ustozIsm")
    JavobDTO toDto(Javob s);

    @Named("ustozIsm")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "ism", source = "ism")
    UstozDTO toDtoUstozIsm(Ustoz ustoz);
}
