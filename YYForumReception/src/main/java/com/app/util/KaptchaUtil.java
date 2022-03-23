package com.app.util;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class KaptchaUtil {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private DefaultKaptcha defaultKaptcha;

    /**
     * 检查验证码是否正确
     * @param codeKey
     * @param code
     * @return
     */
    public boolean checkVerifyCode(String codeKey,String code){
        if (codeKey==null||"".equals(codeKey)) return false;
        if(code==null||("").equals(code)) {
            getRedisTemplate().delete(codeKey);
            return false;
        }
        ValueOperations ops = getRedisTemplate().opsForValue();
        String redisCode = (String) ops.get(codeKey);
        if(code.equals(redisCode)){
            getRedisTemplate().delete(codeKey);
            return true;
        }
        else {
            getRedisTemplate().delete(codeKey);
            return false;
        }
    }

    /**
     * 缓存验证码
     * @param codeKey
     * @param code
     */
    public void cacheVerifyCode(String codeKey,String code){
        ValueOperations ops = getRedisTemplate().opsForValue();
        ops.set(codeKey,code);
        getRedisTemplate().expire(codeKey,1, TimeUnit.MINUTES);
    }


    /**
     * 获取验证码
     * @return
     * @throws IOException
     */
    public Map<String, String> getVerifyCode() throws IOException {
        HashMap<String, String> map = new HashMap<>();
        String code = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(code);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"jpg", byteArrayOutputStream);
        byte[] byteArray = Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
        String imgSourceStr = "data:image/jpeg;base64,"+new String(byteArray);
        String uuID = UUID.randomUUID().toString().replaceAll("-", "");
        map.put("imgSource",imgSourceStr);
        map.put("codeKey",uuID);
        cacheVerifyCode(uuID,code);
        return map;
    }

    /**
     * 配置redisTemplate
     * @return
     */
    private RedisTemplate getRedisTemplate(){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
