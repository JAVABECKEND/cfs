package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fan.class);
        Fan fan1 = new Fan();
        fan1.setId(1L);
        Fan fan2 = new Fan();
        fan2.setId(fan1.getId());
        assertThat(fan1).isEqualTo(fan2);
        fan2.setId(2L);
        assertThat(fan1).isNotEqualTo(fan2);
        fan1.setId(null);
        assertThat(fan1).isNotEqualTo(fan2);
    }
}
