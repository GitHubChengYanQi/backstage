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
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.model.params.WxuserInfoParam;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserinfoServiceImp implements UserInfoService {


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
    public String getuser(GetUser user) {
        /**
         * 获取userid 存入readis 生成随机数作为key
         */
        Long userId = user.getUserId();
        String userString = "bind-wx-" + userId;
        String randStr = ToolUtil.getRandomString(16);


//        String path = user.getPage() + "?key=" + randStr;
//        WxMaQrcodeService wxMaQrcodeService = wxMaService.getQrcodeService();
        redisTemplate.boundValueOps(randStr).set(userString);
        return randStr;
//临时信息
//        redisTemplate.expire(randStr, 360000, TimeUnit.MINUTES);
//
//        WxMaCodeLineColor wxMaCodeLineColor = new WxMaCodeLineColor("0", "0", "0");
//        System.err.println(randStr);
//        String scene = "?key=" + randStr;
//        /**
//         * file改成byte【】
//         */
//        try {
//            if (user.getUserId() != null) {
//                File wxaCode = wxMaQrcodeService.createWxaCode(path);
//                FileInputStream fis = new FileInputStream(wxaCode);
//                ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
//                byte[] b = new byte[1024];
//                int n;
//                while ((n = fis.read(b)) != -1) {
//                    bos.write(b, 0, n);
//                }
//                fis.close();
//                byte[] byteArray = bos.toByteArray();
//                return byteArray;
//            } else {
//                throw new ServiceException(500, "请确定登录");
//            }
//
//        } catch (WxErrorException | FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
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
            BackUser backUser = new BackUser();
            QueryWrapper<UcOpenUserInfo> ucOpenUserInfoQueryWrapper = new QueryWrapper<>();
            ucOpenUserInfoQueryWrapper.in("member_id", UserUtils.getUserId());
            List<UcOpenUserInfo> ucOpenUserInfos = userInfoService.list(ucOpenUserInfoQueryWrapper);
            for (UcOpenUserInfo ucOpenUserInfo : ucOpenUserInfos) {
                backUser.setUcName(ucOpenUserInfo.getUsername());
                backUser.setUcAvatar(ucOpenUserInfo.getAvatar());
            }
            //通过key获取userid
            String wx = String.valueOf(redisTemplate.boundValueOps(randStr).get());
            String userId = wx.substring(8, wx.length());
            Long ids = Long.valueOf(userId);
            //查询userid信息
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
//            userQueryWrapper.in("user_id", ids);
            userQueryWrapper.eq("user_id", ids);
            List<User> users = userService.list(userQueryWrapper);


            //获取user信息返回数据
            for (User user : users) {

                UserResult userResult = new UserResult();
                ToolUtil.copyProperties(user, userResult);
                backUser.setName(userResult.getName());
                backUser.setRandStr(randStr);
            }
            //查询当前userid是否绑定 绑定返回false
            QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
            wxuserInfoQueryWrapper.eq("user_id", ids).or().eq("member_id", UserUtils.getUserId());
            List<WxuserInfo> infoList = wxuserInfoService.list(wxuserInfoQueryWrapper);
//            WxuserInfo one = wxuserInfoService.lambdaQuery().eq(WxuserInfo::getUserId, ids).and(i -> i.eq(WxuserInfo::getMemberId, UserUtils.getUserId())).one();
            if (infoList.size() > 0) {
                backUser.setBln(false);
            }
//            if (one!=null) {
//                backUser.setBln(false);
//            }
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
            /**
             * 从getKey取出key
             */
            String randStr = getKey.getRandStr();
            /**
             * 通过key从redis取出userid
             */
            String wx = String.valueOf(redisTemplate.boundValueOps(randStr).get());
            /**
             * 截取userid
             */
            String userId = wx.substring(8, wx.length());
            Long ids = Long.valueOf(userId);
            /**
             * 通过userid查询是否绑定
             */
            QueryWrapper<WxuserInfo> wxuserInfoQueryWrapper = new QueryWrapper<>();
            wxuserInfoQueryWrapper.in("user_id", ids);
            List<WxuserInfo> list = wxuserInfoService.list(wxuserInfoQueryWrapper);
            for (WxuserInfo wxuserInfo : list) {
                if (wxuserInfo.getMemberId().equals(UserUtils.getUserId())) {
                    throw new ServiceException(500, "该用户已被绑定");
                }
            }
            /**
             * 绑定
             */
            if (list.size() <= 0) {
                if (ids != null && UserUtils.getUserId() != null) {
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
            throw new ServiceException(500, "绑定失败,请确认用户存在");
        }


    }

}
