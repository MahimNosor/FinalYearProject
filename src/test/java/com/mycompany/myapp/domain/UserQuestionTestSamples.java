package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserQuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserQuestion getUserQuestionSample1() {
        return new UserQuestion().id(1L).score(1);
    }

    public static UserQuestion getUserQuestionSample2() {
        return new UserQuestion().id(2L).score(2);
    }

    public static UserQuestion getUserQuestionRandomSampleGenerator() {
        return new UserQuestion().id(longCount.incrementAndGet()).score(intCount.incrementAndGet());
    }
}
