package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuValues;
import cn.atsoft.dasheng.erp.model.params.SkuValuesParam;
import cn.atsoft.dasheng.erp.model.result.SkuValuesResult;
import cn.atsoft.dasheng.erp.service.SkuValuesService;
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
 * sku详情表控制器
 *
 * @author 
 * @Date 2021-10-18 14:14:21
 */
@RestController
@RequestMapping("/skuValues")
@Api(tags = "sku详情表")
public class SkuValuesController extends BaseController {

    @Autowired
    private SkuValuesService skuValuesService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SkuValuesParam skuValuesParam) {
        this.skuValuesService.add(skuValuesParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SkuValuesParam skuValuesParam) {

        this.skuValuesService.update(skuValuesParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SkuValuesParam skuValuesParam)  {
        this.skuValuesService.delete(skuValuesParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody SkuValuesParam skuValuesParam) {
        SkuValues detail = this.skuValuesService.getById(skuValuesParam.getSkuDetailId());
        SkuValuesResult result = new SkuValuesResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SkuValuesResult> list(@RequestBody(required = false) SkuValuesParam skuValuesParam) {
        if(ToolUtil.isEmpty(skuValuesParam)){
            skuValuesParam = new SkuValuesParam();
        }
        return this.skuValuesService.findPageBySpec(skuValuesParam);
    }




}


