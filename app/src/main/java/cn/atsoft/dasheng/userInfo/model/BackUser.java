package cn.atsoft.dasheng.userInfo.model;

import lombok.Data;

@Data
public class BackUser {
    private String name;
    private Boolean bln = true;
    private String randStr;
    private  String ucName;
    private  String ucAvatar;
}
