package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AppUserTestSamples.*;
import static com.mycompany.myapp.domain.AssignmentTestSamples.*;
import static com.mycompany.myapp.domain.StudentClassTestSamples.*;
import static com.mycompany.myapp.domain.UserQuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assignment.class);
        Assignment assignment1 = getAssignmentSample1();
        Assignment assignment2 = new Assignment();
        assertThat(assignment1).isNotEqualTo(assignment2);

        assignment2.setId(assignment1.getId());
        assertThat(assignment1).isEqualTo(assignment2);

        assignment2 = getAssignmentSample2();
        assertThat(assignment1).isNotEqualTo(assignment2);
    }

    @Test
    void appUserTest() {
        Assignment assignment = getAssignmentRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        assignment.setAppUser(appUserBack);
        assertThat(assignment.getAppUser()).isEqualTo(appUserBack);

        assignment.appUser(null);
        assertThat(assignment.getAppUser()).isNull();
    }

    @Test
    void studentClassesTest() {
        Assignment assignment = getAssignmentRandomSampleGenerator();
        StudentClass studentClassBack = getStudentClassRandomSampleGenerator();

        assignment.addStudentClasses(studentClassBack);
        assertThat(assignment.getStudentClasses()).containsOnly(studentClassBack);

        assignment.removeStudentClasses(studentClassBack);
        assertThat(assignment.getStudentClasses()).doesNotContain(studentClassBack);

        assignment.studentClasses(new HashSet<>(Set.of(studentClassBack)));
        assertThat(assignment.getStudentClasses()).containsOnly(studentClassBack);

        assignment.setStudentClasses(new HashSet<>());
        assertThat(assignment.getStudentClasses()).doesNotContain(studentClassBack);
    }

    @Test
    void userQuestionsTest() {
        Assignment assignment = getAssignmentRandomSampleGenerator();
        UserQuestion userQuestionBack = getUserQuestionRandomSampleGenerator();

        assignment.addUserQuestions(userQuestionBack);
        assertThat(assignment.getUserQuestions()).containsOnly(userQuestionBack);
        assertThat(userQuestionBack.getAssignment()).isEqualTo(assignment);

        assignment.removeUserQuestions(userQuestionBack);
        assertThat(assignment.getUserQuestions()).doesNotContain(userQuestionBack);
        assertThat(userQuestionBack.getAssignment()).isNull();

        assignment.userQuestions(new HashSet<>(Set.of(userQuestionBack)));
        assertThat(assignment.getUserQuestions()).containsOnly(userQuestionBack);
        assertThat(userQuestionBack.getAssignment()).isEqualTo(assignment);

        assignment.setUserQuestions(new HashSet<>());
        assertThat(assignment.getUserQuestions()).doesNotContain(userQuestionBack);
        assertThat(userQuestionBack.getAssignment()).isNull();
    }
}
