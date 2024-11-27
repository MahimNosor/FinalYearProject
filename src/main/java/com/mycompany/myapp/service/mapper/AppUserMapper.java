package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.domain.StudentClass;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.service.dto.AppUserDTO;
import com.mycompany.myapp.service.dto.StudentClassDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    // Mapping AppUser to DTO
    @Mapping(source = "user.id", target = "userId") // Map User to userId in DTO
    @Mapping(source = "user.login", target = "login") // NEW: Map User login to login in DTO
    @Mapping(target = "classes", source = "classes", qualifiedByName = "studentClassIdSet") // Map StudentClass to DTO
    AppUserDTO toDto(AppUser appUser);

    // Mapping DTO to AppUser
    @Mapping(source = "userId", target = "user") // Map userId in DTO to User entity
    @Mapping(target = "removeClasses", ignore = true) // Ignore removeClasses method
    AppUser toEntity(AppUserDTO appUserDTO);

    // Map StudentClass to DTO for nested mappings
    @Named("studentClassId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudentClassDTO toDtoStudentClassId(StudentClass studentClass);

    // Map a Set of StudentClass to DTOs
    @Named("studentClassIdSet")
    default Set<StudentClassDTO> toDtoStudentClassIdSet(Set<StudentClass> studentClass) {
        return studentClass.stream().map(this::toDtoStudentClassId).collect(Collectors.toSet());
    }
}
