package cn.atsoft.dasheng.audit.handler;

import cn.atsoft.dasheng.audit.entity.WxAudit;
import cn.atsoft.dasheng.audit.service.WxAuditService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.orderPaymentApply.entity.CrmOrderPaymentApply;
import cn.atsoft.dasheng.orderPaymentApply.service.CrmOrderPaymentApplyService;
import com.alibaba.fastjson.JSON;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutTextMessage;
import me.chanjar.weixin.cp.bean.outxmlbuilder.TextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class MsgHandler extends AbstractHandler{
    @Autowired
    private WxAuditService  wxAuditService;
    @Autowired
    private CrmOrderPaymentApplyService paymentApplyService;


    @Override
    public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage, Map<String, Object> context, WxCpService wxCpService, WxSessionManager sessionManager) throws WxErrorException {
        final String msgType = wxMessage.getMsgType();
        if (msgType == null) {
            // 如果msgType没有，就自己根据具体报文内容做处理
            return null;
        }

        if (!msgType.equals(WxConsts.XmlMsgType.EVENT)) {
            //TODO 可以选择将消息保存到本地


        }
        String spNo = wxMessage.getApprovalInfo().getSpNo();
        WxAudit wxAudit = wxAuditService.getById(spNo);
        CrmOrderPaymentApply paymentApply = paymentApplyService.getById(spNo);
        if (ToolUtil.isNotEmpty(wxAudit) && !wxMessage.getApprovalInfo().getSpStatus().equals(1)) {
            wxAudit.setStatus(wxMessage.getApprovalInfo().getSpStatus());
            wxAudit.setDoneTime(new Date());
            wxAuditService.updateById(wxAudit);
        }
        if (ToolUtil.isNotEmpty(paymentApply) && !wxMessage.getApprovalInfo().getSpStatus().equals(1)) {
            paymentApply.setStatus(wxMessage.getApprovalInfo().getSpStatus());
            paymentApply.setDoneTime(new Date());
            paymentApplyService.updateById(paymentApply);
        }
        //TODO 组装回复消息
        String content = "收到信息内容：" + JSON.toJSONString(wxMessage);



        return WxCpXmlOutMessage.TEXT().content(content)
                .fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName())
                .build();
    }
}
