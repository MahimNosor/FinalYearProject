package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.QuestionTestSamples.*;
import static com.mycompany.myapp.domain.StudentClassTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = getQuestionSample1();
        Question question2 = new Question();
        assertThat(question1).isNotEqualTo(question2);

        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);

        question2 = getQuestionSample2();
        assertThat(question1).isNotEqualTo(question2);
    }

    @Test
    void studentClassTest() {
        Question question = getQuestionRandomSampleGenerator();
        StudentClass studentClassBack = getStudentClassRandomSampleGenerator();

        question.setStudentClass(studentClassBack);
        assertThat(question.getStudentClass()).isEqualTo(studentClassBack);

        question.studentClass(null);
        assertThat(question.getStudentClass()).isNull();
    }
}
