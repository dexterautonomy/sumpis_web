package com.teamwurkz.domain;

import infobip.api.client.SendSingleTextualSms;
import infobip.api.config.BasicAuthConfiguration;
import infobip.api.model.sms.mt.send.textual.SMSTextualRequest;
import java.util.Arrays;
public class SendOtpClass
{
    SendSingleTextualSms client;
    public SendOtpClass()
    {
        client=new SendSingleTextualSms(new BasicAuthConfiguration("EDCCorp2017", "ezeigbo3333"));//username and password to infobip
    }
    public void sendOtp(String TO, String OTP)
    {
        SMSTextualRequest requestBody = new SMSTextualRequest();        
        requestBody.setFrom("Sumpis");        
        requestBody.setTo(Arrays.asList(TO));        
        requestBody.setText(OTP);
        client.execute(requestBody);
    }
}