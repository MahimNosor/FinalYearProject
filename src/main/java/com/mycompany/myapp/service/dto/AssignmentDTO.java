package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.ProgrammingLanguage;
import com.mycompany.myapp.domain.enumeration.difficulty;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Assignment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssignmentDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private difficulty difficulty;

    @Lob
    private String description;

    @NotNull
    private ProgrammingLanguage language;

    @Lob
    private String testCases;

    @NotNull
    private Integer maxScore;

    private Boolean isPreloaded;

    @NotNull
    private AppUserDTO appUser;

    @NotNull
    private Set<StudentClassDTO> studentClasses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProgrammingLanguage getLanguage() {
        return language;
    }

    public void setLanguage(ProgrammingLanguage language) {
        this.language = language;
    }

    public String getTestCases() {
        return testCases;
    }

    public void setTestCases(String testCases) {
        this.testCases = testCases;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public Boolean getIsPreloaded() {
        return isPreloaded;
    }

    public void setIsPreloaded(Boolean isPreloaded) {
        this.isPreloaded = isPreloaded;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
    }

    public Set<StudentClassDTO> getStudentClasses() {
        return studentClasses;
    }

    public void setStudentClasses(Set<StudentClassDTO> studentClasses) {
        this.studentClasses = studentClasses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssignmentDTO)) {
            return false;
        }

        AssignmentDTO assignmentDTO = (AssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssignmentDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", difficulty='" + getDifficulty() + "'" +
            ", description='" + getDescription() + "'" +
            ", language='" + getLanguage() + "'" +
            ", testCases='" + getTestCases() + "'" +
            ", maxScore=" + getMaxScore() +
            ", isPreloaded='" + getIsPreloaded() + "'" +
            ", appUser=" + getAppUser() +
            ", studentClasses=" + getStudentClasses() +
            "}";
    }
}
