package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VazifaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vazifa.class);
        Vazifa vazifa1 = new Vazifa();
        vazifa1.setId(1L);
        Vazifa vazifa2 = new Vazifa();
        vazifa2.setId(vazifa1.getId());
        assertThat(vazifa1).isEqualTo(vazifa2);
        vazifa2.setId(2L);
        assertThat(vazifa1).isNotEqualTo(vazifa2);
        vazifa1.setId(null);
        assertThat(vazifa1).isNotEqualTo(vazifa2);
    }
}
