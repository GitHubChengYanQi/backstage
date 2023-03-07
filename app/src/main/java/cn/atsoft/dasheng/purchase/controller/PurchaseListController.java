package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.PurchaseList;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListResult;
import cn.atsoft.dasheng.purchase.service.PurchaseListService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 预购管理控制器
 *
 * @author Captain_Jazz
 * @Date 2023-03-04 10:58:41
 */
@RestController
@RequestMapping("/purchaseList")
@Api(tags = "预购管理")
public class PurchaseListController extends BaseController {

    @Autowired
    private PurchaseListService purchaseListService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PurchaseListParam purchaseListParam) {
        this.purchaseListService.add(purchaseListParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PurchaseListParam purchaseListParam) {

        this.purchaseListService.update(purchaseListParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PurchaseListParam purchaseListParam)  {
        this.purchaseListService.delete(purchaseListParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<PurchaseListResult> detail(@RequestBody PurchaseListParam purchaseListParam) {
        PurchaseList detail = this.purchaseListService.getById(purchaseListParam.getPurchaseListId());
        PurchaseListResult result = new PurchaseListResult();
        ToolUtil.copyProperties(detail, result);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PurchaseListResult> list(@RequestBody(required = false) PurchaseListParam purchaseListParam) {
        if(ToolUtil.isEmpty(purchaseListParam)){
            purchaseListParam = new PurchaseListParam();
        }
        return this.purchaseListService.findPageBySpec(purchaseListParam);
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    @RequestMapping(value = "/noPageList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData noPageList(@RequestBody(required = false) PurchaseListParam purchaseListParam) {
        if(ToolUtil.isEmpty(purchaseListParam)){
            purchaseListParam = new PurchaseListParam();
        }
        return ResponseData.success(purchaseListService.findListBySpec(purchaseListParam));
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-04
     */
    @RequestMapping(value = "/listBySkuId", method = RequestMethod.GET)
    @ApiOperation("列表")
    public ResponseData noPageList(@RequestParam Long skuId) {

        return ResponseData.success(purchaseListService.listBySkuId(skuId));
    }




}


