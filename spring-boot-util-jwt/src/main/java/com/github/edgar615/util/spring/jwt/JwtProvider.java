package com.github.edgar615.util.spring.jwt;

/**
 * Created by Administrator on 2017/8/18.
 */
public interface JwtProvider {

  /**
   * 使用身份生成token，身份信息不应该存储敏感信息，这个方法会将身份信息做AES加密
   * @param identifier 身份信息
   * @return token
   */
  String generateToken(String identifier);

  /**
   * 将token解析成身份信息，这个方法会做AES解密得到身份信息
   * @param token 将token解析成身份信息
   * @return 用户信息
   */
  String decodeToken(String token);

}
