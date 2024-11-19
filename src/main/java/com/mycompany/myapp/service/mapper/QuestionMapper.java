package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.StudentClass;
import com.mycompany.myapp.service.dto.QuestionDTO;
import com.mycompany.myapp.service.dto.StudentClassDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Question} and its DTO {@link QuestionDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {
    @Mapping(target = "studentClass", source = "studentClass", qualifiedByName = "studentClassId")
    QuestionDTO toDto(Question s);

    @Named("studentClassId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StudentClassDTO toDtoStudentClassId(StudentClass studentClass);
}
