package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BooksDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BooksDTO.class);
        BooksDTO booksDTO1 = new BooksDTO();
        booksDTO1.setId(1L);
        BooksDTO booksDTO2 = new BooksDTO();
        assertThat(booksDTO1).isNotEqualTo(booksDTO2);
        booksDTO2.setId(booksDTO1.getId());
        assertThat(booksDTO1).isEqualTo(booksDTO2);
        booksDTO2.setId(2L);
        assertThat(booksDTO1).isNotEqualTo(booksDTO2);
        booksDTO1.setId(null);
        assertThat(booksDTO1).isNotEqualTo(booksDTO2);
    }
}
