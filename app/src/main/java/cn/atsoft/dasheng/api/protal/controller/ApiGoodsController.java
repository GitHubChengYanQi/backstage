package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.portal.banner.model.response.ResponseData;
import cn.atsoft.dasheng.portal.goods.entity.Goods;
import cn.atsoft.dasheng.portal.goods.service.GoodsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiGoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/getGoodsList", method = RequestMethod.GET)
    public ResponseData getGoods() {
        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        List<Goods> list = goodsService.list(goodsQueryWrapper);
        return ResponseData.success(list);
    }
}
