package cn.atsoft.dasheng.UserInfo.service.impl;

import cn.atsoft.dasheng.UserInfo.model.GetUser;
import cn.atsoft.dasheng.UserInfo.service.UserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.uc.model.params.MiniAppUserProfileParam;
import cn.atsoft.dasheng.uc.service.UcMemberAuth;
import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class UserinfoServiceImp implements UserInfoService {

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public File getuser(GetUser user) {

        Long userId = user.getUserId();
        String userString = "bind-wx-"+userId;


        String randStr = ToolUtil.getRandomString(16);

        String path = user.getPage() + "?key=" +randStr;

        WxMaQrcodeService wxMaQrcodeService = wxMaService.getQrcodeService();


                redisTemplate.boundValueOps(userString).set(randStr);

        try {

            return  wxMaQrcodeService.createQrcode(path);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
       return null;
    }

}
