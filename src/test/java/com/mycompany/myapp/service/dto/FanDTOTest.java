package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FanDTO.class);
        FanDTO fanDTO1 = new FanDTO();
        fanDTO1.setId(1L);
        FanDTO fanDTO2 = new FanDTO();
        assertThat(fanDTO1).isNotEqualTo(fanDTO2);
        fanDTO2.setId(fanDTO1.getId());
        assertThat(fanDTO1).isEqualTo(fanDTO2);
        fanDTO2.setId(2L);
        assertThat(fanDTO1).isNotEqualTo(fanDTO2);
        fanDTO1.setId(null);
        assertThat(fanDTO1).isNotEqualTo(fanDTO2);
    }
}
