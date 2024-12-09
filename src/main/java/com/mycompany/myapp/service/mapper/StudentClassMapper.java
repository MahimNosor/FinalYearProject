package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.domain.StudentClass;
import com.mycompany.myapp.service.dto.AppUserDTO;
import com.mycompany.myapp.service.dto.StudentClassDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StudentClass} and its DTO {@link StudentClassDTO}.
 */
@Mapper(componentModel = "spring")
public interface StudentClassMapper extends EntityMapper<StudentClassDTO, StudentClass> {
    @Mapping(target = "users", source = "users", qualifiedByName = "appUserIdSet")
    @Mapping(target = "appUserId", source = "appUser.id")
    StudentClassDTO toDto(StudentClass s);

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "removeUsers", ignore = true)
    @Mapping(target = "appUser", source = "appUserId")
    StudentClass toEntity(StudentClassDTO studentClassDTO);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);

    @Named("appUserIdSet")
    default Set<AppUserDTO> toDtoAppUserIdSet(Set<AppUser> appUser) {
        return appUser.stream().map(this::toDtoAppUserId).collect(Collectors.toSet());
    }

    // Manual mapping method for Long to AppUser
    default AppUser map(Long value) {
        if (value == null) {
            return null;
        }
        AppUser appUser = new AppUser();
        appUser.setId(value);
        return appUser;
    }

    // Manual mapping method for AppUser to Long
    default Long map(AppUser appUser) {
        return appUser == null ? null : appUser.getId();
    }
}
