package cn.atsoft.dasheng.uc.jwt;

import cn.atsoft.dasheng.base.auth.jwt.JwtTokenUtil;
import io.jsonwebtoken.Claims;

public class UcJwtTokenUtil extends JwtTokenUtil {

    public static UcJwtPayLoad getJwtPayLoad(String token) {
        Claims claimFromToken = getClaimFromToken(token);
        return UcJwtPayLoad.toBean(claimFromToken);
    }
}
