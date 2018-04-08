package com.github.edgar615.util.spring.jdbc;

import com.github.edgar615.util.db.Jdbc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2017/10/7.
 *
 * @ConditionalOnBean：当容器里有指定的Bean 的条件下。
 * @ConditionalOnClass：当类路径下有指定的类的条件下。
 * @ConditionalOnExpression：基于SpEL 表达式作为判断条件。
 * @ConditionalOnJava：基于JVM 版本作为判断条件。
 * @ConditionalOnJndi：在JNDI 存在的条件下查找指定的位置。
 * @ConditionalOnMissingBean：当容器里没有指定Bean 的情况下。
 * @ConditionalOnMissingClass：当类路径下没有指定的类的条件下。
 * @ConditionalOnNotWebApplication：当前项目不是Web 项目的条件下。
 * @ConditionalOnProperty：指定的属性是否有指定的值。
 * @ConditionalOnResource：类路径是否有指定的值。
 * @ConditionalOnSingleCandidate：当指定Bean 在容器中只有一个，或者虽然有多个但是指定首选的Bean。
 * @ConditionalOnWebApplication：当前项目是Web 项目的条件下。
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class JdbcAutoConfiguration {

  @Autowired
  private DataSource dataSource;

  @Bean
  @ConditionalOnMissingBean(Jdbc.class)
  public Jdbc jdbc() {
    return new JdbcImpl(dataSource);
  }

//  @Bean
//  @ConditionalOnMissingBean(HelloService.class)//7当不存在HelloService的bean时候，新建一个HelloService的bean
//  public HelloService helloService() throws JsonProcessingException {
//    HelloService helloService = new HelloService();
//    helloService.setMsg(authorPropertis.getName() +" :" + authorPropertis.getPwd());
//
//    ObjectMapper objectMapper = new ObjectMapper();
//    System.out.println("arrayProps: " + objectMapper.writeValueAsString(authorPropertis.getArrayProps()));
//    System.out.println("listProp1: " + objectMapper.writeValueAsString(authorPropertis.getListProp1()));
//    System.out.println("listProp2: " + objectMapper.writeValueAsString(authorPropertis.getListProp2()));
//    System.out.println("mapProps: " + objectMapper.writeValueAsString(authorPropertis.getMapProps()));
//    System.out.println("Props: " + objectMapper.writeValueAsString(authorPropertis.getProperties()));
//    return helloService;
//  }
}
