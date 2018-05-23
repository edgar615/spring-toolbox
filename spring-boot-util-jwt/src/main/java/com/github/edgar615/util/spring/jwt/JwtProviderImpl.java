package com.github.edgar615.util.spring.jwt;

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

  private final JwtProperty jwtProperty;

  public JwtProviderImpl(JwtProperty jwtProperty) {this.jwtProperty = jwtProperty;}

  @Override
  public String generateToken(Principal principal) {
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
    builder.withClaim("userId", principal.getUserId());
    if (principal.getCompanyCode() != null) {
      builder.withClaim("companyCode", principal.getCompanyCode());
    }
    if (principal.getUsername() != null) {
      builder.withClaim("username", principal.getUsername());
    }
    if (principal.getFullname() != null) {
      builder.withClaim("fullname", principal.getFullname());
    }
    if (principal.getMail() != null) {
      builder.withClaim("mail", principal.getMail());
    }
    if (principal.getTel() != null) {
      builder.withClaim("tel", principal.getTel());
    }
    if (principal.getJti() != null) {
      builder.withJWTId(principal.getJti());
    }
    Algorithm algorithm = null;
    try {
      algorithm = Algorithm.HMAC256(jwtProperty.getSecret());
    } catch (Exception e) {
      throw throwSystemException(e);
    }
    String token = builder
            .sign(algorithm);
    return token;
  }

  @Override
  public Principal decodeUser(String token) {

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
    PrincipalImpl loginUser = new PrincipalImpl();
    Long userId = jwt.getClaims().get("userId").asLong();
    loginUser.setUserId(userId);
    if (jwt.getClaims().containsKey("companyCode")) {
      String companyCode = jwt.getClaims().get("companyCode").asString();
      loginUser.setCompanyCode(companyCode);
    }
    if (jwt.getClaims().containsKey("username")) {
      String username = jwt.getClaims().get("username").asString();
      loginUser.setUsername(username);
    }
    if (jwt.getClaims().containsKey("fullname")) {
      String fullname = jwt.getClaims().get("fullname").asString();
      loginUser.setFullname(fullname);
    }
    if (jwt.getClaims().containsKey("tel")) {
      String tel = jwt.getClaims().get("tel").asString();
      loginUser.setTel(tel);
    }
    if (jwt.getClaims().containsKey("mail")) {
      String mail = jwt.getClaims().get("mail").asString();
      loginUser.setMail(mail);
    }
    if (jwt.getClaims().containsKey("jti")) {
      String jti = jwt.getClaims().get("jti").asString();
      loginUser.setJti(jti);
    }
    return loginUser;
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
