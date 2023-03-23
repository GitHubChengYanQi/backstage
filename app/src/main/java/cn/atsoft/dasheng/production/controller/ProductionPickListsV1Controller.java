package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.InventoryService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsBindService;
import cn.atsoft.dasheng.form.service.ActivitiProcessTaskService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsDetailResult;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsResult;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.production.service.ProductionPickListsDetailService;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.production.service.ProductionTaskService;
import cn.atsoft.dasheng.sendTemplate.RedisSendCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/productionPickLists/{v}")
@Api(tags = "领料单")
public class ProductionPickListsV1Controller {


    @Autowired
    private ProductionPickListsService productionPickListsService;
    @Autowired
    private ProductionPickListsDetailService pickListsDetailService;
    @Autowired
    private ProductionPickListsCartService productionPickListsCartService;

    @Autowired
    private StorehousePositionsBindService storehousePositionsBindService;

    @Autowired
    private ProductionTaskService productionTaskService;
    @Autowired
    private RedisSendCheck redisSendCheck;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ActivitiProcessTaskService processTaskService;

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @ApiVersion("1.1")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ProductionPickListsParam productionPickListsParam) {
        ProductionPickLists detail = new ProductionPickLists();
        ProductionPickListsResult result = new ProductionPickListsResult();
        if (ToolUtil.isNotEmpty(productionPickListsParam.getPickListsId())) {
            detail = this.productionPickListsService.getById(productionPickListsParam.getPickListsId());

            if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        }
        productionPickListsService.format(new ArrayList<ProductionPickListsResult>() {{
            add(result);
        }});
        List<Long> skuIds = new ArrayList<>();
        for (ProductionPickListsDetailResult detailResult : result.getDetailResults()) {
            skuIds.add(detailResult.getSkuId());
        }

        List<StorehousePositionsResult> storehousePositionsResults = skuIds.size() == 0 ? new ArrayList<>() : storehousePositionsBindService.treeView(skuIds);
        result.setStorehousePositionsResults(storehousePositionsResults);
        return ResponseData.success(result);
    }

}
