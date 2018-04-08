package com.github.edgar615.util.spring.jwt;

import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by Edgar on 2017/11/14.
 *
 * @author Edgar  Date 2017/11/14
 */
public class JwtTest {

  @Test
  public void testGenerateToken() {
    JwtProperty jwtProperty = new JwtProperty();
    jwtProperty.setExpired(1000);
    jwtProperty.setSecret("secret");
    JwtProvider jwtProvider = new JwtProviderImpl(jwtProperty);
    PrincipalImpl loginUser = new PrincipalImpl();
    loginUser.setUserId(1l);
    String token = jwtProvider.generateToken(loginUser);
    System.out.println(token);
    Principal vertfiyUser = jwtProvider.decodeUser(token);
    Assert.assertEquals(loginUser.getUserId(), vertfiyUser.getUserId());
    Assert.assertNull(vertfiyUser.getCompanyCode());
    Assert.assertNull(vertfiyUser.getJti());
  }

  @Test(expected = SystemException.class)
  public void testExpired() {
    JwtProperty jwtProperty = new JwtProperty();
    jwtProperty.setExpired(100);
    jwtProperty.setSecret("secret");
    JwtProvider jwtProvider = new JwtProviderImpl(jwtProperty);
    PrincipalImpl loginUser = new PrincipalImpl();
    loginUser.setUserId(1l);
    String token = jwtProvider.generateToken(loginUser);
    System.out.println(token);
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Principal vertfiyUser = null;
    try {
      vertfiyUser = jwtProvider.decodeUser(token);
    } catch (Exception e) {
      SystemException se = (SystemException) e;
      Assert.assertEquals(DefaultErrorCode.EXPIRE_TOKEN, se.getErrorCode());
      throw e;
    }
    Assert.fail();
  }

  @Test(expected = SystemException.class)
  public void testInvalidSign() {
    JwtProperty jwtProperty = new JwtProperty();
    jwtProperty.setExpired(1000);
    jwtProperty.setSecret("secret");
    JwtProvider jwtProvider = new JwtProviderImpl(jwtProperty);
    PrincipalImpl loginUser = new PrincipalImpl();
    loginUser.setUserId(1l);
    String token = jwtProvider.generateToken(loginUser);
    System.out.println(token);
    Principal vertfiyUser = null;
    try {
      vertfiyUser = jwtProvider.decodeUser(token + 1);
    } catch (Exception e) {
      SystemException se = (SystemException) e;
      Assert.assertEquals(DefaultErrorCode.INVALID_TOKEN, se.getErrorCode());
      throw e;
    }
    Assert.fail();
  }

  @Test(expected = SystemException.class)
  public void testInvalidIssuer() {
    JwtProperty jwtProperty = new JwtProperty();
    jwtProperty.setExpired(1000);
    jwtProperty.setSecret("secret");
    jwtProperty.setIssuer("test");
    JwtProvider jwtProvider = new JwtProviderImpl(jwtProperty);
    PrincipalImpl loginUser = new PrincipalImpl();
    loginUser.setUserId(1l);
    String token = jwtProvider.generateToken(loginUser);
    System.out.println(token);
    jwtProperty.setIssuer("haha");
     jwtProvider = new JwtProviderImpl(jwtProperty);
    Principal vertfiyUser = null;
    try {
      vertfiyUser = jwtProvider.decodeUser(token);
    } catch (Exception e) {
      SystemException se = (SystemException) e;
      Assert.assertEquals(DefaultErrorCode.INVALID_TOKEN, se.getErrorCode());
      throw e;
    }
    Assert.fail();
  }
}
