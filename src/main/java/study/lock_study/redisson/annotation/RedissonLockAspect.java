package study.lock_study.redisson.annotation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RedissonLockAspect {

    private final RedissonClient redissonClient;

    // @Around : 지정된 패턴에 해당하는 메서드가 실행되기전, 후에 모두 동작
    // 지금은 우리가 만든 인터페이스가 실행되기전,후 이걸 실행하게 만드는거임
    @Around("@annotation(study.lock_study.redisson.annotation.RedissonLock)")
    public void redissonLock(ProceedingJoinPoint joinPoint) throws Throwable {
        // 현재 메서드의 시그니처를 가져옴 ( 이름, 반환타입, 매개변수 등)
        // 이때 MethodSignature로 가져오면 메서드 관련 추가정보에 접근 가능
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock annotation = method.getAnnotation(RedissonLock.class);
        String lockKey = method.getName() + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), annotation.value());

        RLock lock = redissonClient.getLock(lockKey);

        try{
            boolean lockable = lock.tryLock(annotation.waitTime(), annotation.leaseTime(), TimeUnit.MILLISECONDS);
            if(!lockable){
                log.info("Lock 획득 실패={}", lockKey);
                return;
            }
            log.info("로직 수행");
            joinPoint.proceed();
        }catch (InterruptedException e){
            log.info("에러 발생");
            throw e;
        }finally{
            log.info("락 해제");
            lock.unlock();
        }

    }
}
