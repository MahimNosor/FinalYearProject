package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.ProgrammingLanguage;
import com.mycompany.myapp.domain.enumeration.difficulty;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Assignment} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AssignmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /assignments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssignmentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering difficulty
     */
    public static class difficultyFilter extends Filter<difficulty> {

        public difficultyFilter() {}

        public difficultyFilter(difficultyFilter filter) {
            super(filter);
        }

        @Override
        public difficultyFilter copy() {
            return new difficultyFilter(this);
        }
    }

    /**
     * Class for filtering ProgrammingLanguage
     */
    public static class ProgrammingLanguageFilter extends Filter<ProgrammingLanguage> {

        public ProgrammingLanguageFilter() {}

        public ProgrammingLanguageFilter(ProgrammingLanguageFilter filter) {
            super(filter);
        }

        @Override
        public ProgrammingLanguageFilter copy() {
            return new ProgrammingLanguageFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private difficultyFilter difficulty;

    private ProgrammingLanguageFilter language;

    private IntegerFilter maxScore;

    private BooleanFilter isPreloaded;

    private LongFilter appUserId;

    private LongFilter studentClassesId;

    private LongFilter userQuestionsId;

    private Boolean distinct;

    public AssignmentCriteria() {}

    public AssignmentCriteria(AssignmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.difficulty = other.optionalDifficulty().map(difficultyFilter::copy).orElse(null);
        this.language = other.optionalLanguage().map(ProgrammingLanguageFilter::copy).orElse(null);
        this.maxScore = other.optionalMaxScore().map(IntegerFilter::copy).orElse(null);
        this.isPreloaded = other.optionalIsPreloaded().map(BooleanFilter::copy).orElse(null);
        this.appUserId = other.optionalAppUserId().map(LongFilter::copy).orElse(null);
        this.studentClassesId = other.optionalStudentClassesId().map(LongFilter::copy).orElse(null);
        this.userQuestionsId = other.optionalUserQuestionsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AssignmentCriteria copy() {
        return new AssignmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public difficultyFilter getDifficulty() {
        return difficulty;
    }

    public Optional<difficultyFilter> optionalDifficulty() {
        return Optional.ofNullable(difficulty);
    }

    public difficultyFilter difficulty() {
        if (difficulty == null) {
            setDifficulty(new difficultyFilter());
        }
        return difficulty;
    }

    public void setDifficulty(difficultyFilter difficulty) {
        this.difficulty = difficulty;
    }

    public ProgrammingLanguageFilter getLanguage() {
        return language;
    }

    public Optional<ProgrammingLanguageFilter> optionalLanguage() {
        return Optional.ofNullable(language);
    }

    public ProgrammingLanguageFilter language() {
        if (language == null) {
            setLanguage(new ProgrammingLanguageFilter());
        }
        return language;
    }

    public void setLanguage(ProgrammingLanguageFilter language) {
        this.language = language;
    }

    public IntegerFilter getMaxScore() {
        return maxScore;
    }

    public Optional<IntegerFilter> optionalMaxScore() {
        return Optional.ofNullable(maxScore);
    }

    public IntegerFilter maxScore() {
        if (maxScore == null) {
            setMaxScore(new IntegerFilter());
        }
        return maxScore;
    }

    public void setMaxScore(IntegerFilter maxScore) {
        this.maxScore = maxScore;
    }

    public BooleanFilter getIsPreloaded() {
        return isPreloaded;
    }

    public Optional<BooleanFilter> optionalIsPreloaded() {
        return Optional.ofNullable(isPreloaded);
    }

    public BooleanFilter isPreloaded() {
        if (isPreloaded == null) {
            setIsPreloaded(new BooleanFilter());
        }
        return isPreloaded;
    }

    public void setIsPreloaded(BooleanFilter isPreloaded) {
        this.isPreloaded = isPreloaded;
    }

    public LongFilter getAppUserId() {
        return appUserId;
    }

    public Optional<LongFilter> optionalAppUserId() {
        return Optional.ofNullable(appUserId);
    }

    public LongFilter appUserId() {
        if (appUserId == null) {
            setAppUserId(new LongFilter());
        }
        return appUserId;
    }

    public void setAppUserId(LongFilter appUserId) {
        this.appUserId = appUserId;
    }

    public LongFilter getStudentClassesId() {
        return studentClassesId;
    }

    public Optional<LongFilter> optionalStudentClassesId() {
        return Optional.ofNullable(studentClassesId);
    }

    public LongFilter studentClassesId() {
        if (studentClassesId == null) {
            setStudentClassesId(new LongFilter());
        }
        return studentClassesId;
    }

    public void setStudentClassesId(LongFilter studentClassesId) {
        this.studentClassesId = studentClassesId;
    }

    public LongFilter getUserQuestionsId() {
        return userQuestionsId;
    }

    public Optional<LongFilter> optionalUserQuestionsId() {
        return Optional.ofNullable(userQuestionsId);
    }

    public LongFilter userQuestionsId() {
        if (userQuestionsId == null) {
            setUserQuestionsId(new LongFilter());
        }
        return userQuestionsId;
    }

    public void setUserQuestionsId(LongFilter userQuestionsId) {
        this.userQuestionsId = userQuestionsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AssignmentCriteria that = (AssignmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(difficulty, that.difficulty) &&
            Objects.equals(language, that.language) &&
            Objects.equals(maxScore, that.maxScore) &&
            Objects.equals(isPreloaded, that.isPreloaded) &&
            Objects.equals(appUserId, that.appUserId) &&
            Objects.equals(studentClassesId, that.studentClassesId) &&
            Objects.equals(userQuestionsId, that.userQuestionsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, difficulty, language, maxScore, isPreloaded, appUserId, studentClassesId, userQuestionsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssignmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalDifficulty().map(f -> "difficulty=" + f + ", ").orElse("") +
            optionalLanguage().map(f -> "language=" + f + ", ").orElse("") +
            optionalMaxScore().map(f -> "maxScore=" + f + ", ").orElse("") +
            optionalIsPreloaded().map(f -> "isPreloaded=" + f + ", ").orElse("") +
            optionalAppUserId().map(f -> "appUserId=" + f + ", ").orElse("") +
            optionalStudentClassesId().map(f -> "studentClassesId=" + f + ", ").orElse("") +
            optionalUserQuestionsId().map(f -> "userQuestionsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
