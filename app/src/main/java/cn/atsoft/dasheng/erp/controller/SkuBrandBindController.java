package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuBrandBind;
import cn.atsoft.dasheng.erp.model.params.SkuBrandBindParam;
import cn.atsoft.dasheng.erp.model.result.SkuBrandBindResult;
import cn.atsoft.dasheng.erp.service.SkuBrandBindService;
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
 * 控制器
 *
 * @author Captain_Jazz
 * @Date 2022-01-18 10:55:42
 */
@RestController
@RequestMapping("/skuBrandBind")
@Api(tags = "")
public class SkuBrandBindController extends BaseController {

    @Autowired
    private SkuBrandBindService skuBrandBindService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SkuBrandBindParam skuBrandBindParam) {
        this.skuBrandBindService.add(skuBrandBindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SkuBrandBindParam skuBrandBindParam) {

        this.skuBrandBindService.update(skuBrandBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SkuBrandBindParam skuBrandBindParam)  {
        this.skuBrandBindService.delete(skuBrandBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody SkuBrandBindParam skuBrandBindParam) {
        SkuBrandBind detail = this.skuBrandBindService.getById(skuBrandBindParam.getSkuBrandBind());
        SkuBrandBindResult result = new SkuBrandBindResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-01-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SkuBrandBindResult> list(@RequestBody(required = false) SkuBrandBindParam skuBrandBindParam) {
        if(ToolUtil.isEmpty(skuBrandBindParam)){
            skuBrandBindParam = new SkuBrandBindParam();
        }
        return this.skuBrandBindService.findPageBySpec(skuBrandBindParam);
    }




}


