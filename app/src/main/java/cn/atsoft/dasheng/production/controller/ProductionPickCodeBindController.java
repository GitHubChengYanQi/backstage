package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPickCodeBind;
import cn.atsoft.dasheng.production.model.params.ProductionPickCodeBindParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickCodeBindResult;
import cn.atsoft.dasheng.production.service.ProductionPickCodeBindService;
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
 * @Date 2022-03-29 16:43:50
 */
@RestController
@RequestMapping("/productionPickCodeBind")
@Api(tags = "")
public class ProductionPickCodeBindController extends BaseController {

    @Autowired
    private ProductionPickCodeBindService productionPickCodeBindService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionPickCodeBindParam productionPickCodeBindParam) {
        this.productionPickCodeBindService.add(productionPickCodeBindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductionPickCodeBindParam productionPickCodeBindParam) {

        this.productionPickCodeBindService.update(productionPickCodeBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductionPickCodeBindParam productionPickCodeBindParam)  {
        this.productionPickCodeBindService.delete(productionPickCodeBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ProductionPickCodeBindParam productionPickCodeBindParam) {
        ProductionPickCodeBind detail = this.productionPickCodeBindService.getById(productionPickCodeBindParam.getPickCodeBindId());
        ProductionPickCodeBindResult result = new ProductionPickCodeBindResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-29
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionPickCodeBindResult> list(@RequestBody(required = false) ProductionPickCodeBindParam productionPickCodeBindParam) {
        if(ToolUtil.isEmpty(productionPickCodeBindParam)){
            productionPickCodeBindParam = new ProductionPickCodeBindParam();
        }
        return this.productionPickCodeBindService.findPageBySpec(productionPickCodeBindParam);
    }




}


