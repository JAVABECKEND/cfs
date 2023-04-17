package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UstozTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ustoz.class);
        Ustoz ustoz1 = new Ustoz();
        ustoz1.setId(1L);
        Ustoz ustoz2 = new Ustoz();
        ustoz2.setId(ustoz1.getId());
        assertThat(ustoz1).isEqualTo(ustoz2);
        ustoz2.setId(2L);
        assertThat(ustoz1).isNotEqualTo(ustoz2);
        ustoz1.setId(null);
        assertThat(ustoz1).isNotEqualTo(ustoz2);
    }
}
