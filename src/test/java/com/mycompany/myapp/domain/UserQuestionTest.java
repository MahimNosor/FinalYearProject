package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AppUserTestSamples.*;
import static com.mycompany.myapp.domain.AssignmentTestSamples.*;
import static com.mycompany.myapp.domain.QuestionTestSamples.*;
import static com.mycompany.myapp.domain.UserQuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserQuestion.class);
        UserQuestion userQuestion1 = getUserQuestionSample1();
        UserQuestion userQuestion2 = new UserQuestion();
        assertThat(userQuestion1).isNotEqualTo(userQuestion2);

        userQuestion2.setId(userQuestion1.getId());
        assertThat(userQuestion1).isEqualTo(userQuestion2);

        userQuestion2 = getUserQuestionSample2();
        assertThat(userQuestion1).isNotEqualTo(userQuestion2);
    }

    @Test
    void appUserTest() {
        UserQuestion userQuestion = getUserQuestionRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        userQuestion.setAppUser(appUserBack);
        assertThat(userQuestion.getAppUser()).isEqualTo(appUserBack);

        userQuestion.appUser(null);
        assertThat(userQuestion.getAppUser()).isNull();
    }

    @Test
    void questionTest() {
        UserQuestion userQuestion = getUserQuestionRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        userQuestion.setQuestion(questionBack);
        assertThat(userQuestion.getQuestion()).isEqualTo(questionBack);

        userQuestion.question(null);
        assertThat(userQuestion.getQuestion()).isNull();
    }

    @Test
    void assignmentTest() {
        UserQuestion userQuestion = getUserQuestionRandomSampleGenerator();
        Assignment assignmentBack = getAssignmentRandomSampleGenerator();

        userQuestion.setAssignment(assignmentBack);
        assertThat(userQuestion.getAssignment()).isEqualTo(assignmentBack);

        userQuestion.assignment(null);
        assertThat(userQuestion.getAssignment()).isNull();
    }
}
