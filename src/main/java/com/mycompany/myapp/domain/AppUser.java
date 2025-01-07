package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "appuser")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "name", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String email;

    @NotNull
    @JsonIgnore
    @Column(name = "password", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String password;

    @NotNull
    @Column(name = "roles", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String roles;

    @Column(name = "points")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer points;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_app_user__classes",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "classes_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "users", "assignments" }, allowSetters = true)
    private Set<StudentClass> classes = new HashSet<>();


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "appUser", "studentClasses", "userQuestions" }, allowSetters = true)
    private Set<Assignment> assignments = new HashSet<>();


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Other fields and getters/setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return this.name;
    }

    public AppUser name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public AppUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public AppUser password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return this.roles;
    }

    public AppUser roles(String roles) {
        this.setRoles(roles);
        return this;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Integer getPoints() {
        return this.points;
    }

    public AppUser points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Set<StudentClass> getClasses() {
        return this.classes;
    }

    public void setClasses(Set<StudentClass> studentClasses) {
        this.classes = studentClasses;
    }

    public AppUser classes(Set<StudentClass> studentClasses) {
        this.setClasses(studentClasses);
        return this;
    }

    public AppUser addClasses(StudentClass studentClass) {
        this.classes.add(studentClass);
        return this;
    }

    public AppUser removeClasses(StudentClass studentClass) {
        this.classes.remove(studentClass);
        return this;
    }
    public Set<Assignment> getAssignments() {
        return this.assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        if (this.assignments != null) {
            this.assignments.forEach(i -> i.setAppUser(null));
        }
        if (assignments != null) {
            assignments.forEach(i -> i.setAppUser(this));
        }
        this.assignments = assignments;
    }

    public AppUser assignments(Set<Assignment> assignments) {
        this.setAssignments(assignments);
        return this;
    }

    public AppUser addAssignments(Assignment assignment) {
        this.assignments.add(assignment);
        assignment.setAppUser(this);
        return this;
    }

    public AppUser removeAssignments(Assignment assignment) {
        this.assignments.remove(assignment);
        assignment.setAppUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return getId() != null && getId().equals(((AppUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", roles='" + getRoles() + "'" +
            ", points=" + getPoints() +
            "}";
    }
}
