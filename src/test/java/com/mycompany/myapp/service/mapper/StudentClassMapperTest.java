package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.StudentClassAsserts.*;
import static com.mycompany.myapp.domain.StudentClassTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentClassMapperTest {

    private StudentClassMapper studentClassMapper;

    @BeforeEach
    void setUp() {
        studentClassMapper = new StudentClassMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStudentClassSample1();
        var actual = studentClassMapper.toEntity(studentClassMapper.toDto(expected));
        assertStudentClassAllPropertiesEquals(expected, actual);
    }
}
