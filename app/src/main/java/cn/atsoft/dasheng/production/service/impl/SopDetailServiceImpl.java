package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.SopDetail;
import cn.atsoft.dasheng.production.mapper.SopDetailMapper;
import cn.atsoft.dasheng.production.model.params.SopDetailParam;
import cn.atsoft.dasheng.production.model.result.SopDetailResult;
import cn.atsoft.dasheng.production.service.SopDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
 * sop 详情 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-02-10
 */
@Service
public class SopDetailServiceImpl extends ServiceImpl<SopDetailMapper, SopDetail> implements SopDetailService {

    @Autowired
    private MediaService mediaService;

    @Override
    public void add(SopDetailParam param) {
        SopDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SopDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(SopDetailParam param) {
        SopDetail oldEntity = getOldEntity(param);
        SopDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SopDetailResult findBySpec(SopDetailParam param) {
        return null;
    }

    @Override
    public List<SopDetailResult> findListBySpec(SopDetailParam param) {
        return null;
    }

    @Override
    public List<SopDetailResult> getResultBySopId(Long sopId) {
        if (ToolUtil.isEmpty(sopId)) {
            return new ArrayList<>();
        }
        List<SopDetail> sopDetails = this.query().eq("sop_id", sopId).orderByDesc("sort").list();
        if (ToolUtil.isEmpty(sopDetails)) {
            return new ArrayList<>();
        }
        List<SopDetailResult> details = BeanUtil.copyToList(sopDetails, SopDetailResult.class, new CopyOptions());

        for (SopDetailResult detail : details) {
            if (ToolUtil.isNotEmpty(detail.getImg())) {
                Long img = Long.valueOf(detail.getImg());
                String mediaUrl = mediaService.getMediaUrl(img, 0L);
                detail.setMediaUrl(mediaUrl);
            }
        }
        return details;
    }

    @Override
    public PageInfo<SopDetailResult> findPageBySpec(SopDetailParam param) {
        Page<SopDetailResult> pageContext = getPageContext();
        IPage<SopDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SopDetailParam param) {
        return param.getSopDetailId();
    }

    private Page<SopDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SopDetail getOldEntity(SopDetailParam param) {
        return this.getById(getKey(param));
    }

    private SopDetail getEntity(SopDetailParam param) {
        SopDetail entity = new SopDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
