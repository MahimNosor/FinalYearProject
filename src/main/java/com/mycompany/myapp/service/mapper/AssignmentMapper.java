package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.domain.Assignment;
import com.mycompany.myapp.domain.StudentClass;
import com.mycompany.myapp.service.dto.AppUserDTO;
import com.mycompany.myapp.service.dto.AssignmentDTO;
import com.mycompany.myapp.service.dto.StudentClassDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Assignment} and its DTO {@link AssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssignmentMapper extends EntityMapper<AssignmentDTO, Assignment> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "appUserName")
    @Mapping(target = "studentClasses", source = "studentClasses", qualifiedByName = "studentClassClassNameSet")
    AssignmentDTO toDto(Assignment s);

    @Mapping(target = "removeStudentClasses", ignore = true)
    Assignment toEntity(AssignmentDTO assignmentDTO);

    @Named("appUserName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AppUserDTO toDtoAppUserName(AppUser appUser);

    @Named("studentClassClassName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "className", source = "className")
    StudentClassDTO toDtoStudentClassClassName(StudentClass studentClass);

    @Named("studentClassClassNameSet")
    default Set<StudentClassDTO> toDtoStudentClassClassNameSet(Set<StudentClass> studentClass) {
        return studentClass.stream().map(this::toDtoStudentClassClassName).collect(Collectors.toSet());
    }
}
