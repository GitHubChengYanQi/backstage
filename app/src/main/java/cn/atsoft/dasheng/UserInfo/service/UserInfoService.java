package cn.atsoft.dasheng.UserInfo.service;

import cn.atsoft.dasheng.UserInfo.model.GetUser;


public interface UserInfoService {

    byte[] getuser(GetUser user);

    void redis (String randStr);
}
