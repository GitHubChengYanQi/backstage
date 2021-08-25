package cn.atsoft.dasheng.UserInfo.service.impl;

import cn.atsoft.dasheng.UserInfo.model.GetUser;
import cn.atsoft.dasheng.UserInfo.service.UserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class UserinfoServiceImp implements UserInfoService {

    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private UserService userService;

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
//临时信息
//        redisTemplate.expire(randStr, 360000, TimeUnit.MINUTES);

        WxMaCodeLineColor wxMaCodeLineColor = new WxMaCodeLineColor("0", "0", "0");
        String scene = "?key=" + randStr;
        try {
            if (user.getUserId() != null && user.getPage() != null) {
                File wxaCode = wxMaQrcodeService.createWxaCode(path);
                FileInputStream fis = new FileInputStream(wxaCode);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                fis.close();
                byte[] byteArray = bos.toByteArray();
                return byteArray;
//                return wxMaQrcodeService.createWxaCodeUnlimitBytes(scene, user.getPage(), 430, true, wxMaCodeLineColor, true);


            } else {
                throw new ServiceException(500, "请确定登录");
            }

        } catch (WxErrorException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserResult redis(String randStr) {
        String wx = String.valueOf(redisTemplate.boundValueOps(randStr).get());

        String userId = wx.substring(8, wx.length());
        Long ids = Long.valueOf(userId);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("user_id", ids);
        List<User> users = userService.list(userQueryWrapper);
        for (User user : users) {
            UserResult userResult = new UserResult();
            ToolUtil.copyProperties(user, userResult);
            return userResult;
        }

        System.err.println(userId);

        return null;
    }

}
