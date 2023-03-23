package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPickCode;
import cn.atsoft.dasheng.production.model.params.ProductionPickCodeParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickCodeResult;
import cn.atsoft.dasheng.production.service.ProductionPickCodeService;
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
 * 领取物料码控制器
 *
 * @author cheng
 * @Date 2022-03-29 11:20:07
 */
@RestController
@RequestMapping("/productionPickCode")
@Api(tags = "领取物料码")
public class ProductionPickCodeController extends BaseController {

    @Autowired
    private ProductionPickCodeService productionPickCodeService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2022-03-29
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionPickCodeParam productionPickCodeParam) {
        this.productionPickCodeService.add(productionPickCodeParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2022-03-29
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductionPickCodeParam productionPickCodeParam) {

        this.productionPickCodeService.update(productionPickCodeParam);
        return ResponseData.success();
    }
    @RequestMapping(value = "/getCode", method = RequestMethod.GET)
    @ApiOperation("编辑")
    public ResponseData getCodeByLoginUser() {

        ProductionPickCode pickCode = this.productionPickCodeService.query().eq("create_user", LoginContextHolder.getContext().getUserId()).eq("display", 1).last("limit 1").one();
        if (ToolUtil.isNotEmpty(pickCode)){
            return ResponseData.success(pickCode.getCode());

        }
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2022-03-29
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductionPickCodeParam productionPickCodeParam)  {
        this.productionPickCodeService.delete(productionPickCodeParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2022-03-29
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ProductionPickCodeParam productionPickCodeParam) {
        ProductionPickCode detail = this.productionPickCodeService.getById(productionPickCodeParam.getPickCodeId());
        ProductionPickCodeResult result = new ProductionPickCodeResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2022-03-29
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionPickCodeResult> list(@RequestBody(required = false) ProductionPickCodeParam productionPickCodeParam) {
        if(ToolUtil.isEmpty(productionPickCodeParam)){
            productionPickCodeParam = new ProductionPickCodeParam();
        }
        return this.productionPickCodeService.findPageBySpec(productionPickCodeParam);
    }




}


