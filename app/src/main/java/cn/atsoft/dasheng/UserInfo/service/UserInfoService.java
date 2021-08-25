package cn.atsoft.dasheng.UserInfo.service;

import cn.atsoft.dasheng.UserInfo.model.GetUser;
import org.springframework.stereotype.Service;

import java.io.File;


public interface UserInfoService {
    File getuser(GetUser user);
}
