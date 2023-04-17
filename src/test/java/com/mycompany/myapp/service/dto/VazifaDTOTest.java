package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VazifaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VazifaDTO.class);
        VazifaDTO vazifaDTO1 = new VazifaDTO();
        vazifaDTO1.setId(1L);
        VazifaDTO vazifaDTO2 = new VazifaDTO();
        assertThat(vazifaDTO1).isNotEqualTo(vazifaDTO2);
        vazifaDTO2.setId(vazifaDTO1.getId());
        assertThat(vazifaDTO1).isEqualTo(vazifaDTO2);
        vazifaDTO2.setId(2L);
        assertThat(vazifaDTO1).isNotEqualTo(vazifaDTO2);
        vazifaDTO1.setId(null);
        assertThat(vazifaDTO1).isNotEqualTo(vazifaDTO2);
    }
}
