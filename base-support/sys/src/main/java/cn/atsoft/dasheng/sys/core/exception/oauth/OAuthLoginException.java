package cn.atsoft.dasheng.sys.core.exception.oauth;

import cn.atsoft.dasheng.portal.banner.model.exception.AbstractBaseExceptionEnum;
import cn.atsoft.dasheng.portal.banner.model.exception.ServiceException;

/**
 * 第三方登录异常
 *
 * @author fengshuonan
 * @Date 2019/6/9 18:43
 */
public class OAuthLoginException extends ServiceException {

    public OAuthLoginException(AbstractBaseExceptionEnum exception) {
        super(exception);
    }

}
