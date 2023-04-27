package cn.atsoft.dasheng.audit.service.impl;


import cn.atsoft.dasheng.audit.config.TemplateConfig;
import cn.atsoft.dasheng.audit.entity.WxAudit;
import cn.atsoft.dasheng.audit.mapper.WxAuditMapper;
import cn.atsoft.dasheng.audit.service.RestWxCpService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.audit.model.params.WxAuditParam;
import cn.atsoft.dasheng.audit.model.result.WxAuditResult;
import cn.atsoft.dasheng.audit.service.WxAuditService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.media.config.RestAliConfiguration;
import cn.atsoft.dasheng.media.config.RestAliyunService;
import cn.atsoft.dasheng.media.entity.Media;
import cn.atsoft.dasheng.media.service.RestMediaService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orderPaymentApply.entity.CrmOrderPaymentApply;
import cn.atsoft.dasheng.orderPaymentApply.service.CrmOrderPaymentApplyService;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.bean.oa.SummaryInfo;
import me.chanjar.weixin.cp.bean.oa.WxCpApprovalDetailResult;
import me.chanjar.weixin.cp.bean.oa.WxCpOaApplyEventRequest;
import me.chanjar.weixin.cp.bean.oa.WxCpTemplateResult;
import me.chanjar.weixin.cp.bean.oa.applydata.ApplyDataContent;
import me.chanjar.weixin.cp.bean.oa.applydata.ContentValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static me.chanjar.weixin.common.api.WxConsts.MediaFileType.FILE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-18
 */
@Service
public class WxAuditServiceImpl extends ServiceImpl<WxAuditMapper, WxAudit> implements WxAuditService {
    @Autowired
    private RestWxCpService wxCpService;
    @Autowired
    private RestMediaService mediaService;
    @Autowired
    private RestAliyunService aliyunService;
    @Autowired
    private CrmOrderPaymentApplyService orderPaymentApplyService;
    @Autowired
    private UserService userService;
    @Autowired
    private TemplateConfig templateConfig;
    @Override
    public void add(WxAuditParam param){

        WxAudit entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(WxAuditParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(WxAuditParam param){
        WxAudit oldEntity = getOldEntity(param);
        WxAudit newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public WxAuditResult findBySpec(WxAuditParam param){
        return null;
    }

    @Override
    public List<WxAuditResult> findListBySpec(WxAuditParam param){
        return null;
    }

    @Override
    public PageInfo<WxAuditResult> findPageBySpec(WxAuditParam param){
        Page<WxAuditResult> pageContext = getPageContext();
        IPage<WxAuditResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(WxAuditParam param){
        return param.getSpNo();
    }

    private Page<WxAuditResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private WxAudit getOldEntity(WxAuditParam param) {
        return this.getById(getKey(param));
    }

    private WxAudit getEntity(WxAuditParam param) {
        WxAudit entity = new WxAudit();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
    @Override
    @Transactional
    public String post(WxAuditParam param) throws WxErrorException, IOException {
        String userWechatOpenId = userService.getUserWechatOpenId(LoginContextHolder.getContext().getUserId());
        if(ToolUtil.isEmpty(userWechatOpenId)){
            throw new ServiceException(500,"请先在企业微信中打开平台进行绑定");
        }
        param.setCreatorUserId(userWechatOpenId);
        param.setTemplateId(templateConfig.getTemplateId());
        param.setUseTemplateApprover(1);

        WxCpOaApplyEventRequest wxCpOaApplyEventRequest = BeanUtil.copyProperties(param, WxCpOaApplyEventRequest.class);
//                WxCpOaApplyEventRequest

        if (ToolUtil.isEmpty(param.getApplyData()) || ToolUtil.isEmpty(param.getApplyData().getContents())){
            throw new ServiceException(500,"请传入审批申请数据");
        }
        List<Long> mediaIdList = new ArrayList<>();
        CrmOrderPaymentApply paymentApply = new CrmOrderPaymentApply();
        paymentApply.setOrderId(param.getOrderId());
        List<SummaryInfo> summaryList = new ArrayList<>();
        for (ApplyDataContent content : wxCpOaApplyEventRequest.getApplyData().getContents()) {
            String id = content.getId();
            ContentValue value = content.getValue();
            switch (id){
                case "item-1494251052639":
                    SummaryInfo moneySummaryInfo = new SummaryInfo();
                    List<SummaryInfo.SummaryInfoData> moneySummaryInfoData = new ArrayList<>();
                    SummaryInfo.SummaryInfoData moneyInfoData = new SummaryInfo.SummaryInfoData();
                    moneyInfoData.setText("付款金额："+"￥"+value.getNewMoney());
                    moneyInfoData.setLang("zh_CN");
                    moneySummaryInfoData.add(moneyInfoData);
                    moneySummaryInfo.setSummaryInfoData(moneySummaryInfoData);
                    summaryList.add(moneySummaryInfo);
//                    summaryList.add(new SummaryInfo(){{
//                        setSummaryInfoData(new ArrayList<SummaryInfoData>(){{
//                            add(new SummaryInfoData(){{
//                                setText("付款金额："+"￥"+value.getNewMoney());
//                                setLang("zh_CN");
//                            }});
//                        }});
//                    }});

                    long money = BigDecimal.valueOf(Double.parseDouble(value.getNewMoney())).multiply(new BigDecimal(100)).longValue();
                    //TODO 保存金额
                    System.out.println(value.getNewMoney());
                    Long unpaidMoney = this.baseMapper.unpaidMoney(param.getOrderId());
                    if (unpaidMoney - money<0) {
                        throw new ServiceException(500,"累计付款金额不得超过订单总金额");
                    }
                    paymentApply.setNewMoney(money);

                    break;
                case "item-1494251220825":
                    //附件
                    for (ContentValue.File file : value.getFiles()) {
                        file.getFileId();

                        Media media = mediaService.getById(Long.parseLong(file.getFileId()));
                        mediaIdList.add(media.getMediaId());
                        RestAliConfiguration.OSS oss = aliyunService.getConfig().getOss2();
                        OSSObject object = aliyunService.getPrivateOssClient().getObject(oss.getBucket(), media.getPath());

                        InputStream field = object.getObjectContent();
                        WxMediaUploadResult upload = wxCpService.getWxCpClient().getMediaService().upload(FILE, media.getType(), field);
                        file.setFileId(upload.getMediaId());
                    }
                    break;
                case "item-1494251039326":
                    SummaryInfo pay = new SummaryInfo();
                    List<SummaryInfo.SummaryInfoData> paySummaryInfoData = new ArrayList<>();
                    SummaryInfo.SummaryInfoData payInfoData = new SummaryInfo.SummaryInfoData();
                    payInfoData.setText("付款事由："+value.getText());
                    payInfoData.setLang("zh_CN");
                    paySummaryInfoData.add(payInfoData);
                    pay.setSummaryInfoData(paySummaryInfoData);
                    summaryList.add(pay);
//                    summaryList.add(new SummaryInfo(){{
//                        setSummaryInfoData(new ArrayList<SummaryInfoData>(){{
//                            add(new SummaryInfoData(){{
//                                setText("付款事由："+value.getText());
//                                setLang("zh_CN");
//                            }});
//                        }});
//                    }});
//                    break;
                case "item-1494251166594":
                    SummaryInfo summaryInfo = new SummaryInfo();
                    List<SummaryInfo.SummaryInfoData> summaryInfoData = new ArrayList<>();
                    SummaryInfo.SummaryInfoData infoData = new SummaryInfo.SummaryInfoData();
                    infoData.setText("收款人全称：" + value.getText());
                    infoData.setLang("zh_CN");
                    summaryInfoData.add(infoData);
                    summaryInfo.setSummaryInfoData(summaryInfoData);
                    summaryList.add(summaryInfo);
                    break;
            }
        }
        wxCpOaApplyEventRequest.setSummaryList(summaryList);
        param.setSummaryList(summaryList);
        param.setMediaIds(mediaIdList);
        String apply = wxCpService.getWxCpClient().getOaService().apply(wxCpOaApplyEventRequest);
        Long userId = LoginContextHolder.getContext().getUserId();
        if (mediaIdList.size()>0){
            paymentApply.setFiled(StringUtils.join( mediaIdList,","));
        }

        //TODO 保存数据
        paymentApply.setSpNo(apply);
        orderPaymentApplyService.save(paymentApply);
        WxAudit entity = new WxAudit();
        entity.setCreatorUser(userId);
        entity.setSpNo(apply);
        entity.setStatus(0);
        entity.setTemplateId(param.getTemplateId());
        entity.setMsg(JSON.toJSONString(param));
        this.save(entity);
        log.debug(apply);

        return apply;
    }
    @Override
    public WxCpTemplateResult getTemplate(String templateId) throws WxErrorException {
        WxCpTemplateResult templateDetail = wxCpService.getWxCpClient().getOaService().getTemplateDetail(templateId);
        return templateDetail;
    }
    @Override
    public WxCpApprovalDetailResult getDetail(String spNo) throws WxErrorException {
        if (ToolUtil.isEmpty(this.getById(spNo))){
            throw new ServiceException(500,"参数不正确");
        }
        return wxCpService.getWxCpClient().getOaService().getApprovalDetail(spNo);
    }
}
