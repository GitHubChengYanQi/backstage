package cn.atsoft.dasheng.portal.goodsdetailsbanner.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.entity.GoodsDetailsBanner;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.mapper.GoodsDetailsBannerMapper;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.model.params.GoodsDetailsBannerParam;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.model.result.GoodsDetailsBannerResult;
import  cn.atsoft.dasheng.portal.goodsdetailsbanner.service.GoodsDetailsBannerService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品轮播图 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
@Service
public class GoodsDetailsBannerServiceImpl extends ServiceImpl<GoodsDetailsBannerMapper, GoodsDetailsBanner> implements GoodsDetailsBannerService {

    @Override
    public void add(GoodsDetailsBannerParam param){
        GoodsDetailsBanner entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(GoodsDetailsBannerParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(GoodsDetailsBannerParam param){
        GoodsDetailsBanner oldEntity = getOldEntity(param);
        GoodsDetailsBanner newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public GoodsDetailsBannerResult findBySpec(GoodsDetailsBannerParam param){
        return null;
    }

    @Override
    public List<GoodsDetailsBannerResult> findListBySpec(GoodsDetailsBannerParam param){
        return null;
    }

    @Override
    public PageInfo<GoodsDetailsBannerResult> findPageBySpec(GoodsDetailsBannerParam param){
        Page<GoodsDetailsBannerResult> pageContext = getPageContext();
        IPage<GoodsDetailsBannerResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(GoodsDetailsBannerParam param){
        return param.getDetailBannerId();
    }

    private Page<GoodsDetailsBannerResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private GoodsDetailsBanner getOldEntity(GoodsDetailsBannerParam param) {
        return this.getById(getKey(param));
    }

    private GoodsDetailsBanner getEntity(GoodsDetailsBannerParam param) {
        GoodsDetailsBanner entity = new GoodsDetailsBanner();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
