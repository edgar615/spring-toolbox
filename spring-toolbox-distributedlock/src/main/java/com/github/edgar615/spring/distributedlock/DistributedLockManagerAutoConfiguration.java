package com.github.edgar615.spring.distributedlock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(DistributedLockProvider.class)
public class DistributedLockManagerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(DistributedLockProvider.class)
    public DistributedLockManager distributedLockManager(DistributedLockProvider distributedLockProvider) {
        return new SimpleDistributedLockManager(distributedLockProvider);
    }
}
