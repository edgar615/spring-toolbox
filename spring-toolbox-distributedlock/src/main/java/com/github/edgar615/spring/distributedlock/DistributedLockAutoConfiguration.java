package com.github.edgar615.spring.distributedlock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(DistributedLockManager.class)
public class DistributedLockAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(DistributedLockManager.class)
    public DistributedLockAspect distributedLockAspect(DistributedLockManager distributedLockManager) {
        return new DistributedLockAspect(distributedLockManager);
    }
}
