package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.ProgrammingLanguage;
import com.mycompany.myapp.domain.enumeration.difficulty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Assignment.
 */
@Entity
@Table(name = "assignment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assignment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Assignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private difficulty difficulty;

    @Lob
    @Column(name = "description", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ProgrammingLanguage language;

    @Lob
    @Column(name = "test_cases", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String testCases;

    @NotNull
    @Column(name = "max_score", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer maxScore;

    @Column(name = "is_preloaded")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isPreloaded;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "classes", "assignments" }, allowSetters = true)
    private AppUser appUser;

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull
    @JoinTable(
        name = "rel_assignment__student_classes",
        joinColumns = @JoinColumn(name = "assignment_id"),
        inverseJoinColumns = @JoinColumn(name = "student_classes_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "users", "assignments" }, allowSetters = true)
    private Set<StudentClass> studentClasses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "appUser", "question", "assignment" }, allowSetters = true)
    private Set<UserQuestion> userQuestions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Assignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Assignment title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public difficulty getDifficulty() {
        return this.difficulty;
    }

    public Assignment difficulty(difficulty difficulty) {
        this.setDifficulty(difficulty);
        return this;
    }

    public void setDifficulty(difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return this.description;
    }

    public Assignment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProgrammingLanguage getLanguage() {
        return this.language;
    }

    public Assignment language(ProgrammingLanguage language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(ProgrammingLanguage language) {
        this.language = language;
    }

    public String getTestCases() {
        return this.testCases;
    }

    public Assignment testCases(String testCases) {
        this.setTestCases(testCases);
        return this;
    }

    public void setTestCases(String testCases) {
        this.testCases = testCases;
    }

    public Integer getMaxScore() {
        return this.maxScore;
    }

    public Assignment maxScore(Integer maxScore) {
        this.setMaxScore(maxScore);
        return this;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public Boolean getIsPreloaded() {
        return this.isPreloaded;
    }

    public Assignment isPreloaded(Boolean isPreloaded) {
        this.setIsPreloaded(isPreloaded);
        return this;
    }

    public void setIsPreloaded(Boolean isPreloaded) {
        this.isPreloaded = isPreloaded;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Assignment appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Set<StudentClass> getStudentClasses() {
        return this.studentClasses;
    }

    public void setStudentClasses(Set<StudentClass> studentClasses) {
        this.studentClasses = studentClasses;
    }

    public Assignment studentClasses(Set<StudentClass> studentClasses) {
        this.setStudentClasses(studentClasses);
        return this;
    }

    public Assignment addStudentClasses(StudentClass studentClass) {
        this.studentClasses.add(studentClass);
        return this;
    }

    public Assignment removeStudentClasses(StudentClass studentClass) {
        this.studentClasses.remove(studentClass);
        return this;
    }

    public Set<UserQuestion> getUserQuestions() {
        return this.userQuestions;
    }

    public void setUserQuestions(Set<UserQuestion> userQuestions) {
        if (this.userQuestions != null) {
            this.userQuestions.forEach(i -> i.setAssignment(null));
        }
        if (userQuestions != null) {
            userQuestions.forEach(i -> i.setAssignment(this));
        }
        this.userQuestions = userQuestions;
    }

    public Assignment userQuestions(Set<UserQuestion> userQuestions) {
        this.setUserQuestions(userQuestions);
        return this;
    }

    public Assignment addUserQuestions(UserQuestion userQuestion) {
        this.userQuestions.add(userQuestion);
        userQuestion.setAssignment(this);
        return this;
    }

    public Assignment removeUserQuestions(UserQuestion userQuestion) {
        this.userQuestions.remove(userQuestion);
        userQuestion.setAssignment(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assignment)) {
            return false;
        }
        return getId() != null && getId().equals(((Assignment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assignment{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", difficulty='" + getDifficulty() + "'" +
            ", description='" + getDescription() + "'" +
            ", language='" + getLanguage() + "'" +
            ", testCases='" + getTestCases() + "'" +
            ", maxScore=" + getMaxScore() +
            ", isPreloaded='" + getIsPreloaded() + "'" +
            "}";
    }
}
