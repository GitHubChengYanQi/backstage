package cn.atsoft.dasheng.userInfo.service;

import cn.atsoft.dasheng.userInfo.model.BackUser;
import cn.atsoft.dasheng.userInfo.model.GetBind;
import cn.atsoft.dasheng.userInfo.model.GetKey;
import cn.atsoft.dasheng.userInfo.model.GetUser;


public interface UserInfoService {

    String getuser(GetUser user);

    BackUser backUser(String randStr);

    void binding(GetKey getKey);

    void binds(GetBind getBind);
}
