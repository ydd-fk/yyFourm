package com.app.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ResponseBody
@RequestMapping("/error")
@Controller
public class JWTErrorController {

    @RequestMapping("/verify")
    public Object jwtError(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message",request.getAttribute("message"));
        jsonObject.put("code",request.getAttribute("code"));
        return jsonObject;
    }
}
