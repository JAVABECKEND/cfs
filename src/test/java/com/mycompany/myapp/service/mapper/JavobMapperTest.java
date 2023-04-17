package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JavobMapperTest {

    private JavobMapper javobMapper;

    @BeforeEach
    public void setUp() {
        javobMapper = new JavobMapperImpl();
    }
}
