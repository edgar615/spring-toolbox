package com.github.edgar615.spring.web;

import com.google.common.base.Strings;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * 没起作用，还有继续研究.
 *
 * @author Edgar  Date 2018/6/27
 */
@Deprecated
public class ApiBasePathEnableAutoConfig implements ImportBeanDefinitionRegistrar {

  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                      BeanDefinitionRegistry registry) {
    Map<String, Object> map = importingClassMetadata
            .getAnnotationAttributes(EnableApiBasePath.class.getName());
    AnnotationAttributes attributes = AnnotationAttributes.fromMap(map);
    String path = attributes.getString("value");
    if (Strings.isNullOrEmpty(path)) {
      return;
    }
    BeanDefinitionBuilder.genericBeanDefinition(WebMvcRegistrations.class,
                                                () -> new ApiBasePathWebMvcRegistrations(path));
  }

}
