package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JavobDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(JavobDTO.class);
        JavobDTO javobDTO1 = new JavobDTO();
        javobDTO1.setId(1L);
        JavobDTO javobDTO2 = new JavobDTO();
        assertThat(javobDTO1).isNotEqualTo(javobDTO2);
        javobDTO2.setId(javobDTO1.getId());
        assertThat(javobDTO1).isEqualTo(javobDTO2);
        javobDTO2.setId(2L);
        assertThat(javobDTO1).isNotEqualTo(javobDTO2);
        javobDTO1.setId(null);
        assertThat(javobDTO1).isNotEqualTo(javobDTO2);
    }
}
