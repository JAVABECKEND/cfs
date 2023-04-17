package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UstozDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UstozDTO.class);
        UstozDTO ustozDTO1 = new UstozDTO();
        ustozDTO1.setId(1L);
        UstozDTO ustozDTO2 = new UstozDTO();
        assertThat(ustozDTO1).isNotEqualTo(ustozDTO2);
        ustozDTO2.setId(ustozDTO1.getId());
        assertThat(ustozDTO1).isEqualTo(ustozDTO2);
        ustozDTO2.setId(2L);
        assertThat(ustozDTO1).isNotEqualTo(ustozDTO2);
        ustozDTO1.setId(null);
        assertThat(ustozDTO1).isNotEqualTo(ustozDTO2);
    }
}
