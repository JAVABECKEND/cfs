package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TogarakTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Togarak.class);
        Togarak togarak1 = new Togarak();
        togarak1.setId(1L);
        Togarak togarak2 = new Togarak();
        togarak2.setId(togarak1.getId());
        assertThat(togarak1).isEqualTo(togarak2);
        togarak2.setId(2L);
        assertThat(togarak1).isNotEqualTo(togarak2);
        togarak1.setId(null);
        assertThat(togarak1).isNotEqualTo(togarak2);
    }
}
