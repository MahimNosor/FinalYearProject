package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TestCaseAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTestCaseAllPropertiesEquals(TestCase expected, TestCase actual) {
        assertTestCaseAutoGeneratedPropertiesEquals(expected, actual);
        assertTestCaseAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTestCaseAllUpdatablePropertiesEquals(TestCase expected, TestCase actual) {
        assertTestCaseUpdatableFieldsEquals(expected, actual);
        assertTestCaseUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTestCaseAutoGeneratedPropertiesEquals(TestCase expected, TestCase actual) {
        assertThat(expected)
            .as("Verify TestCase auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTestCaseUpdatableFieldsEquals(TestCase expected, TestCase actual) {
        assertThat(expected)
            .as("Verify TestCase relevant properties")
            .satisfies(e -> assertThat(e.getInput()).as("check input").isEqualTo(actual.getInput()))
            .satisfies(e -> assertThat(e.getExpectedOutput()).as("check expectedOutput").isEqualTo(actual.getExpectedOutput()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTestCaseUpdatableRelationshipsEquals(TestCase expected, TestCase actual) {
        assertThat(expected)
            .as("Verify TestCase relationships")
            .satisfies(e -> assertThat(e.getQuestion()).as("check question").isEqualTo(actual.getQuestion()));
    }
}
