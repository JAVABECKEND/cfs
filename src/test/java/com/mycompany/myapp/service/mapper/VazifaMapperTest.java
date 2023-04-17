package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VazifaMapperTest {

    private VazifaMapper vazifaMapper;

    @BeforeEach
    public void setUp() {
        vazifaMapper = new VazifaMapperImpl();
    }
}
