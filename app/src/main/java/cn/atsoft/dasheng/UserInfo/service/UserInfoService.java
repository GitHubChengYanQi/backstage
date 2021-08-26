package cn.atsoft.dasheng.UserInfo.service;

import cn.atsoft.dasheng.UserInfo.model.GetUser;


public interface UserInfoService {

    byte[] getuser(GetUser user);

    String backUser (String randStr);

    void binding (Long userid);
}
