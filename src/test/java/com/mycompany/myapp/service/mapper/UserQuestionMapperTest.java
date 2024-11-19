package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.UserQuestionAsserts.*;
import static com.mycompany.myapp.domain.UserQuestionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserQuestionMapperTest {

    private UserQuestionMapper userQuestionMapper;

    @BeforeEach
    void setUp() {
        userQuestionMapper = new UserQuestionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserQuestionSample1();
        var actual = userQuestionMapper.toEntity(userQuestionMapper.toDto(expected));
        assertUserQuestionAllPropertiesEquals(expected, actual);
    }
}
