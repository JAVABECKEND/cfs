package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Fan;
import com.mycompany.myapp.service.dto.FanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fan} and its DTO {@link FanDTO}.
 */
@Mapper(componentModel = "spring")
public interface FanMapper extends EntityMapper<FanDTO, Fan> {}
