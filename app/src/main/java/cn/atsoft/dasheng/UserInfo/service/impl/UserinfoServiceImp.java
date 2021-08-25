package cn.atsoft.dasheng.UserInfo.service.impl;

import cn.atsoft.dasheng.UserInfo.model.GetUser;
import cn.atsoft.dasheng.UserInfo.service.UserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserinfoServiceImp implements UserInfoService {

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public byte[] getuser(GetUser user) {

        Long userId = user.getUserId();
        String userString = "bind-wx-" + userId;


        String randStr = ToolUtil.getRandomString(16);

        String path = user.getPage() + "?key=" + randStr;

        WxMaQrcodeService wxMaQrcodeService = wxMaService.getQrcodeService();


        redisTemplate.boundValueOps(randStr).set(userString);

        redisTemplate.expire(randStr, 360000, TimeUnit.MINUTES);

        WxMaCodeLineColor wxMaCodeLineColor = new WxMaCodeLineColor("0", "0", "0");
        String scene = "欢迎登录";
        try {
            if (user.getUserId() != null && user.getPage() != null) {
                return wxMaQrcodeService.createWxaCodeUnlimitBytes(scene, user.getPage(), 430, true, wxMaCodeLineColor, true);
            }

        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void redis() {

    }

}
