package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TogarakDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TogarakDTO.class);
        TogarakDTO togarakDTO1 = new TogarakDTO();
        togarakDTO1.setId(1L);
        TogarakDTO togarakDTO2 = new TogarakDTO();
        assertThat(togarakDTO1).isNotEqualTo(togarakDTO2);
        togarakDTO2.setId(togarakDTO1.getId());
        assertThat(togarakDTO1).isEqualTo(togarakDTO2);
        togarakDTO2.setId(2L);
        assertThat(togarakDTO1).isNotEqualTo(togarakDTO2);
        togarakDTO1.setId(null);
        assertThat(togarakDTO1).isNotEqualTo(togarakDTO2);
    }
}
