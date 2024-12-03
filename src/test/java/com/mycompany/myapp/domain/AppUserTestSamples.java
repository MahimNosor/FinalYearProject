package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AppUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AppUser getAppUserSample1() {
        return new AppUser().id(1L).name("name1").email("email1").password("password1").roles("roles1").points(1);
    }

    public static AppUser getAppUserSample2() {
        return new AppUser().id(2L).name("name2").email("email2").password("password2").roles("roles2").points(2);
    }

    public static AppUser getAppUserRandomSampleGenerator() {
        return new AppUser()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .roles(UUID.randomUUID().toString())
            .points(intCount.incrementAndGet());
    }
}
