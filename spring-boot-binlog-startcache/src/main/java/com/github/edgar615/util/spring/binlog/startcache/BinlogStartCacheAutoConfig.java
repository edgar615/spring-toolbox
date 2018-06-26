package com.github.edgar615.util.spring.binlog.startcache;

import com.github.edgar615.util.spring.cache.StartCacheManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 因为依赖StartCacheManager导致失败，还未找到解决方法.
 *
 * @author Edgar  Date 2018/6/26
 */
@Deprecated
public class BinlogStartCacheAutoConfig implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

  private BeanFactory beanFactory;

  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                      BeanDefinitionRegistry registry) {
    registry.registerBeanDefinition("binlogStartCacheEventListener", createBean());
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  private GenericBeanDefinition createBean() {
    GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
    ConstructorArgumentValues values = new ConstructorArgumentValues();
    StartCacheManager cacheManager = beanFactory.getBean(StartCacheManager.class);
    values.addIndexedArgumentValue(0, cacheManager);
    beanDefinition.setConstructorArgumentValues(values);
    beanDefinition.setBeanClass(BinlogStartCacheEventListener.class);
    beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_NO);
    return beanDefinition;
  }
}
