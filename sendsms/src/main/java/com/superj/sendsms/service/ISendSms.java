package com.superj.sendsms.service;

import java.util.Map;

public interface ISendSms {
    boolean send(String phoneNum, Map<String, Object> code) throws Exception;
}
