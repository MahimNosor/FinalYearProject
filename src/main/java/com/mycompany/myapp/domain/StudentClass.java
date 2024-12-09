package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StudentClass.
 */
@Entity
@Table(name = "student_class")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "studentclass")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "class_name", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String className;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "classes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "classes" }, allowSetters = true)
    private Set<AppUser> users = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = true)
    private AppUser appUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StudentClass id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return this.className;
    }

    public StudentClass className(String className) {
        this.setClassName(className);
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Set<AppUser> getUsers() {
        return this.users;
    }

    public void setUsers(Set<AppUser> appUsers) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeClasses(this));
        }
        if (appUsers != null) {
            appUsers.forEach(i -> i.addClasses(this));
        }
        this.users = appUsers;
    }

    public StudentClass users(Set<AppUser> appUsers) {
        this.setUsers(appUsers);
        return this;
    }

    public StudentClass addUsers(AppUser appUser) {
        this.users.add(appUser);
        appUser.getClasses().add(this);
        return this;
    }

    public StudentClass removeUsers(AppUser appUser) {
        this.users.remove(appUser);
        appUser.getClasses().remove(this);
        return this;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
    
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentClass)) {
            return false;
        }
        return getId() != null && getId().equals(((StudentClass) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentClass{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            "}";
    }
}
