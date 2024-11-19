package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.SubmissionStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.UserQuestion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserQuestionDTO implements Serializable {

    private Long id;

    private Integer score;

    private Instant submissionDate;

    private SubmissionStatus status;

    private AppUserDTO appUser;

    private QuestionDTO question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Instant getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Instant submissionDate) {
        this.submissionDate = submissionDate;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public AppUserDTO getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUserDTO appUser) {
        this.appUser = appUser;
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
        if (!(o instanceof UserQuestionDTO)) {
            return false;
        }

        UserQuestionDTO userQuestionDTO = (UserQuestionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userQuestionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserQuestionDTO{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", submissionDate='" + getSubmissionDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", appUser=" + getAppUser() +
            ", question=" + getQuestion() +
            "}";
    }
}
