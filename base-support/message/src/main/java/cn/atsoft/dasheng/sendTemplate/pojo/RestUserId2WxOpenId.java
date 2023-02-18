package cn.atsoft.dasheng.sendTemplate.pojo;

import lombok.Data;

@Data
public class RestUserId2WxOpenId {
    private Long userId;
    private Long memberId;
    private String openId;
}
