package cn.atsoft.dasheng.appBase.service;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;

import java.util.ArrayList;
import java.util.List;

public interface sendTemplateInterface {


    String getTemplateId();

    List<String> getOpenIds();

    List<WxMpTemplateData> getTemplateData();

    String getPage();

    void send() throws WxErrorException;


    //企业微信推送userIds
    List<String> userIds();

    void wxCpSend() throws WxErrorException;
}
