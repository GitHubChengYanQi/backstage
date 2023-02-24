package cn.atsoft.dasheng.uc.jwt;

import cn.atsoft.dasheng.base.auth.jwt.payload.JwtPayLoad;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UcJwtPayLoad extends JwtPayLoad {

    private String type;

    private String mobile;

    private Long memberId;

    public UcJwtPayLoad() {
    }

    public UcJwtPayLoad(String type, Long userId, String account, String userKey) {
        super(userId, account, userKey);
        this.type = type;
    }

    /**
     * payload转化为map形式
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("type", this.type);
        map.put("mobile", this.mobile);
        map.put("memberId",this.memberId);
        return map;
    }

    /**
     * payload转化为map形式
     */
    public static UcJwtPayLoad toBean(Map<String, Object> map) {
        if (map == null || map.size() == 0) {
            return new UcJwtPayLoad();
        } else {
            UcJwtPayLoad ucJwtPayLoad = new UcJwtPayLoad();

            Object userId = map.get("userId");
            if (userId instanceof Long) {
                ucJwtPayLoad.setUserId(Long.valueOf(map.get("userId").toString()));
            }

            ucJwtPayLoad.setType((String) map.get("type"));
            ucJwtPayLoad.setAccount((String) map.get("account"));
            ucJwtPayLoad.setMobile((String) map.get("mobile"));
            ucJwtPayLoad.setUserKey((String) map.get("userKey"));
            ucJwtPayLoad.setMemberId((Long) map.get("memberId"));
            return ucJwtPayLoad;
        }
    }
}
