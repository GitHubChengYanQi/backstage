package cn.atsoft.dasheng.userInfo.service.impl;

import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import cn.atsoft.dasheng.uc.utils.UserUtils;
import cn.atsoft.dasheng.userInfo.model.BackUser;
import cn.atsoft.dasheng.userInfo.model.GetKey;
import cn.atsoft.dasheng.userInfo.model.GetUser;
import cn.atsoft.dasheng.userInfo.service.UserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.portal.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.portal.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.portal.wxUser.service.WxuserInfoService;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class UserinfoServiceImp implements UserInfoService {

    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private WxuserInfoService wxuserInfoService;
    @Autowired
    private UcOpenUserInfoService userInfoService;

    /**
     * 返回二维码
     *
     * @param user
     * @return
     */
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
        System.err.println(randStr);
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

    /**
     * 获取userId
     *
     * @param randStr
     * @return
     */
    @Override
    public BackUser backUser(String randStr) {

        if (UserUtils.getUserId() != null) {
            String wx = String.valueOf(redisTemplate.boundValueOps(randStr).get());
            String userId = wx.substring(8, wx.length());
            Long ids = Long.valueOf(userId);
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.in("user_id", ids);
            List<User> users = userService.list(userQueryWrapper);
            BackUser backUser = new BackUser();

            for (User user : users) {
                UserResult userResult = new UserResult();
                ToolUtil.copyProperties(user, userResult);
                backUser.setName(userResult.getName());
                backUser.setRandStr(randStr);
            }


            QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
            wxuserInfoQueryWrapper.in("user_id", ids);
            List<WxuserInfo> infoList = wxuserInfoService.list(wxuserInfoQueryWrapper);
            List<Long> memberIds = new ArrayList<>();
            for (WxuserInfo wxuserInfo : infoList) {
                memberIds.add(wxuserInfo.getMemberId());
            }


            System.err.println(userId);
            return backUser;
        }

        return null;

    }

    /**
     * 绑定
     *
     * @param
     */
    @Override
    public void binding(GetKey getKey) {
        if (getKey.getRandStr() != null) {
            String randStr = getKey.getRandStr();
            String wx = String.valueOf(redisTemplate.boundValueOps(randStr).get());
            String userId = wx.substring(8, wx.length());
//        String[] split = wx.split("bind-wx-");
            Long ids = Long.valueOf(userId);
//        Long getMemberId = Long.valueOf(getKey.getGetUserId());
            QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
            wxuserInfoQueryWrapper.in("user_id", ids);
            List<WxuserInfo> list = wxuserInfoService.list(wxuserInfoQueryWrapper);
            if (list.size() <= 0) {
                if (ids != null && UserUtils.getUserId() != null) {
//                Long memberId = UserUtils.getUserId();
                    WxuserInfoParam wxuserInfoParam = new WxuserInfoParam();
                    wxuserInfoParam.setUserId(ids);
                    wxuserInfoParam.setMemberId(UserUtils.getUserId());
                    wxuserInfoParam.setUuid(UserUtils.getUserAccount());
                    wxuserInfoService.add(wxuserInfoParam);
                }
            } else {
                throw new ServiceException(505, "账户已经绑定");
            }
        } else {
            throw new ServiceException(505, "绑定失败,请确认用户存在");
        }


    }

}
