package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.app.model.params.AdressParam;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.banner.entity.Banner;
import cn.atsoft.dasheng.portal.goods.entity.Goods;
import cn.atsoft.dasheng.portal.goods.service.GoodsService;
import cn.atsoft.dasheng.portal.goodsdetails.entity.GoodsDetails;
import cn.atsoft.dasheng.portal.goodsdetails.model.params.GoodsDetailsParam;
import cn.atsoft.dasheng.portal.goodsdetails.model.result.GoodsDetailsResult;
import cn.atsoft.dasheng.portal.goodsdetails.service.GoodsDetailsService;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.entity.GoodsDetailsBanner;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.model.params.GoodsDetailsBannerParam;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.model.result.GoodsDetailsBannerResult;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.service.GoodsDetailsBannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiGoodsDetailsController {


    @Autowired
    private GoodsDetailsService goodsDetailsService;
    @Autowired
    private GoodsDetailsBannerService goodsDetailsBannerService;

    @RequestMapping(value = "/getGoodsDetails", method = RequestMethod.POST)
    public ResponseData getGoodsDetails(@RequestBody GoodsDetailsParam goodsDetailsParam) {

        List<GoodsDetails> detailDetailsList = goodsDetailsService.list();
        GoodsDetailsResult goodsDetailsResult = new GoodsDetailsResult();
        if(ToolUtil.isNotEmpty(detailDetailsList)){
            for(GoodsDetails data : detailDetailsList){
                if(data.getGoodId().equals(goodsDetailsParam.getGoodId())){
                    goodsDetailsResult.setGoodDetailsId(data.getGoodDetailsId());
                    goodsDetailsResult.setGoodId(data.getGoodId());
                    goodsDetailsResult.setDetailBannerId(data.getDetailBannerId());
                    goodsDetailsResult.setTitle(data.getTitle());
                    goodsDetailsResult.setPrice(data.getPrice());
                    goodsDetailsResult.setLastPrice(data.getLastPrice());
                    goodsDetailsResult.setServer(data.getServer());
                    goodsDetailsResult.setSpecificationId(data.getSpecificationId());
                    goodsDetailsResult.setDetails(data.getDetails());
                    goodsDetailsResult.setSort(data.getSort());

                    List<GoodsDetailsBanner> detailDetailsBannerList = goodsDetailsBannerService.list();

                    List<GoodsDetailsBannerResult> goodsDetailsBannerResultList = new ArrayList<>();
                    for(GoodsDetailsBanner bannerList : detailDetailsBannerList){
                        if(bannerList.getGoodDetailsId().equals(data.getGoodDetailsId())){
                            GoodsDetailsBannerResult goodsDetailsBannerResult = new GoodsDetailsBannerResult();
                            goodsDetailsBannerResult.setGoodDetailsId(bannerList.getGoodDetailsId());
                            goodsDetailsBannerResult.setDetailBannerId(bannerList.getDetailBannerId());
                            goodsDetailsBannerResult.setSort(bannerList.getSort());
                            goodsDetailsBannerResult.setImgUrl(bannerList.getImgUrl());
                            goodsDetailsBannerResultList.add(goodsDetailsBannerResult);
                        }
                    }
                    goodsDetailsResult.setGoodsDetailsBannerList(goodsDetailsBannerResultList);
                    break;
                }
            }
        }
        return ResponseData.success(goodsDetailsResult);
    }
}
