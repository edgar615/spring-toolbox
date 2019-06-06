package com.github.edgar615.util.spring.jwt;

import com.github.edgar615.util.base.Randoms;
import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Test;

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
    jwtProperty.setSecret(Randoms.randomAlphabet(10));
    jwtProperty.setSensitiveSecret(Randoms.randomAlphabet(10));
    String identifier = Randoms.randomAlphabetAndNum(6);
    JwtProvider jwtProvider = new JwtProviderImpl(jwtProperty);
    Token token = jwtProvider.generateToken(identifier);
    System.out.println(token);
    String decodeIdentifier = jwtProvider.decodeToken(token.getToken());
    Assert.assertEquals(identifier, decodeIdentifier);
  }

  @Test(expected = SystemException.class)
  public void testExpired() {
    JwtProperty jwtProperty = new JwtProperty();
    jwtProperty.setExpired(100);
    jwtProperty.setSecret(Randoms.randomAlphabet(10));
    jwtProperty.setSensitiveSecret(Randoms.randomAlphabet(10));
    String identifier = Randoms.randomAlphabetAndNum(6);
    JwtProvider jwtProvider = new JwtProviderImpl(jwtProperty);
    Token token = jwtProvider.generateToken(identifier);
    System.out.println(token);
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    try {
      jwtProvider.decodeToken(token.getToken());
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
    jwtProperty.setSecret(Randoms.randomAlphabet(10));
    jwtProperty.setSensitiveSecret(Randoms.randomAlphabet(10));
    String identifier = Randoms.randomAlphabetAndNum(6);
    JwtProvider jwtProvider = new JwtProviderImpl(jwtProperty);
    Token token = jwtProvider.generateToken(identifier);
    System.out.println(token);
    try {
      jwtProvider.decodeToken(token.getToken() + 1);
    } catch (Exception e) {
      SystemException se = (SystemException) e;
      Assert.assertEquals(DefaultErrorCode.INVALID_TOKEN, se.getErrorCode());
      throw e;
    }
  }

  @Test(expected = SystemException.class)
  public void testInvalidIssuer() {
    JwtProperty jwtProperty = new JwtProperty();
    jwtProperty.setExpired(1000);
    jwtProperty.setSecret("secret");
    jwtProperty.setIssuer("test");
    jwtProperty.setSecret(Randoms.randomAlphabet(10));
    jwtProperty.setSensitiveSecret(Randoms.randomAlphabet(10));
    String identifier = Randoms.randomAlphabetAndNum(6);
    JwtProvider jwtProvider = new JwtProviderImpl(jwtProperty);
    Token token = jwtProvider.generateToken(identifier);
    System.out.println(token);
    jwtProperty.setIssuer("haha");
    jwtProvider = new JwtProviderImpl(jwtProperty);
    try {
      jwtProvider.decodeToken(token.getToken());
    } catch (Exception e) {
      SystemException se = (SystemException) e;
      Assert.assertEquals(DefaultErrorCode.INVALID_TOKEN, se.getErrorCode());
      throw e;
    }
    Assert.fail();
  }
}
