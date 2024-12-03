package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.AppUserAsserts.*;
import static com.mycompany.myapp.domain.AppUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppUserMapperTest {

    private AppUserMapper appUserMapper;

    @BeforeEach
    void setUp() {
        appUserMapper = new AppUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAppUserSample1();
        var actual = appUserMapper.toEntity(appUserMapper.toDto(expected));
        assertAppUserAllPropertiesEquals(expected, actual);
    }
}
