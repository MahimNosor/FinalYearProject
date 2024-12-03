package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.domain.StudentClass;
import com.mycompany.myapp.service.dto.AppUserDTO;
import com.mycompany.myapp.service.dto.StudentClassDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "classes", source = "classes", qualifiedByName = "studentClassIdSet")
    AppUserDTO toDto(AppUser s);

    @Mapping(target = "removeClasses", ignore = true)
    AppUser toEntity(AppUserDTO appUserDTO);

    @Named("studentClassId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudentClassDTO toDtoStudentClassId(StudentClass studentClass);

    @Named("studentClassIdSet")
    default Set<StudentClassDTO> toDtoStudentClassIdSet(Set<StudentClass> studentClass) {
        return studentClass.stream().map(this::toDtoStudentClassId).collect(Collectors.toSet());
    }
}
