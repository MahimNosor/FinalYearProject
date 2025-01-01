package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class UserQuestionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserQuestionAllPropertiesEquals(UserQuestion expected, UserQuestion actual) {
        assertUserQuestionAutoGeneratedPropertiesEquals(expected, actual);
        assertUserQuestionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserQuestionAllUpdatablePropertiesEquals(UserQuestion expected, UserQuestion actual) {
        assertUserQuestionUpdatableFieldsEquals(expected, actual);
        assertUserQuestionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserQuestionAutoGeneratedPropertiesEquals(UserQuestion expected, UserQuestion actual) {
        assertThat(expected)
            .as("Verify UserQuestion auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserQuestionUpdatableFieldsEquals(UserQuestion expected, UserQuestion actual) {
        assertThat(expected)
            .as("Verify UserQuestion relevant properties")
            .satisfies(e -> assertThat(e.getScore()).as("check score").isEqualTo(actual.getScore()))
            .satisfies(e -> assertThat(e.getSubmissionDate()).as("check submissionDate").isEqualTo(actual.getSubmissionDate()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserQuestionUpdatableRelationshipsEquals(UserQuestion expected, UserQuestion actual) {
        assertThat(expected)
            .as("Verify UserQuestion relationships")
            .satisfies(e -> assertThat(e.getAppUser()).as("check appUser").isEqualTo(actual.getAppUser()))
            .satisfies(e -> assertThat(e.getQuestion()).as("check question").isEqualTo(actual.getQuestion()))
            .satisfies(e -> assertThat(e.getAssignment()).as("check assignment").isEqualTo(actual.getAssignment()));
    }
}
