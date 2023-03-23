package cn.atsoft.dasheng.portal.goodsDetailsBanner.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.goodsDetailsBanner.entity.GoodsDetailsBanner;
import cn.atsoft.dasheng.portal.goodsDetailsBanner.model.params.GoodsDetailsBannerParam;
import cn.atsoft.dasheng.portal.goodsDetailsBanner.model.result.GoodsDetailsBannerResult;
import cn.atsoft.dasheng.portal.goodsDetailsBanner.service.GoodsDetailsBannerService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 商品轮播图控制器
 *
 * @author siqiang
 * @Date 2021-08-19 16:34:29
 */
@RestController
@RequestMapping("/goodsDetailsBanner")
@Api(tags = "商品轮播图")
public class GoodsDetailsBannerController extends BaseController {

    @Autowired
    private GoodsDetailsBannerService goodsDetailsBannerService;

    /**
     * 新增接口
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody GoodsDetailsBannerParam goodsDetailsBannerParam) {
        this.goodsDetailsBannerService.add(goodsDetailsBannerParam);
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
    public ResponseData update(@RequestBody GoodsDetailsBannerParam goodsDetailsBannerParam) {

        this.goodsDetailsBannerService.update(goodsDetailsBannerParam);
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
    public ResponseData delete(@RequestBody GoodsDetailsBannerParam goodsDetailsBannerParam)  {
        this.goodsDetailsBannerService.delete(goodsDetailsBannerParam);
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
    public ResponseData detail(@RequestBody GoodsDetailsBannerParam goodsDetailsBannerParam) {
        GoodsDetailsBanner detail = this.goodsDetailsBannerService.getById(goodsDetailsBannerParam.getDetailBannerId());
        GoodsDetailsBannerResult result = new GoodsDetailsBannerResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

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
    public PageInfo<GoodsDetailsBannerResult> list(@RequestBody(required = false) GoodsDetailsBannerParam goodsDetailsBannerParam) {
        if(ToolUtil.isEmpty(goodsDetailsBannerParam)){
            goodsDetailsBannerParam = new GoodsDetailsBannerParam();
        }
        return this.goodsDetailsBannerService.findPageBySpec(goodsDetailsBannerParam);
    }




}


