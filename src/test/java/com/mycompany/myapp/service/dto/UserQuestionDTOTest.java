package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserQuestionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserQuestionDTO.class);
        UserQuestionDTO userQuestionDTO1 = new UserQuestionDTO();
        userQuestionDTO1.setId(1L);
        UserQuestionDTO userQuestionDTO2 = new UserQuestionDTO();
        assertThat(userQuestionDTO1).isNotEqualTo(userQuestionDTO2);
        userQuestionDTO2.setId(userQuestionDTO1.getId());
        assertThat(userQuestionDTO1).isEqualTo(userQuestionDTO2);
        userQuestionDTO2.setId(2L);
        assertThat(userQuestionDTO1).isNotEqualTo(userQuestionDTO2);
        userQuestionDTO1.setId(null);
        assertThat(userQuestionDTO1).isNotEqualTo(userQuestionDTO2);
    }
}
