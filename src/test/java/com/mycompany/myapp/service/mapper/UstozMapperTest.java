package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UstozMapperTest {

    private UstozMapper ustozMapper;

    @BeforeEach
    public void setUp() {
        ustozMapper = new UstozMapperImpl();
    }
}
