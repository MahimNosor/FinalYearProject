package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AppUserTestSamples.*;
import static com.mycompany.myapp.domain.StudentClassTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AppUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUser.class);
        AppUser appUser1 = getAppUserSample1();
        AppUser appUser2 = new AppUser();
        assertThat(appUser1).isNotEqualTo(appUser2);

        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);

        appUser2 = getAppUserSample2();
        assertThat(appUser1).isNotEqualTo(appUser2);
    }

    @Test
    void classesTest() {
        AppUser appUser = getAppUserRandomSampleGenerator();
        StudentClass studentClassBack = getStudentClassRandomSampleGenerator();

        appUser.addClasses(studentClassBack);
        assertThat(appUser.getClasses()).containsOnly(studentClassBack);

        appUser.removeClasses(studentClassBack);
        assertThat(appUser.getClasses()).doesNotContain(studentClassBack);

        appUser.classes(new HashSet<>(Set.of(studentClassBack)));
        assertThat(appUser.getClasses()).containsOnly(studentClassBack);

        appUser.setClasses(new HashSet<>());
        assertThat(appUser.getClasses()).doesNotContain(studentClassBack);
    }
}
