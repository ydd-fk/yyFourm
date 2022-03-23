package com.app.interceptor;

import com.app.annotation.TokenPromise;
import com.app.util.JWTUtil;
import com.app.util.ResponseCode;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("authorization");
        Boolean flag = false;
        try {
            DecodedJWT decodedJWT = JWTUtil.parseToken(token);
            Map<String, Claim> claims = decodedJWT.getClaims();
            Integer userID = Integer.parseInt(claims.get("id").asString());
            request.setAttribute("userID",userID);
            flag = true;
        }catch (TokenExpiredException e){
            request.setAttribute("code", ResponseCode.TOKEN_EXPIRE_CODE);
            request.setAttribute("message","登录过期");
        }catch (Exception e){
            request.setAttribute("code",ResponseCode.TOKEN_NONE_CODE);
            request.setAttribute("message","请先登录");
        }
        //判断是否需要登录，如果不需要则直接放行
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            if (handlerMethod.getMethod().getAnnotation(TokenPromise.class)==null) flag=true;
        }else {
            flag=true;
        }
        if(flag==false) {
            request.getRequestDispatcher("/error/verify").forward(request,response);
        }
        return flag;
    }

}


