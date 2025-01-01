package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.AssignmentAsserts.*;
import static com.mycompany.myapp.domain.AssignmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssignmentMapperTest {

    private AssignmentMapper assignmentMapper;

    @BeforeEach
    void setUp() {
        assignmentMapper = new AssignmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssignmentSample1();
        var actual = assignmentMapper.toEntity(assignmentMapper.toDto(expected));
        assertAssignmentAllPropertiesEquals(expected, actual);
    }
}
