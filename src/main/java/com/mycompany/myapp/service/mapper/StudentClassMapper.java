package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.domain.StudentClass;
import com.mycompany.myapp.service.dto.AppUserDTO;
import com.mycompany.myapp.service.dto.StudentClassDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import com.mycompany.myapp.domain.Assignment;
import com.mycompany.myapp.service.dto.AssignmentDTO;

/**
 * Mapper for the entity {@link StudentClass} and its DTO {@link StudentClassDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentClassMapper extends EntityMapper<StudentClassDTO, StudentClass> {
    @Mapping(target = "users", source = "users", qualifiedByName = "appUserIdSet")
    @Mapping(target = "appUserId", source = "appUser.id")
    @Mapping(target = "assignments", source = "assignments", qualifiedByName = "assignmentIdSet")
    StudentClassDTO toDto(StudentClass s);

    @Mapping(target = "users", source = "users", qualifiedByName = "toEntityAppUserIdSet")
    @Mapping(target = "removeUsers", ignore = true)
    @Mapping(target = "appUser", source = "appUserId")
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "removeAssignments", ignore = true)
    StudentClass toEntity(StudentClassDTO studentClassDTO);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);

    @Named("appUserIdSet")
    default Set<AppUserDTO> toDtoAppUserIdSet(Set<AppUser> appUser) {
        return appUser.stream().map(this::toDtoAppUserId).collect(Collectors.toSet());
    }

    @Named("toEntityAppUserIdSet")
    default Set<AppUser> toEntityAppUserIdSet(Set<AppUserDTO> appUserDTOs) {
        return appUserDTOs.stream()
            .map(this::toEntityAppUserId)
            .collect(Collectors.toSet());
    }

    @Named("toEntityAppUserId")
    @Mapping(target = "id", source = "id")
    AppUser toEntityAppUserId(AppUserDTO appUserDTO);

    // Manual mapping method for Long to AppUser
    default AppUser map(Long value) {
        if (value == null) {
            return null;
        }
        AppUser appUser = new AppUser();
        appUser.setId(value);
        return appUser;
    }

    @Named("assignmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AssignmentDTO toDtoAssignmentId(Assignment assignment);

    default Long map(AppUser appUser) {
        return appUser == null ? null : appUser.getId();
    }

    @Named("assignmentIdSet")
    default Set<AssignmentDTO> toDtoAssignmentIdSet(Set<Assignment> assignment) {
        return assignment.stream().map(this::toDtoAssignmentId).collect(Collectors.toSet());
    }
}

