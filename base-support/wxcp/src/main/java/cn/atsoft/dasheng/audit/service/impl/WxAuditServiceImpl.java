package cn.atsoft.dasheng.audit.service.impl;


import cn.atsoft.dasheng.audit.entity.WxAudit;
import cn.atsoft.dasheng.audit.mapper.WxAuditMapper;
import cn.atsoft.dasheng.audit.model.result.WxAuditPost;
import cn.atsoft.dasheng.audit.service.RestWxCpService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.audit.model.params.WxAuditParam;
import cn.atsoft.dasheng.audit.model.result.WxAuditResult;
import cn.atsoft.dasheng.audit.service.WxAuditService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.media.config.RestAliyunService;
import cn.atsoft.dasheng.media.entity.Media;
import cn.atsoft.dasheng.media.service.RestMediaService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpOaService;
import me.chanjar.weixin.cp.bean.oa.WxCpOaApplyEventRequest;
import me.chanjar.weixin.cp.bean.oa.WxCpTemplateResult;
import me.chanjar.weixin.cp.bean.oa.applydata.ApplyDataContent;
import me.chanjar.weixin.cp.bean.oa.applydata.ContentValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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
    public WxAuditPost post(WxCpOaApplyEventRequest param) throws WxErrorException, IOException {
        param.setCreatorUserId("RenYiTaiYu");
        param.setTemplateId("3WLJEyiwiby2y4STpyuFXsTdTdM9ste9mAbUQBzT");
        param.setNotifyType(0);
        if (ToolUtil.isEmpty(param.getApplyData()) || ToolUtil.isEmpty(param.getApplyData().getContents())){
            throw new ServiceException(500,"请传入审批申请数据");
        }
        for (ApplyDataContent content : param.getApplyData().getContents()) {
            String id = content.getId();
            ContentValue value = content.getValue();
            switch (id){
                case "item-1494251052639":
                    //TODO 保存金额
                    System.out.println(value.getNewMoney());
                    break;
                case "item-1494251220825":
                    //附件
                    for (ContentValue.File file : value.getFiles()) {
                        file.getFileId();
                        Media media = mediaService.getById(Long.parseLong(file.getFileId()));
                        OSSObject object = aliyunService.getOssClient().getObject(media.getBucket(), media.getFiledName());

                        InputStream field = object.getObjectContent();
                        WxMediaUploadResult upload = wxCpService.getWxCpClient().getMediaService().upload(FILE, object.getObjectMetadata().getContentType(), field);
                        file.setFileId(upload.getMediaId());
                    }
                    break;
            }
        }

        String apply = wxCpService.getWxCpClient().getOaService().apply(param);


        return JSON.parseObject(apply, WxAuditPost.class);
    }
    @Override
    public WxCpTemplateResult getTemplate(String templateId) throws WxErrorException {
        WxCpTemplateResult templateDetail = wxCpService.getWxCpClient().getOaService().getTemplateDetail(templateId);
        return templateDetail;
    }
}
