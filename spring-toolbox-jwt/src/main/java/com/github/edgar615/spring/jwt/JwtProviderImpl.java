package com.github.edgar615.spring.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.github.edgar615.util.exception.DefaultErrorCode;
import com.github.edgar615.util.exception.SystemException;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/18.
 */
public class JwtProviderImpl implements JwtProvider {

  private final JwtProperty defaultJwtProperty;

  public JwtProviderImpl(JwtProperty jwtProperty) {
    this.defaultJwtProperty = jwtProperty;
  }

  @Override
  public Token generateToken(String identifier) {
    return generateToken(identifier, defaultJwtProperty);
  }

  @Override
  public String decodeToken(String token) {
    return decodeToken(token, defaultJwtProperty);
  }

  @Override
  public Token generateToken(String identifier, JwtProperty jwtProperty) {
    JWTCreator.Builder builder = JWT.create();
    if (jwtProperty.getIssuer() != null) {
      builder.withIssuer(jwtProperty.getIssuer());
    }
    if (jwtProperty.getSubject() != null) {
      builder.withSubject(jwtProperty.getSubject());
    }
    if (jwtProperty.getExpired() > 0) {
      builder.withExpiresAt(new Date(System.currentTimeMillis() + jwtProperty.getExpired()));
    }
    Algorithm algorithm = null;
    try {
      String encodeIdentifier = AESUtils.encrypt(identifier, jwtProperty.getSensitiveSecret());
      builder.withClaim("identifier", encodeIdentifier);
      algorithm = Algorithm.HMAC256(jwtProperty.getSecret());
    } catch (Exception e) {
      throw throwSystemException(e);
    }
    String jwt = builder
        .sign(algorithm);
    Token token = new Token();
    token.setExpire(jwtProperty.getExpired());
    token.setToken(jwt);
    return token;
  }

  @Override
  public String decodeToken(String token, JwtProperty jwtProperty) {
    DecodedJWT jwt;
    Verification verification = null;
    try {
      verification = JWT.require(Algorithm.HMAC256(jwtProperty.getSecret()));
      JWTVerifier verifier = verification.build(); //Reusable verifier instance
      if (jwtProperty.getIssuer() != null) {
        verification.withIssuer(jwtProperty.getIssuer());
      }
      if (jwtProperty.getSubject() != null) {
        verification.withSubject(jwtProperty.getSubject());
      }
      jwt = verifier.verify(token);
    } catch (Exception e) {
      throw throwSystemException(e);
    }
    if (jwtProperty.getIssuer() != null) {
      verification.withIssuer(jwtProperty.getIssuer());
    }
    if (!jwt.getClaims().containsKey("identifier")) {
      throw SystemException.create(DefaultErrorCode.INVALID_TOKEN);
    }
    String encodeIdentifier = jwt.getClaim("identifier").asString();

    try {
      String identifier = AESUtils.decrypt(encodeIdentifier, jwtProperty.getSensitiveSecret());
      return identifier;
    } catch (Exception e) {
      throw SystemException.create(DefaultErrorCode.INVALID_TOKEN);
    }
  }

  private SystemException throwSystemException(Exception e) {
    if (e instanceof TokenExpiredException) {
      return SystemException.create(DefaultErrorCode.EXPIRE_TOKEN);
    }
    if (e instanceof SignatureVerificationException) {
      return SystemException.create(DefaultErrorCode.INVALID_TOKEN)
          .set("details", e.getMessage());
    }
    if (e instanceof InvalidClaimException) {
      return SystemException.create(DefaultErrorCode.INVALID_TOKEN)
          .set("details", e.getMessage());
    }
    return SystemException.wrap(DefaultErrorCode.UNKOWN, e);
  }


}
