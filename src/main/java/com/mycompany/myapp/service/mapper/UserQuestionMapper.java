package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AppUser;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.UserQuestion;
import com.mycompany.myapp.service.dto.AppUserDTO;
import com.mycompany.myapp.service.dto.QuestionDTO;
import com.mycompany.myapp.service.dto.UserQuestionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserQuestion} and its DTO {@link UserQuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserQuestionMapper extends EntityMapper<UserQuestionDTO, UserQuestion> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "appUserId")
    @Mapping(target = "question", source = "question", qualifiedByName = "questionId")
    UserQuestionDTO toDto(UserQuestion s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);

    @Named("questionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoQuestionId(Question question);
}
