package com.dingjianjun.basetech.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author : Jianjun.Ding
 * @description: 安全检查切面
 * @date 2020/6/16
 */
@Aspect
@Component
@Slf4j
@Order(100)
public class SecurityAspect {
    /**
     * 目标方法正常执行：Before -> target method -> After -> AfterReturing
     * 目标方法执行异常：Before -> target method -> After -> AfterThrowing
     *
     * 环绕通知：
     * 环绕通知在执行的时候是优先于普通通知的
     * 如果是正常结束，那么执行顺序是：
     *     环绕前置通知 -> Before -> target method -> 环绕后置通知 -> 环绕返回通知 -> After -> AfterReturning
     * 如果是异常结束，那么执行顺序是：
     *     环绕前置通知 -> Before -> target method -> 环绕异常通知 -> 环绕返回通知 -> After -> AfterReturing
     * 如果出现异常的时候，在环绕通知中解决了，那么普通通知是接受不到的，如果想让普通通知接收到需要进行抛出 throw throwable
     * 执行顺序改为：
     *     环绕前置通知 -> Before -> target method -> 环绕异常通知 -> 环绕返回通知 -> After -> AfterThrowing
     *
     */
    @Pointcut("execution(* com.dingjianjun.basetech.service..*(..))")
    public void securityPointCut() {}

    /**
     * 前置通知：目标方法执行之前执行通知
     * @return
     */
    @Before("securityPointCut()")
    public void start(JoinPoint jp) {
        Signature signature = jp.getSignature();
        Object[] args = jp.getArgs();
        log.info("security>>> {} 方法执行before, 方法参数：{}", signature.getName(), Arrays.asList(args));
    }

    /**
     * 后置通知：目标方法执行之后执行通知，无论目标方法是否成功执行完成
     * @param jp
     */
    @After("securityPointCut()")
    public void securityFinally(JoinPoint jp) {
        Signature signature = jp.getSignature();
        Object[] args = jp.getArgs();
        log.info("security>>> {} 方法执行完成after, 方法参数：{}", signature.getName(), Arrays.asList(args));
    }

    /**
     * 后置返回通知：目标方法成功执行完成后执行通知
     * @param jp
     * @param retVal
     */
    @AfterReturning(value = "securityPointCut()", returning = "retVal")
    public void stop(JoinPoint jp, Object retVal) {
        Signature signature = jp.getSignature();
        Object[] args = jp.getArgs();
        log.info("security>>> {} 方法成功执行完成afterReturing, 方法参数：{}, 结果：{}", signature.getName(), Arrays.asList(args), retVal);
    }

    /**
     * 后置异常通知：目标方法执行过程中出现异常执行通知
     * @param jp
     * @param e
     */
    @AfterThrowing(value = "securityPointCut()", throwing = "e")
    public void securityException(JoinPoint jp, Exception e) {
        Signature signature = jp.getSignature();
        Object[] args = jp.getArgs();
        log.info("security>>> {} 方法执行异常afterThrowing, 方法参数：{}, 异常：{}", signature.getName(), Arrays.asList(args), e.getMessage());
    }

    /**
     * 环绕通知：环绕通知可以在方法调用前后完成自定义的行为。
     * 它可以选择是否继续执行连接点或直接返回自定义的返回值又或抛出异常将执行结束。
     * @param pjp
     * @return
     */
    @Around("securityPointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        Object[] args = pjp.getArgs();
        Object result = null;
        log.info("security>>> {} 方法执行之前around, 方法参数：{}", signature.getName(), Arrays.asList(args));
        try {
            result = pjp.proceed(args);
            log.info("security>>> {} 方法执行正常完成返回around, 方法参数：{}, 结果：{}", signature.getName(), Arrays.asList(args), result);
        } catch (Throwable throwable) {
            log.info("security>>> {} 方法执行异常around, 方法参数：{}, 异常：{}", signature.getName(), Arrays.asList(args), throwable.getMessage());
            throw throwable;
        } finally {
            log.info("security>>> {} 方法执行完成返回前around, 方法参数：{}", signature.getName(), Arrays.asList(args));
        }

        return result;
    }

}
