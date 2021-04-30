package com.superj.sendsms.controller;

import com.superj.sendsms.service.ISendSms;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 发送短信验证码controller
 * 接口地址：http://localhost:8080/sendSms/手机号
 */
@RestController
@CrossOrigin
public class SmsApiController {
    @Resource
    private ISendSms iSendSms;
    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/sendSms/{phoneNum}")
    public String code(@PathVariable("phoneNum") String phoneNum) throws Exception {
        //集成redis
        String code = String.valueOf(redisTemplate.opsForValue().get(phoneNum));
        if (StringUtils.hasLength(code) && !code.equals("null")) {
            return phoneNum + ":" + code + "已存在，还没过期。";
        }
        //生成随机验证码并存储到redis中
        code = UUID.randomUUID().toString().substring(0, 4);
        HashMap<String, Object> param = new HashMap<>();
        param.put("code", code);
        //调用发送短信服务
        boolean sendResult = iSendSms.send(phoneNum,param);
        if (sendResult) {
            //如果发送成功存入redis
            redisTemplate.opsForValue().set(phoneNum, code, 5, TimeUnit.MINUTES);//5分钟过期
            return phoneNum + ":" + code + "发送成功！";
        } else {
            return phoneNum + ":" + code + "发送失败！";
        }
    }
}
