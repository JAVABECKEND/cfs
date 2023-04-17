package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TogarakMapperTest {

    private TogarakMapper togarakMapper;

    @BeforeEach
    public void setUp() {
        togarakMapper = new TogarakMapperImpl();
    }
}
