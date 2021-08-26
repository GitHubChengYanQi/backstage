package cn.atsoft.dasheng.UserInfo.service;

import cn.atsoft.dasheng.UserInfo.model.BackUser;
import cn.atsoft.dasheng.UserInfo.model.GetKey;
import cn.atsoft.dasheng.UserInfo.model.GetUser;


public interface UserInfoService {

    byte[] getuser(GetUser user);

    BackUser backUser(String randStr);

    void binding(GetKey getKey);
}
