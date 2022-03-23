package com.app.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtil {
    private final static String PRIVATE_KEY="dcf3fgdsayhjop@";

    /**
     * 创建token
     * @param map
     * @return
     */
    public static String getToken(Map<String,String> map){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,30);
        JWTCreator.Builder builder = JWT.create();
        //添加载荷信息
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        String token = builder.withExpiresAt(calendar.getTime()).//设置token过期时间
                sign(initSign());//设置密钥
        return token;
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public static DecodedJWT parseToken(String token){
        DecodedJWT verify = JWT.require(initSign()).build().verify(token);

        return verify;
    }

    /**
     * 初始化密钥
     * @return
     */
    private static Algorithm initSign(){
        return Algorithm.HMAC256(PRIVATE_KEY);
    }

}
