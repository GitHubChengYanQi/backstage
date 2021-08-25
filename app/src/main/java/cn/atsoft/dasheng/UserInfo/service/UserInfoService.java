package cn.atsoft.dasheng.UserInfo.service;

import cn.atsoft.dasheng.UserInfo.model.GetUser;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;


public interface UserInfoService {

    byte[] getuser(GetUser user);

    UserResult redis (String randStr);
}
