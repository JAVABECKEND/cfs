package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Books;
import com.mycompany.myapp.service.dto.BooksDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Books} and its DTO {@link BooksDTO}.
 */
@Mapper(componentModel = "spring")
public interface BooksMapper extends EntityMapper<BooksDTO, Books> {}
