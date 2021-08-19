package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.banner.entity.Banner;
import cn.atsoft.dasheng.portal.goods.entity.Goods;
import cn.atsoft.dasheng.portal.goods.service.GoodsService;
import cn.atsoft.dasheng.portal.goodsdetails.entity.GoodsDetails;
import cn.atsoft.dasheng.portal.goodsdetails.service.GoodsDetailsService;
import cn.atsoft.dasheng.portal.goodsdetailsbanner.entity.GoodsDetailsBanner;
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
        QueryWrapper<GoodsDetails> goodDetailQueryWrapper = new QueryWrapper<>();
        goodDetailQueryWrapper.in("good_id", goodId).orderByAsc("sort").in("display",1);
        List<GoodsDetails> list = goodsDetailsService.list(goodDetailQueryWrapper);

        QueryWrapper<GoodsDetailsBanner> goodDetailBannerQueryWrapper = new QueryWrapper<>();
        for(GoodsDetails data : list){
            goodDetailBannerQueryWrapper.in("good_details_id",  data.getGoodDetailsId()).orderByAsc("sort").in("display",1);
            List<GoodsDetailsBanner> bannerList = goodsDetailsBannerService.list(goodDetailBannerQueryWrapper);
            data.setGoodsDetailsBannerList(bannerList);
        }
        return ResponseData.success(list);
    }
}
