package cn.atsoft.dasheng.portal.goodsDetails.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.goodsDetails.entity.GoodsDetails;
import cn.atsoft.dasheng.portal.goodsDetails.model.params.GoodsDetailsParam;
import cn.atsoft.dasheng.portal.goodsDetails.model.result.GoodsDetailsResult;
import cn.atsoft.dasheng.portal.goodsDetails.service.GoodsDetailsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 首页商品详情控制器
 *
 * @author siqiang
 * @Date 2021-08-19 13:30:45
 */
@RestController
@RequestMapping("/goodsDetails")
@Api(tags = "首页商品详情")
public class GoodsDetailsController extends BaseController {

    @Autowired
    private GoodsDetailsService goodsDetailsService;

    /**
     * 新增接口
     *
     * @author siqiang
     * @Date 2021-08-19
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody GoodsDetailsParam goodsDetailsParam) {
        this.goodsDetailsService.add(goodsDetailsParam);
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
    public ResponseData update(@RequestBody GoodsDetailsParam goodsDetailsParam) {

        this.goodsDetailsService.update(goodsDetailsParam);
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
    public ResponseData delete(@RequestBody GoodsDetailsParam goodsDetailsParam)  {
        this.goodsDetailsService.delete(goodsDetailsParam);
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
    public ResponseData detail(@RequestBody GoodsDetailsParam goodsDetailsParam) {
        GoodsDetails detail = this.goodsDetailsService.getById(goodsDetailsParam.getGoodDetailsId());
        GoodsDetailsResult result = new GoodsDetailsResult();
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
    public PageInfo<GoodsDetailsResult> list(@RequestBody(required = false) GoodsDetailsParam goodsDetailsParam) {
        if(ToolUtil.isEmpty(goodsDetailsParam)){
            goodsDetailsParam = new GoodsDetailsParam();
        }
        return this.goodsDetailsService.findPageBySpec(goodsDetailsParam);
    }




}


