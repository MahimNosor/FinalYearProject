package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AppUserTestSamples.*;
import static com.mycompany.myapp.domain.AssignmentTestSamples.*;
import static com.mycompany.myapp.domain.StudentClassTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StudentClassTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentClass.class);
        StudentClass studentClass1 = getStudentClassSample1();
        StudentClass studentClass2 = new StudentClass();
        assertThat(studentClass1).isNotEqualTo(studentClass2);

        studentClass2.setId(studentClass1.getId());
        assertThat(studentClass1).isEqualTo(studentClass2);

        studentClass2 = getStudentClassSample2();
        assertThat(studentClass1).isNotEqualTo(studentClass2);
    }

    @Test
    void usersTest() {
        StudentClass studentClass = getStudentClassRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        studentClass.addUsers(appUserBack);
        assertThat(studentClass.getUsers()).containsOnly(appUserBack);
        assertThat(appUserBack.getClasses()).containsOnly(studentClass);

        studentClass.removeUsers(appUserBack);
        assertThat(studentClass.getUsers()).doesNotContain(appUserBack);
        assertThat(appUserBack.getClasses()).doesNotContain(studentClass);

        studentClass.users(new HashSet<>(Set.of(appUserBack)));
        assertThat(studentClass.getUsers()).containsOnly(appUserBack);
        assertThat(appUserBack.getClasses()).containsOnly(studentClass);

        studentClass.setUsers(new HashSet<>());
        assertThat(studentClass.getUsers()).doesNotContain(appUserBack);
        assertThat(appUserBack.getClasses()).doesNotContain(studentClass);
    }

    @Test
    void assignmentsTest() {
        StudentClass studentClass = getStudentClassRandomSampleGenerator();
        Assignment assignmentBack = getAssignmentRandomSampleGenerator();

        studentClass.addAssignments(assignmentBack);
        assertThat(studentClass.getAssignments()).containsOnly(assignmentBack);
        assertThat(assignmentBack.getStudentClasses()).containsOnly(studentClass);

        studentClass.removeAssignments(assignmentBack);
        assertThat(studentClass.getAssignments()).doesNotContain(assignmentBack);
        assertThat(assignmentBack.getStudentClasses()).doesNotContain(studentClass);

        studentClass.assignments(new HashSet<>(Set.of(assignmentBack)));
        assertThat(studentClass.getAssignments()).containsOnly(assignmentBack);
        assertThat(assignmentBack.getStudentClasses()).containsOnly(studentClass);

        studentClass.setAssignments(new HashSet<>());
        assertThat(studentClass.getAssignments()).doesNotContain(assignmentBack);
        assertThat(assignmentBack.getStudentClasses()).doesNotContain(studentClass);
    }
}
