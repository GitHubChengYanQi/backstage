package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.model.LoginUser;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.config.MobileService;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.AnomalyDetailMapper;
import cn.atsoft.dasheng.erp.model.params.AnomalyDetailParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyDetailResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessTaskParam;
import cn.atsoft.dasheng.message.entity.MarkDownTemplate;
import cn.atsoft.dasheng.form.entity.ActivitiProcessTask;
import cn.atsoft.dasheng.form.model.params.RemarksParam;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.message.enmu.OperationType;
import cn.atsoft.dasheng.message.entity.RemarksEntity;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sendTemplate.WxCpSendTemplate;
import cn.atsoft.dasheng.sendTemplate.WxCpTemplate;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-05-27
 */
@Service
public class AnomalyDetailServiceImpl extends ServiceImpl<AnomalyDetailMapper, AnomalyDetail> implements AnomalyDetailService {

    @Autowired
    private MediaService mediaService;
    @Autowired
    private AnomalyBindService bindService;
    @Autowired
    private AnnouncementsService announcementsService;
    @Autowired
    private UserService userService;
    @Autowired
    private MobileService mobileService;
    @Autowired
    private WxCpSendTemplate wxCpSendTemplate;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private AnomalyService anomalyService;
    @Autowired
    private InstockLogDetailService instockLogDetailService;
    @Autowired
    private ActivitiProcessTaskService taskService;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private ActivitiProcessTaskService activitiProcessTaskService;
    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private SkuService skuService;

    @Override
    public void add(AnomalyDetailParam param) {
        AnomalyDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AnomalyDetailParam param) {
        AnomalyDetail entity = getEntity(param);
        entity.setDisplay(0);
        this.updateById(entity);
    }

    @Override
    @Transactional
    public void update(AnomalyDetailParam param) {
        AnomalyDetail oldEntity = getOldEntity(param);
        AnomalyDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        if (ToolUtil.isNotEmpty(param.getUserId()) && oldEntity.getStauts() == 0) {
            pushPeople(param.getUserId(), oldEntity.getAnomalyId());
        }
        this.updateById(newEntity);

        String skuMessage = "";
        Anomaly anomaly = anomalyService.getById(oldEntity.getAnomalyId());
        if (ToolUtil.isNotEmpty(anomaly)) {
            skuMessage = skuService.skuMessage(anomaly.getSkuId());
        }
        if (ToolUtil.isNotEmpty(param.getAnomalyOrderId())) {
            ActivitiProcessTask task = taskService.getByFormId(param.getAnomalyOrderId());
            if (ToolUtil.isNotEmpty(task)) {
                if (ToolUtil.isNotEmpty(param.getStauts())) {
                    switch (param.getStauts().toString()) {
                        case "-1":
                            skuMessage = "对" + skuMessage + "给出了终止入库处理意见";
                            break;
                        case "1":
                            skuMessage = "对" + skuMessage + "给出了允许入库处理意见";
                            break;
                        case "0":
                            skuMessage = skuMessage + "修改了处理意见";
                            break;
                    }
                }

                /**
                 * 添加动态记录
                 */
                shopCartService.addDynamic(param.getAnomalyOrderId(), skuMessage);

                if (ToolUtil.isNotEmpty(param.getUserId()) && oldEntity.getStauts() == 0) {
                    if (LoginContextHolder.getContext().getUserId().equals(param.getUserId())) {
                        throw new ServiceException(500, "不可转交自己");
                    }
                    User user = userService.getById(param.getUserId());
                    skuMessage = skuService.skuMessage(anomaly.getSkuId());
                    shopCartService.addDynamic(param.getAnomalyOrderId(), "将" + skuMessage + "转交给" + user.getName() + "进行处理");
                    forWard(oldEntity);   //异常明细转交处理
                }

            }
        }
    }

    /**
     * 转交处理
     */
    private void forWard(AnomalyDetail detail) {
        LoginUser user = LoginContextHolder.getContext().getUser();
        ActivitiProcessTaskParam activitiProcessTaskParam = new ActivitiProcessTaskParam();
        activitiProcessTaskParam.setTaskName(user.getName() + "转交的异常处理");
        activitiProcessTaskParam.setUserIds(detail.getUserId().toString());
        activitiProcessTaskParam.setFormId(detail.getAnomalyId());
        activitiProcessTaskParam.setType("ErrorForWard");
        activitiProcessTaskParam.setUserId(detail.getUserId());
        Long taskId = activitiProcessTaskService.add(activitiProcessTaskParam);
        wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
            setType(1);
            setItems("入库异常 转交您处理");
            setUrl(mobileService.getMobileConfig().getUrl() + "/Receipts/ReceiptsDetail?id=" + taskId);
            setDescription("入库异常 转交处理");
            setUserIds(new ArrayList<Long>() {{
                add(detail.getUserId());
            }});
        }});
    }

    /**
     * 转交人处理
     *
     * @param userId
     * @param id
     */
    @Override
    public void pushPeople(Long userId, Long id) {
        Anomaly anomalyServiceById = this.anomalyService.getById(id);
        wxCpSendTemplate.sendMarkDownTemplate(new MarkDownTemplate() {{
            setType(1);
            setItems("入库异常 转交您处理");
            setUrl(mobileService.getMobileConfig().getUrl() + "/#/Work/Error/Detail/Handle?id=" + id);
            setDescription("入库异常 转交处理");
            setRemark(anomalyServiceById.getRemark());
            setUserIds(new ArrayList<Long>() {{
                add(userId);
            }});
        }});
    }

    @Override
    public AnomalyDetailResult findBySpec(AnomalyDetailParam param) {
        return null;
    }

    @Override
    public List<AnomalyDetailResult> findListBySpec(AnomalyDetailParam param) {
        return null;
    }

    @Override
    public List<AnomalyDetailResult> getDetails(Long anomalyId) {
        if (ToolUtil.isEmpty(anomalyId)) {
            return new ArrayList<>();
        }
        List<AnomalyDetail> details = this.query().eq("anomaly_id", anomalyId).eq("display", 1).list();
        List<AnomalyDetailResult> results = BeanUtil.copyToList(details, AnomalyDetailResult.class, new CopyOptions());
        format(results);
        return results;
    }


    @Override
    public PageInfo<AnomalyDetailResult> findPageBySpec(AnomalyDetailParam param) {
        Page<AnomalyDetailResult> pageContext = getPageContext();
        IPage<AnomalyDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AnomalyDetailParam param) {
        return param.getDetailId();
    }

    private Page<AnomalyDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AnomalyDetail getOldEntity(AnomalyDetailParam param) {
        return this.getById(getKey(param));
    }

    private AnomalyDetail getEntity(AnomalyDetailParam param) {
        AnomalyDetail entity = new AnomalyDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public void format(List<AnomalyDetailResult> data) {

        List<Long> userIds = new ArrayList<>();
        for (AnomalyDetailResult datum : data) {
            userIds.add(datum.getUserId());
        }

        List<User> userList = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);

        for (AnomalyDetailResult datum : data) {
            for (User user : userList) {
                if (ToolUtil.isNotEmpty(datum.getUserId()) && datum.getUserId().equals(user.getUserId())) {
                    datum.setUser(user);
                    break;
                }
            }

            if (ToolUtil.isNotEmpty(datum.getRemark())) {
                List<Long> noticeIds = JSON.parseArray(datum.getRemark(), Long.class);
                List<Announcements> announcementsList = announcementsService.listByIds(noticeIds);
                datum.setAnnouncements(announcementsList);
            }


            if (ToolUtil.isNotEmpty(datum.getOpinionImg())) {
                List<Long> opinionImgIds = JSON.parseArray(datum.getOpinionImg(), Long.class);
                List<String> opinionUrls = new ArrayList<>();
                for (Long opinionImgId : opinionImgIds) {
                    String mediaUrl = mediaService.getMediaUrl(opinionImgId, 0L);
                    opinionUrls.add(mediaUrl);
                }
                datum.setOpinionUrls(opinionUrls);
            }

            if (ToolUtil.isNotEmpty(datum.getReasonImg())) {
                List<Long> reasionImgIds = JSON.parseArray(datum.getReasonImg(), Long.class);
                List<String> reasonUrls = new ArrayList<>();
                for (Long mediaId : reasionImgIds) {
                    String mediaUrl = mediaService.getMediaUrl(mediaId, 0L);
                    reasonUrls.add(mediaUrl);
                }
                datum.setReasonUrls(reasonUrls);
            }
        }
    }
}
