package cn.atsoft.dasheng.portal.goods.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.banner.model.response.ResponseData;
import cn.atsoft.dasheng.portal.goods.entity.Goods;
import cn.atsoft.dasheng.portal.goods.model.params.GoodsParam;
import cn.atsoft.dasheng.portal.goods.model.result.GoodsResult;
import cn.atsoft.dasheng.portal.goods.service.GoodsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 首页商品控制器
 *
 * @author siqiang
 * @Date 2021-08-19 08:53:11
 */
@RestController
@RequestMapping("/goods")
@Api(tags = "首页商品")
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 新增接口
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody GoodsParam goodsParam) {
        this.goodsService.add(goodsParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody GoodsParam goodsParam) {

        this.goodsService.update(goodsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody GoodsParam goodsParam)  {
        this.goodsService.delete(goodsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<GoodsResult> detail(@RequestBody GoodsParam goodsParam) {
        Goods detail = this.goodsService.getById(goodsParam.getGoodId());
        GoodsResult result = new GoodsResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<GoodsResult> list(@RequestBody(required = false) GoodsParam goodsParam) {
        if(ToolUtil.isEmpty(goodsParam)){
            goodsParam = new GoodsParam();
        }
        return this.goodsService.findPageBySpec(goodsParam);
    }




}


