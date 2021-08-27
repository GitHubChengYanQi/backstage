package cn.atsoft.dasheng.portal.bannerDifference.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.bannerDifference.entity.BannerDifference;
import cn.atsoft.dasheng.portal.bannerDifference.mapper.BannerDifferenceMapper;
import cn.atsoft.dasheng.portal.bannerDifference.model.params.BannerDifferenceParam;
import cn.atsoft.dasheng.portal.bannerDifference.model.result.BannerDifferenceResult;
import  cn.atsoft.dasheng.portal.bannerDifference.service.BannerDifferenceService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 轮播图分类 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-08-18
 */
@Service
public class BannerDifferenceServiceImpl extends ServiceImpl<BannerDifferenceMapper, BannerDifference> implements BannerDifferenceService {

    @Override
    public void add(BannerDifferenceParam param){
        BannerDifference entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(BannerDifferenceParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(BannerDifferenceParam param){
        BannerDifference oldEntity = getOldEntity(param);
        BannerDifference newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public BannerDifferenceResult findBySpec(BannerDifferenceParam param){
        return null;
    }

    @Override
    public List<BannerDifferenceResult> findListBySpec(BannerDifferenceParam param){
        return null;
    }

    @Override
    public PageInfo<BannerDifferenceResult> findPageBySpec(BannerDifferenceParam param){
        Page<BannerDifferenceResult> pageContext = getPageContext();
        IPage<BannerDifferenceResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(BannerDifferenceParam param){
        return param.getClassificationId();
    }

    private Page<BannerDifferenceResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private BannerDifference getOldEntity(BannerDifferenceParam param) {
        return this.getById(getKey(param));
    }

    private BannerDifference getEntity(BannerDifferenceParam param) {
        BannerDifference entity = new BannerDifference();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
