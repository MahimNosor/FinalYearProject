package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.TestCase} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCaseDTO implements Serializable {

    private Long id;

    @NotNull
    private String input;

    @NotNull
    private String expectedOutput;

    private String description;

    private QuestionDTO question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCaseDTO)) {
            return false;
        }

        TestCaseDTO testCaseDTO = (TestCaseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, testCaseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCaseDTO{" +
            "id=" + getId() +
            ", input='" + getInput() + "'" +
            ", expectedOutput='" + getExpectedOutput() + "'" +
            ", description='" + getDescription() + "'" +
            ", question=" + getQuestion() +
            "}";
    }
}
