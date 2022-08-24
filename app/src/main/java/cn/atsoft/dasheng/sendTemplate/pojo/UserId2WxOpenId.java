package cn.atsoft.dasheng.sendTemplate.pojo;

import lombok.Data;

@Data
public class UserId2WxOpenId {
    private Long userId;
    private Long memberId;
    private String openId;
}
