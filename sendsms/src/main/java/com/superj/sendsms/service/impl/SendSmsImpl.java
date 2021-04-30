package com.superj.sendsms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.superj.sendsms.service.ISendSms;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SendSmsImpl implements ISendSms {

    @Value("${superj.sms.signName}")
    private String signName;//签名
    @Value("${superj.sms.templateCode}")
    private String templateCode;//模版code
    @Value("${superj.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${superj.sms.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId accessKeyId
     * @param accessKeySecret accessKeySecret
     * @return Client Client
     * @throws Exception Exception
     */
    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名（默认的，不要动）
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

    /**
     * 发送验证码
     * @param phoneNum 手机号
     * @param code 验证码
     * @return boolean 是否发送成功
     * @throws Exception 异常
     */
    @Override
    public boolean send(String phoneNum, Map<String, Object> code) throws Exception {
        // 连接阿里云
        Client client = SendSmsImpl.createClient(accessKeyId, accessKeySecret);
        // 自定义参数
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phoneNum) //手机号
                .setSignName(signName) //签名
                .setTemplateCode(templateCode) //模板code
                .setTemplateParam(JSONObject.toJSONString(code)); //验证码
        // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse response = client.sendSms(sendSmsRequest);
        if (response.body.code.equals("OK")) { //如果返回OK，发送成功，返回true
            return true;
        }
        //错误码&错误信息
        System.out.println(response.body.code);
        System.out.println(response.body.message);
        return false;
    }
    /**测试用*/
    /*public static void main(String[] args_) throws Exception {
        Client client = SendSmsImpl.createClient("accessKeyId", "accessKeySecret");
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers("手机号")
                .setSignName("签名")
                .setTemplateCode("模版code")
                .setTemplateParam("{code:'2233'}");//验证码json字符串
        // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
        System.out.println(sendSmsResponse.body.code);
        System.out.println(sendSmsResponse.body.message);
    }*/
}
