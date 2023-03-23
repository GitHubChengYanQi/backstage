package cn.atsoft.dasheng.production.card.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.production.card.entity.ProductionCard;
import cn.atsoft.dasheng.production.card.model.params.ProductionCardParam;
import cn.atsoft.dasheng.production.card.model.result.ProductionCardResult;
import cn.atsoft.dasheng.production.card.service.RestProductionCardService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 生产卡片控制器
 *
 * @author Captain_Jazz
 * @Date 2023-03-10 14:28:45
 */
@RestController
@RequestMapping("/{version}/productionCard")
@ApiVersion("2.0")
@Api(tags = "生产卡片")
public class RestProductionCardController extends BaseController {

    @Autowired
    private RestProductionCardService restProductionCardService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionCardParam productionCardParam) {
        this.restProductionCardService.add(productionCardParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductionCardParam productionCardParam) {

        this.restProductionCardService.update(productionCardParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductionCardParam productionCardParam)  {
        this.restProductionCardService.delete(productionCardParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ProductionCardResult> detail(@RequestBody ProductionCardParam productionCardParam) {
        ProductionCard detail = this.restProductionCardService.getById(productionCardParam.getProductionCardId());
        ProductionCardResult result = new ProductionCardResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionCardResult> list(@RequestBody(required = false) ProductionCardParam productionCardParam) {
        if(ToolUtil.isEmpty(productionCardParam)){
            productionCardParam = new ProductionCardParam();
        }
        return this.restProductionCardService.findPageBySpec(productionCardParam);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    @RequestMapping(value = "/viewOrderDetail", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionCardResult> viewOrderDetail(@RequestBody(required = false) ProductionCardParam productionCardParam) {
        if(ToolUtil.isEmpty(productionCardParam)){
            productionCardParam = new ProductionCardParam();
        }
        return this.restProductionCardService.findPageBySpec(productionCardParam);
    }




}


