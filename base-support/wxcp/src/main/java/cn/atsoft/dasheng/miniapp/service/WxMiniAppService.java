package cn.atsoft.dasheng.miniapp.service;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.util.HttpContext;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.miniapp.entity.WxMaConfig;
import cn.atsoft.dasheng.miniapp.model.propertis.WxMaProperties;
import cn.atsoft.dasheng.miniapp.model.propertis.WxMaProperties2;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.aliyuncs.http.HttpRequest;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@EnableConfigurationProperties({WxMaProperties.class, WxMaProperties2.class})
public class WxMiniAppService {
    @Autowired
    private WxMaProperties config;
    private WxMaProperties2 config2;

    @Autowired
    private WxMaConfigService wxMaConfigService;

    /**
     * 获取微信小程序客户端
     *
     * @return
     */
    public WxMaServiceImpl getWxCpClient() {
        HttpServletRequest request = HttpContext.getRequest();
        //从request中获取appId参数
        assert request != null;
        String appId = request.getParameter("appid");
        return this.getWxCpClient(appId);
        //根据租户ID获取微信小程序配置
//        return getWxCpClient(LoginContextHolder.getContext().getTenantId());
//        return getWxCpClient(LoginContextHolder.getContext().getTenantId());
    }

    /**
     * 获取微信小程序客户端
     *
     * @param tenantId
     * @return
     */
    public WxMaServiceImpl getWxCpClient(Long tenantId) {

        //根据租户ID获取微信小程序配置
        WxMaConfig dbConfig = wxMaConfigService.getById(tenantId);
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        if (ToolUtil.isEmpty(dbConfig)) {
            // 未配置微信小程序 走默认application.yml配置
            config.setAppid(this.config.getMiniapp().getAppid());
            config.setSecret(this.config.getMiniapp().getSecret());
            config.setToken(this.config.getMiniapp().getToken());
            config.setMsgDataFormat(this.config.getMiniapp().getToken());
            config.setAesKey(this.config.getMiniapp().getAesKey());
        } else {
            //初始化微信小程序配置
            config.setAppid(dbConfig.getAppid());
            config.setSecret(dbConfig.getSecret());
            config.setToken(dbConfig.getToken());
            config.setMsgDataFormat(dbConfig.getToken());
            config.setAesKey(dbConfig.getAesKey());
        }
        WxMaServiceImpl wxCpService = new WxMaServiceImpl();
        wxCpService.setWxMaConfig(config);
        return wxCpService;

    }

    /**
     * 获取微信小程序客户端
     *
     * @param appId
     * @return
     */
    public WxMaServiceImpl getWxCpClient(String appId) {

        //根据租户ID获取微信小程序配置
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        if (ToolUtil.isEmpty(appId)) {
            // 未配置微信小程序 走默认application.yml配置
            config.setAppid(this.config.getMiniapp().getAppid());
            config.setSecret(this.config.getMiniapp().getSecret());
            config.setToken(this.config.getMiniapp().getToken());
            config.setMsgDataFormat(this.config.getMiniapp().getToken());
            config.setAesKey(this.config.getMiniapp().getAesKey());
        } else {
            WxMaConfig dbConfig = wxMaConfigService.lambdaQuery().eq(WxMaConfig::getAppid,appId).orderByDesc(WxMaConfig::getCreateTime).last("limit 1").one();
            if (ToolUtil.isEmpty(dbConfig)) {
                throw new ServiceException(500,"未配置微信小程序");
            }
            //初始化微信小程序配置
            config.setAppid(dbConfig.getAppid());
            config.setSecret(dbConfig.getSecret());
            config.setToken(dbConfig.getToken());
            config.setMsgDataFormat(dbConfig.getToken());
            config.setAesKey(dbConfig.getAesKey());
        }
        WxMaServiceImpl wxCpService = new WxMaServiceImpl();
        wxCpService.setWxMaConfig(config);
        return wxCpService;

    }
}
