package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListingParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListingResult;
import cn.atsoft.dasheng.purchase.pojo.ListingPlan;
import cn.atsoft.dasheng.purchase.pojo.PlanListParam;
import cn.atsoft.dasheng.purchase.service.PurchaseListingService;
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
import java.util.Set;


/**
 * 采购清单控制器
 *
 * @author song
 * @Date 2021-12-15 09:35:37
 */
@RestController
@RequestMapping("/purchaseListing")
@Api(tags = "采购清单")
public class PurchaseListingController extends BaseController {

    @Autowired
    private PurchaseListingService purchaseListingService;
    @Autowired
    private StockDetailsService stockDetailsService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-12-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PurchaseListingParam purchaseListingParam) {
        this.purchaseListingService.add(purchaseListingParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-12-15
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PurchaseListingParam purchaseListingParam) {

        this.purchaseListingService.update(purchaseListingParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-12-15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PurchaseListingParam purchaseListingParam) {
        this.purchaseListingService.delete(purchaseListingParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-12-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<PurchaseListingResult> detail(@RequestBody PurchaseListingParam purchaseListingParam) {
        PurchaseListing detail = this.purchaseListingService.getById(purchaseListingParam.getPurchaseListingId());
        PurchaseListingResult result = new PurchaseListingResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-12-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PurchaseListingResult> list(@RequestBody(required = false) PurchaseListingParam purchaseListingParam) {
        if (ToolUtil.isEmpty(purchaseListingParam)) {
            purchaseListingParam = new PurchaseListingParam();
        }
        return this.purchaseListingService.findPageBySpec(purchaseListingParam);
    }

    /**
     * 待买
     *
     * @author song
     * @Date 2021-12-15
     */
    @RequestMapping(value = "/planList", method = RequestMethod.POST)
    @ApiOperation("待买")
    public ResponseData planList(@RequestBody(required = false) PlanListParam param) {
        if (ToolUtil.isEmpty(param)) {
            param = new PlanListParam();
        }
        Set<ListingPlan> plans = this.purchaseListingService.plans(param);
        if (ToolUtil.isEmpty(plans)) {
            return ResponseData.success(null);
        }
        List<ListingPlan> planList = new ArrayList<>(plans);
        planList.removeIf(plan -> plan.getSkuResult() == null);

        stockDetailsService.preorder(planList);

        return ResponseData.success(planList);
    }



}


