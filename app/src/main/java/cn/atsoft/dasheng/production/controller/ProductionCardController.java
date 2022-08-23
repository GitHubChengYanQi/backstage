package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionCard;
import cn.atsoft.dasheng.production.model.params.ProductionCardParam;
import cn.atsoft.dasheng.production.model.result.ProductionCardResult;
import cn.atsoft.dasheng.production.service.ProductionCardService;
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
 * 生产卡片控制器
 *
 * @author 
 * @Date 2022-02-28 13:51:24
 */
@RestController
@RequestMapping("/productionCard")
@Api(tags = "生产卡片")
public class ProductionCardController extends BaseController {

    @Autowired
    private ProductionCardService productionCardService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2022-02-28
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionCardParam productionCardParam) {
        this.productionCardService.add(productionCardParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2022-02-28
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductionCardParam productionCardParam) {

        this.productionCardService.update(productionCardParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2022-02-28
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductionCardParam productionCardParam)  {
        this.productionCardService.delete(productionCardParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2022-02-28
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ProductionCardParam productionCardParam) {
        ProductionCard detail = this.productionCardService.getById(productionCardParam.getProductionCardId());
        ProductionCardResult result = new ProductionCardResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2022-02-28
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionCardResult> list(@RequestBody(required = false) ProductionCardParam productionCardParam) {
        if(ToolUtil.isEmpty(productionCardParam)){
            productionCardParam = new ProductionCardParam();
        }
        return this.productionCardService.findPageBySpec(productionCardParam);
    }




}


