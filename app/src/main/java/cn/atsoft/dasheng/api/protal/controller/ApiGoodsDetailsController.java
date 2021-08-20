package cn.atsoft.dasheng.api.protal.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiGoodsDetailsController {


    @Autowired
    private GoodsDetailsService goodsDetailsService;
    @Autowired
    private GoodsDetailsBannerService goodsDetailsBannerService;

    @RequestMapping(value = "/getGoodsDetails", method = RequestMethod.GET)
    public ResponseData getGoods(@RequestParam("goodId") Long goodId) {

        GoodsDetailsParam goodsDetailParam = new GoodsDetailsParam();
        goodsDetailParam.setGoodId(goodId);
        List<GoodsDetailsResult> detailDetailsList = goodsDetailsService.findListBySpec(goodsDetailParam);
        GoodsDetailsBannerParam  goodsDetailsBannerParam = new GoodsDetailsBannerParam();

        if(ToolUtil.isNotEmpty(detailDetailsList)){
            for(GoodsDetailsResult data : detailDetailsList){
                goodsDetailsBannerParam.setGoodDetailsId(data.getGoodDetailsId());
                List<GoodsDetailsBannerResult> detailDetailsBannerList = goodsDetailsBannerService.findListBySpec(goodsDetailsBannerParam);
                data.setGoodsDetailsBannerList(detailDetailsBannerList);
            }

        }
        return ResponseData.success(detailDetailsList);
    }
}
