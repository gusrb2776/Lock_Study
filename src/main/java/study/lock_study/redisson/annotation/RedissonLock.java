package study.lock_study.redisson.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedissonLock {

    String value(); // Lock의 이름 (고유값)
    long waitTime() default 5000L; // Lock 획득을 위한 최대 대기시간 (ms)
    long leaseTime() default 2000L; // Lock 점유 최대 시간 (ms)
}
