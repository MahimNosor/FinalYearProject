package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.StudentClass} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StudentClassDTO implements Serializable {

    private Long id;

    @NotNull
    private String className;

    private Set<AppUserDTO> users = new HashSet<>();

    private Set<AssignmentDTO> assignments = new HashSet<>();

    private Long appUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Set<AppUserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<AppUserDTO> users) {
        this.users = users;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public Set<AssignmentDTO> getAssignments() {
        return assignments;
     }
    public void setAssignments(Set<AssignmentDTO> assignments) {
        this.assignments = assignments;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentClassDTO)) {
            return false;
        }

        StudentClassDTO studentClassDTO = (StudentClassDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, studentClassDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StudentClassDTO{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", users=" + getUsers() +
            "}";
    }
}
