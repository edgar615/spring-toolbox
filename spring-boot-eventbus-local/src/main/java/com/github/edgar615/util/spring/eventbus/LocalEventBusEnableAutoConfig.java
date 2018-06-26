package com.github.edgar615.util.spring.eventbus;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Edgar on 2018/6/26.
 *
 * @author Edgar  Date 2018/6/26
 */
public class LocalEventBusEnableAutoConfig implements ImportSelector {

  @Override
  public String[] selectImports(AnnotationMetadata importingClassMetadata) {
    Map<String, Object> map = importingClassMetadata
            .getAnnotationAttributes(EnableLocalEventBus.class.getName());
    AnnotationAttributes attributes = AnnotationAttributes.fromMap(map);
    List<String> list = new ArrayList<>();
    if (attributes.getBoolean("producer")) {
      list.add(SpringEventProducer.class.getName());
    }
    if (attributes.getBoolean("consumer")) {
      list.add(SpringEventConsumer.class.getName());
    }
    return list.toArray(new String[list.size()]);
  }

  private GenericBeanDefinition createBeanDefinition(Class<?> beanClass) {
    GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
    beanDefinition.setBeanClass(beanClass);
    beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_NO);
    return beanDefinition;
  }
}
