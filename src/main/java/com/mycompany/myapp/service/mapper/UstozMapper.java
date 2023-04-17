package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Ustoz;
import com.mycompany.myapp.service.dto.UstozDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ustoz} and its DTO {@link UstozDTO}.
 */
@Mapper(componentModel = "spring")
public interface UstozMapper extends EntityMapper<UstozDTO, Ustoz> {}
