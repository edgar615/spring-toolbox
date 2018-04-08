package com.github.edgar615.util.spring.jwt;

/**
 * Created by Administrator on 2017/8/18.
 */
public interface JwtProvider {

  String generateToken(Principal principal);

  Principal decodeUser(String token);

}
