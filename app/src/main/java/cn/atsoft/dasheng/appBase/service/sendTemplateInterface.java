package cn.atsoft.dasheng.appBase.service;

import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;

import java.util.ArrayList;
import java.util.List;

public interface sendTemplateInterface {



    String getTemplateId();

    List<String> getOpenIds();

    List<WxMpTemplateData> getTemplateData();

    String getPage();

    void send();
}
