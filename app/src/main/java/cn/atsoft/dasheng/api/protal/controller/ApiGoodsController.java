package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.goods.entity.Goods;
import cn.atsoft.dasheng.portal.goods.model.params.GoodsParam;
import cn.atsoft.dasheng.portal.goods.model.result.GoodsResult;
import cn.atsoft.dasheng.portal.goods.service.GoodsService;
import cn.atsoft.dasheng.uc.utils.UserUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiGoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 推荐商品详情
     *
     * @return
     */
    @RequestMapping(value = "/getGoodsList", method = RequestMethod.GET)
    public ResponseData getGoods() {
        UserUtils.getUserId();
        QueryWrapper<Goods> goodsQueryWrapper = new QueryWrapper<>();
        List<Goods> list = goodsService.list(goodsQueryWrapper);
        return ResponseData.success(list);
    }

    /**
     * 查看详情接口
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody GoodsParam goodsParam) {
        Goods detail = this.goodsService.getById(goodsParam.getGoodId());
        GoodsResult result = new GoodsResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }
}
