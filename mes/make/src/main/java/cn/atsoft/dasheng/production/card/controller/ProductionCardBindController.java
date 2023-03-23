package cn.atsoft.dasheng.production.card.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.card.entity.ProductionCardBind;
import cn.atsoft.dasheng.production.card.model.params.ProductionCardBindParam;
import cn.atsoft.dasheng.production.card.model.result.ProductionCardBindResult;
import cn.atsoft.dasheng.production.card.service.RestProductionCardBindService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 生产卡片绑定控制器
 *
 * @author Captain_Jazz
 * @Date 2023-03-10 14:28:45
 */
@RestController
@RequestMapping("/productionCardBind")
@Api(tags = "生产卡片绑定")
public class ProductionCardBindController extends BaseController {

    @Autowired
    private RestProductionCardBindService restProductionCardBindService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-03-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionCardBindParam productionCardBindParam) {
        this.restProductionCardBindService.add(productionCardBindParam);
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
    public ResponseData update(@RequestBody ProductionCardBindParam productionCardBindParam) {

        this.restProductionCardBindService.update(productionCardBindParam);
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
    public ResponseData delete(@RequestBody ProductionCardBindParam productionCardBindParam)  {
        this.restProductionCardBindService.delete(productionCardBindParam);
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
    public ResponseData<ProductionCardBindResult> detail(@RequestBody ProductionCardBindParam productionCardBindParam) {
        ProductionCardBind detail = this.restProductionCardBindService.getById(productionCardBindParam.getProductionCardBindId());
        ProductionCardBindResult result = new ProductionCardBindResult();
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
    public PageInfo<ProductionCardBindResult> list(@RequestBody(required = false) ProductionCardBindParam productionCardBindParam) {
        if(ToolUtil.isEmpty(productionCardBindParam)){
            productionCardBindParam = new ProductionCardBindParam();
        }
        return this.restProductionCardBindService.findPageBySpec(productionCardBindParam);
    }




}


