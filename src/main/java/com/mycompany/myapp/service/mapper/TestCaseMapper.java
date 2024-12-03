package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.TestCase;
import com.mycompany.myapp.service.dto.QuestionDTO;
import com.mycompany.myapp.service.dto.TestCaseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TestCase} and its DTO {@link TestCaseDTO}.
 */
@Mapper(componentModel = "spring")
public interface TestCaseMapper extends EntityMapper<TestCaseDTO, TestCase> {
    @Mapping(target = "question", source = "question", qualifiedByName = "questionId")
    TestCaseDTO toDto(TestCase s);

    @Named("questionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    QuestionDTO toDtoQuestionId(Question question);
}
