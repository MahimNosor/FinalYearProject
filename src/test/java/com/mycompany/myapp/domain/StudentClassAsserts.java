package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentClassAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStudentClassAllPropertiesEquals(StudentClass expected, StudentClass actual) {
        assertStudentClassAutoGeneratedPropertiesEquals(expected, actual);
        assertStudentClassAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStudentClassAllUpdatablePropertiesEquals(StudentClass expected, StudentClass actual) {
        assertStudentClassUpdatableFieldsEquals(expected, actual);
        assertStudentClassUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStudentClassAutoGeneratedPropertiesEquals(StudentClass expected, StudentClass actual) {
        assertThat(expected)
            .as("Verify StudentClass auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStudentClassUpdatableFieldsEquals(StudentClass expected, StudentClass actual) {
        assertThat(expected)
            .as("Verify StudentClass relevant properties")
            .satisfies(e -> assertThat(e.getClassName()).as("check className").isEqualTo(actual.getClassName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertStudentClassUpdatableRelationshipsEquals(StudentClass expected, StudentClass actual) {
        assertThat(expected)
            .as("Verify StudentClass relationships")
            .satisfies(e -> assertThat(e.getUsers()).as("check users").isEqualTo(actual.getUsers()))
            .satisfies(e -> assertThat(e.getAssignments()).as("check assignments").isEqualTo(actual.getAssignments()));
    }
}
