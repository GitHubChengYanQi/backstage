package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.PurchaseConfig;
import cn.atsoft.dasheng.purchase.model.params.PurchaseConfigParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseConfigResult;
import cn.atsoft.dasheng.purchase.service.PurchaseConfigService;
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
 * 采购配置表控制器
 *
 * @author song
 * @Date 2021-12-21 14:00:27
 */
@RestController
@RequestMapping("/purchaseConfig")
@Api(tags = "采购配置表")
public class PurchaseConfigController extends BaseController {

    @Autowired
    private PurchaseConfigService purchaseConfigService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PurchaseConfigParam purchaseConfigParam) {
        this.purchaseConfigService.add(purchaseConfigParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PurchaseConfigParam purchaseConfigParam) {

        this.purchaseConfigService.update(purchaseConfigParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PurchaseConfigParam purchaseConfigParam)  {
        this.purchaseConfigService.delete(purchaseConfigParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody PurchaseConfigParam purchaseConfigParam) {
        PurchaseConfig detail = this.purchaseConfigService.getById(purchaseConfigParam.getPurchaseConfigId());
        PurchaseConfigResult result = new PurchaseConfigResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PurchaseConfigResult> list(@RequestBody(required = false) PurchaseConfigParam purchaseConfigParam) {
        if(ToolUtil.isEmpty(purchaseConfigParam)){
            purchaseConfigParam = new PurchaseConfigParam();
        }
        return this.purchaseConfigService.findPageBySpec(purchaseConfigParam);
    }




}


