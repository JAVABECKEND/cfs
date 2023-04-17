package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Fan;
import com.mycompany.myapp.domain.Ustoz;
import com.mycompany.myapp.domain.Vazifa;
import com.mycompany.myapp.service.dto.FanDTO;
import com.mycompany.myapp.service.dto.UstozDTO;
import com.mycompany.myapp.service.dto.VazifaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vazifa} and its DTO {@link VazifaDTO}.
 */
@Mapper(componentModel = "spring")
public interface VazifaMapper extends EntityMapper<VazifaDTO, Vazifa> {
    @Mapping(target = "ustoz", source = "ustoz", qualifiedByName = "ustozIsm")
    @Mapping(target = "fan", source = "fan", qualifiedByName = "fanNomi")
    VazifaDTO toDto(Vazifa s);

    @Named("ustozIsm")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "ism", source = "ism")
    UstozDTO toDtoUstozIsm(Ustoz ustoz);

    @Named("fanNomi")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nomi", source = "nomi")
    FanDTO toDtoFanNomi(Fan fan);
}
