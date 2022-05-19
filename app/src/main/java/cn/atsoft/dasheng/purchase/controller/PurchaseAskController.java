package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;

import cn.atsoft.dasheng.action.dict.PurchaseAskDictEnum;
import cn.atsoft.dasheng.purchase.model.params.PurchaseAskParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseAskResult;
import cn.atsoft.dasheng.purchase.service.PurchaseAskService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 采购申请控制器
 *
 * @author song
 * @Date 2021-12-15 09:35:37
 */
@RestController
@RequestMapping("/purchaseAsk")
@Api(tags = "采购申请")
public class PurchaseAskController extends BaseController {

    @Autowired
    private PurchaseAskService purchaseAskService;


    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-12-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PurchaseAskParam purchaseAskParam) {

        this.purchaseAskService.add(purchaseAskParam);
        return ResponseData.success();
    }





    @RequestMapping(value = "/rejected", method = RequestMethod.GET)
    @ApiOperation("驳回")
    public ResponseData rejected(@RequestParam Long askId) {
        this.purchaseAskService.rejected(askId);
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
    public ResponseData update(@RequestBody PurchaseAskParam purchaseAskParam) {
        throw new ServiceException(500, "不可以修改");
    }

//    /**
//     * 删除接口
//     *
//     * @author song
//     * @Date 2021-12-15
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody PurchaseAskParam purchaseAskParam)  {
//        this.purchaseAskService.delete(purchaseAskParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-12-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<PurchaseAskResult> detail(@RequestBody PurchaseAskParam purchaseAskParam) {
        PurchaseAskResult detail = this.purchaseAskService.detail(purchaseAskParam);
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-12-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PurchaseAskResult> list(@RequestBody(required = false) PurchaseAskParam purchaseAskParam) {
        if (ToolUtil.isEmpty(purchaseAskParam)) {
            purchaseAskParam = new PurchaseAskParam();
        }
        return this.purchaseAskService.findPageBySpec(purchaseAskParam);
    }


}


