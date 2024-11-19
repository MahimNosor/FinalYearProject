package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.TestCaseAsserts.*;
import static com.mycompany.myapp.domain.TestCaseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestCaseMapperTest {

    private TestCaseMapper testCaseMapper;

    @BeforeEach
    void setUp() {
        testCaseMapper = new TestCaseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTestCaseSample1();
        var actual = testCaseMapper.toEntity(testCaseMapper.toDto(expected));
        assertTestCaseAllPropertiesEquals(expected, actual);
    }
}
