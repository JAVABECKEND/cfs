package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BooksMapperTest {

    private BooksMapper booksMapper;

    @BeforeEach
    public void setUp() {
        booksMapper = new BooksMapperImpl();
    }
}
