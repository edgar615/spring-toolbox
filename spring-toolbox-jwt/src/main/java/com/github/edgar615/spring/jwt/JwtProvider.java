package com.github.edgar615.spring.jwt;

/**
 * Created by Administrator on 2017/8/18.
 */
public interface JwtProvider {

  /**
   * 使用身份生成token，身份信息不应该存储敏感信息，这个方法会将身份信息做AES加密
   * @param identifier 身份信息
   * @return token
   */
  Token generateToken(String identifier);

  /**
   * 将token解析成身份信息，这个方法会做AES解密得到身份信息
   * @param token 将token解析成身份信息
   * @return 用户信息
   */
  String decodeToken(String token);

  /**
   * 使用身份生成token，身份信息不应该存储敏感信息，这个方法会将身份信息做AES加密
   * @param identifier 身份信息
   * @param jwtProperty JWT配置
   * @return token
   */
  Token generateToken(String identifier, JwtProperty jwtProperty);

  /**
   * 将token解析成身份信息，这个方法会做AES解密得到身份信息
   * @param token 将token解析成身份信息
   * @param jwtProperty JWT配置
   * @return 用户信息
   */
  String decodeToken(String token, JwtProperty jwtProperty);

}
