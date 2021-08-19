package cn.atsoft.dasheng.portal.goods.service.impl;


import cn.atsoft.dasheng.app.entity.Outstock;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.portal.goods.entity.Goods;
import cn.atsoft.dasheng.portal.goods.mapper.GoodsMapper;
import cn.atsoft.dasheng.portal.goods.model.params.GoodsParam;
import cn.atsoft.dasheng.portal.goods.model.result.GoodsResult;
import  cn.atsoft.dasheng.portal.goods.service.GoodsService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.portal.goodsdetails.entity.GoodsDetails;
import cn.atsoft.dasheng.portal.goodsdetails.model.params.GoodsDetailsParam;
import cn.atsoft.dasheng.portal.goodsdetails.model.result.GoodsDetailsResult;
import cn.atsoft.dasheng.portal.goodsdetails.service.GoodsDetailsService;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.entity.GoodsDetailsBanner;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.model.params.GoodsDetailsBannerParam;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.service.GoodsDetailsBannerService;
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
 * 首页商品 服务实现类
 * </p>
 *
 * @author siqiang
 * @since 2021-08-19
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsDetailsService goodsDetailsService;
    @Autowired
    private GoodsDetailsBannerService goodsDetailsBannerService;

    @Override
    public void add(GoodsParam param){
        Goods entity = getEntity(param);
        this.save(entity);
        // 加入内容到商品详细表
        GoodsDetailsParam goodsDetail = new GoodsDetailsParam();
        goodsDetail.setGoodId(entity.getGoodId());
        goodsDetail.setTitle(entity.getTitle());
        goodsDetail.setPrice(entity.getPrice());
        goodsDetail.setLastPrice(entity.getLastPrice());
        Long goodsDetailsId = this.goodsDetailsService.add(goodsDetail);
        // 把图片加到商品轮播图
        GoodsDetailsBannerParam goodsDetailsBanner = new GoodsDetailsBannerParam();
        goodsDetailsBanner.setGoodDetailsId(goodsDetailsId);
        goodsDetailsBanner.setImgUrl(param.getImgUrl());
        this.goodsDetailsBannerService.add(goodsDetailsBanner);
    }

    @Override
    public void delete(GoodsParam param){
        Goods goodId = this.getById(param.getGoodId());
        if (ToolUtil.isEmpty(goodId)){
            throw new ServiceException(500,"删除目标不存在");
        }
        List<GoodsDetails> goodsDetails = this.goodsDetailsService.list();
        List detailBannerIds = new ArrayList<>();
        // 删除商品详细
        for(GoodsDetails data : goodsDetails){
            if(data.getGoodId() == param.getGoodId()) {
                this.goodsDetailsService.removeById(data.getGoodDetailsId());
                // 删除商品轮播图
                List<GoodsDetailsBanner> goodsDetailsBanner = this.goodsDetailsBannerService.list();
                for(GoodsDetailsBanner bannerData : goodsDetailsBanner) {
                    if(bannerData.getGoodDetailsId() == data.getGoodDetailsId()){
                        detailBannerIds.add(bannerData.getDetailBannerId());
                    }
                }
                this.goodsDetailsBannerService.removeByIds(detailBannerIds);
            }
        }

        param.setDisplay(0);
        this.update(param);
    }

    @Override
    public void update(GoodsParam param){
        Goods oldEntity = getOldEntity(param);
        Goods newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
        // 更新商品明细信息
        List<GoodsDetails> goodsDetails = this.goodsDetailsService.list();
        for(GoodsDetails data : goodsDetails){
            if(data.getGoodId() == param.getGoodId()){
                GoodsDetailsParam goodsDetail = new GoodsDetailsParam();
                goodsDetail.setGoodId(data.getGoodId());
                goodsDetail.setTitle(data.getTitle());
                goodsDetail.setPrice(data.getPrice());
                goodsDetail.setLastPrice(data.getLastPrice());
                Long goodsDetailsId = this.goodsDetailsService.update(goodsDetail);

                GoodsDetailsBannerParam goodsDetailsBanner = new GoodsDetailsBannerParam();
                goodsDetailsBanner.setGoodDetailsId(goodsDetailsId);
                goodsDetailsBanner.setImgUrl(param.getImgUrl());
                this.goodsDetailsBannerService.update(goodsDetailsBanner);
                break;
            }
        }
    }

    @Override
    public GoodsResult findBySpec(GoodsParam param){
        return null;
    }

    @Override
    public List<GoodsResult> findListBySpec(GoodsParam param){
        return null;
    }

    @Override
    public PageInfo<GoodsResult> findPageBySpec(GoodsParam param){
        Page<GoodsResult> pageContext = getPageContext();
        IPage<GoodsResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(GoodsParam param){
        return param.getGoodId();
    }

    private Page<GoodsResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Goods getOldEntity(GoodsParam param) {
        return this.getById(getKey(param));
    }

    private Goods getEntity(GoodsParam param) {
        Goods entity = new Goods();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
