package cn.atsoft.dasheng.erp.service.impl;

import cn.atsoft.dasheng.appBase.service.WxCpService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.binding.wxUser.entity.WxuserInfo;
import cn.atsoft.dasheng.binding.wxUser.service.WxuserInfoService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.uc.entity.UcOpenUserInfo;
import cn.atsoft.dasheng.uc.service.UcOpenUserInfoService;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class OrderUpload {

    @Autowired
    private WxCpService wxCpService;
    @Autowired
    private UcOpenUserInfoService openUserInfoService;
    @Autowired
    private WxuserInfoService wxuserInfoService;


    public void upload(File file) {


        try {

            WxMediaUploadResult  upload = wxCpService.getWxCpClient().getMediaService().upload("file", file);
            String mediaId = upload.getMediaId();

            Long userId = LoginContextHolder.getContext().getUserId();
            WxuserInfo wxuserInfo = wxuserInfoService.query().eq("user_id", userId).eq("source", "wxCp").one();

            if (ToolUtil.isNotEmpty(wxuserInfo)) {
                UcOpenUserInfo userInfo = openUserInfoService.query().eq("member_id", wxuserInfo.getMemberId()).one();
                if (ToolUtil.isNotEmpty(userInfo)) {
                    WxCpMessage wxCpMessage = new WxCpMessage();
                    wxCpMessage.setToUser(userInfo.getUuid());
                    wxCpMessage.setMsgType("file");
                    wxCpMessage.setMediaId(mediaId);
                    wxCpService.getWxCpClient().getMessageService().send(wxCpMessage);
                }
            }


        } catch (WxErrorException e) {
            e.printStackTrace();
        }


    }

}
