package cn.atsoft.dasheng.uc.utils;

import cn.atsoft.dasheng.uc.jwt.UcJwtPayLoad;
import cn.atsoft.dasheng.uc.jwt.UcJwtTokenUtil;
import cn.atsoft.dasheng.base.auth.exception.AuthException;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.sys.core.auth.util.TokenUtil;

public class UserUtils {



    public static UcJwtPayLoad getPayLoad(){
        String token = TokenUtil.getToken();
        try{
            return UcJwtTokenUtil.getJwtPayLoad(token);
        }catch (Exception e){
            return new UcJwtPayLoad();
        }

    }

    public static String getUserAccount(){
        UcJwtPayLoad ucJwtPayLoad = getPayLoad();
        String account =  ucJwtPayLoad.getAccount();
        if(ToolUtil.isEmpty(account)){
            throw new AuthException();
        }
        return account;
    }

    public static Long getUserId(){
        UcJwtPayLoad ucJwtPayLoad = getPayLoad();
        Long userId =  ucJwtPayLoad.getUserId();
        if(ToolUtil.isEmpty(userId)){
            throw new AuthException(402);
        }
        return userId;
    }

    public static String getType(){
        UcJwtPayLoad ucJwtPayLoad = getPayLoad();
        String type =  ucJwtPayLoad.getType();
        if(ToolUtil.isEmpty(type)){
            return "";
        }
        return type;
    }

    public static String getMobile(){
        UcJwtPayLoad ucJwtPayLoad = getPayLoad();
        String mobile =  ucJwtPayLoad.getMobile();
        if(ToolUtil.isEmpty(mobile)){
            return "";
        }
        return mobile;
    }
}
