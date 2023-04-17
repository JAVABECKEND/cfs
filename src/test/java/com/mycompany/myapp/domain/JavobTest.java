package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JavobTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Javob.class);
        Javob javob1 = new Javob();
        javob1.setId(1L);
        Javob javob2 = new Javob();
        javob2.setId(javob1.getId());
        assertThat(javob1).isEqualTo(javob2);
        javob2.setId(2L);
        assertThat(javob1).isNotEqualTo(javob2);
        javob1.setId(null);
        assertThat(javob1).isNotEqualTo(javob2);
    }
}
