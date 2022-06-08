package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Announcements;
import cn.atsoft.dasheng.erp.entity.AnomalyDetail;
import cn.atsoft.dasheng.erp.mapper.AnomalyDetailMapper;
import cn.atsoft.dasheng.erp.model.params.AnomalyDetailParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyDetailResult;
import cn.atsoft.dasheng.erp.service.AnnouncementsService;
import cn.atsoft.dasheng.erp.service.AnomalyBindService;
import cn.atsoft.dasheng.erp.service.AnomalyDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void update(AnomalyDetailParam param) {
        AnomalyDetail oldEntity = getOldEntity(param);
        AnomalyDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
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
        for (AnomalyDetailResult result : results) {
            Integer count = bindService.query().eq("detail_id", result.getDetailId()).eq("display", 1).count();
            result.setNumber(Long.valueOf(count));
        }
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

    private void format(List<AnomalyDetailResult> data) {

        for (AnomalyDetailResult datum : data) {
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
