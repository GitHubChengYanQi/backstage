package cn.atsoft.dasheng.portal.goodsDetails.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.goodsDetails.entity.GoodsDetails;
import cn.atsoft.dasheng.portal.goodsDetails.mapper.GoodsDetailsMapper;
import cn.atsoft.dasheng.portal.goodsDetails.model.params.GoodsDetailsParam;
import cn.atsoft.dasheng.portal.goodsDetails.model.result.GoodsDetailsResult;
import  cn.atsoft.dasheng.portal.goodsDetails.service.GoodsDetailsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.goodsDetailsBanner.entity.GoodsDetailsBanner;
import cn.atsoft.dasheng.portal.goodsDetailsBanner.service.GoodsDetailsBannerService;
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
 * 首页商品详情 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
@Service
public class GoodsDetailsServiceImpl extends ServiceImpl<GoodsDetailsMapper, GoodsDetails> implements GoodsDetailsService {

    @Autowired
    private GoodsDetailsBannerService goodsDetailsBannerService;
    @Override
    public Long add(GoodsDetailsParam param){
        GoodsDetails entity = getEntity(param);
        this.save(entity);
        return entity.getGoodDetailsId();
    }

    @Override
    public void delete(GoodsDetailsParam param){
        try {
            // 删除商品轮播图
            List detailBannerIds = new ArrayList<>();
            List<GoodsDetailsBanner> goodsDetailsBanner = this.goodsDetailsBannerService.list();
            for(GoodsDetailsBanner bannerData : goodsDetailsBanner) {
                if(bannerData.getGoodDetailsId() == param.getGoodDetailsId()){
                    detailBannerIds.add(bannerData.getDetailBannerId());
                }
            }
            this.goodsDetailsBannerService.removeByIds(detailBannerIds);
            this.removeById(getKey(param));
        }catch (Exception e){
            //
        }
    }

    @Override
    public Long update(GoodsDetailsParam param){
        GoodsDetails oldEntity = getOldEntity(param);
        GoodsDetails newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        return newEntity.getGoodDetailsId();
    }

    @Override
    public GoodsDetailsResult findBySpec(GoodsDetailsParam param){
        return null;
    }

    @Override
    public List<GoodsDetailsResult> findListBySpec(GoodsDetailsParam param){
        return null;
    }

    @Override
    public PageInfo<GoodsDetailsResult> findPageBySpec(GoodsDetailsParam param){
        Page<GoodsDetailsResult> pageContext = getPageContext();
        IPage<GoodsDetailsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(GoodsDetailsParam param){
        return param.getGoodDetailsId();
    }

    private Page<GoodsDetailsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private GoodsDetails getOldEntity(GoodsDetailsParam param) {
        return this.getById(getKey(param));
    }

    private GoodsDetails getEntity(GoodsDetailsParam param) {
        GoodsDetails entity = new GoodsDetails();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
