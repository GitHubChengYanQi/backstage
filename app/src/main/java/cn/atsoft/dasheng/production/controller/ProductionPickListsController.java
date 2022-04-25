package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StorehousePositionsBind;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.StorehousePositionsBindService;
import cn.atsoft.dasheng.production.entity.ProductionPickLists;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
import cn.atsoft.dasheng.production.entity.ProductionTask;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsCartResult;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsDetailResult;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsResult;
import cn.atsoft.dasheng.production.model.result.ProductionTaskResult;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.production.service.ProductionPickListsDetailService;
import cn.atsoft.dasheng.production.service.ProductionPickListsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.production.service.ProductionTaskService;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 领料单控制器
 *
 * @author Captain_Jazz
 * @Date 2022-03-25 16:18:02
 */
@RestController
@RequestMapping("/productionPickLists")
@Api(tags = "领料单")
public class ProductionPickListsController extends BaseController {

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

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionPickListsParam productionPickListsParam) {
        this.productionPickListsService.add(productionPickListsParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProductionPickListsParam productionPickListsParam) {

        this.productionPickListsService.update(productionPickListsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProductionPickListsParam productionPickListsParam) {
        this.productionPickListsService.delete(productionPickListsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ProductionPickListsResult> detail(@RequestBody ProductionPickListsParam productionPickListsParam) {
        ProductionPickLists detail = new ProductionPickLists();
        ProductionPickListsResult result = new ProductionPickListsResult();
        if (ToolUtil.isNotEmpty(productionPickListsParam.getPickListsId())) {
            detail = this.productionPickListsService.getById(productionPickListsParam.getPickListsId());

            ToolUtil.copyProperties(detail, result);
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

    @RequestMapping(value = "/mergeDetail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData merge(@RequestBody ProductionPickListsParam productionPickListsParam) {

        List<ProductionPickLists> productionPickLists = productionPickListsService.listByIds(productionPickListsParam.getPickListsIds());
        List<Long> sourceIds = new ArrayList<>();
        for (ProductionPickLists productionPickList : productionPickLists) {
            sourceIds.add(productionPickList.getSourceId());
        }
        /**
         * 返回任务信息
         */
        List<ProductionTask> productionTasks = sourceIds.size() == 0 ? new ArrayList<>() : productionTaskService.listByIds(sourceIds);
        List<ProductionTaskResult> productionTaskResults = new ArrayList<>();
        for (ProductionTask productionTask : productionTasks) {
            ProductionTaskResult productionTaskResult = new ProductionTaskResult();
            ToolUtil.copyProperties(productionTask, productionTaskResult);
            productionTaskResults.add(productionTaskResult);
        }
        /**
         * 查询备料信息
         */
        List<ProductionPickListsCart> productionPickListsCarts = productionPickListsCartService.query().in("pick_lists_id", productionPickListsParam.getPickListsIds()).list();
        List<ProductionPickListsCartResult> pickListsCartResults = new ArrayList<>();
        for (ProductionPickListsCart productionPickListsCart : productionPickListsCarts) {
            ProductionPickListsCartResult productionPickListsCartResult = new ProductionPickListsCartResult();
            ToolUtil.copyProperties(productionPickListsCart, productionPickListsCartResult);
            pickListsCartResults.add(productionPickListsCartResult);
        }
        productionPickListsCartService.format(pickListsCartResults);

        List<ProductionPickListsResult> results = new ArrayList<>();
        for (ProductionPickLists productionPickList : productionPickLists) {
            ProductionPickListsResult result = new ProductionPickListsResult();
            ToolUtil.copyProperties(productionPickList, result);
            results.add(result);
        }
        productionPickListsService.format(results);
        ProductionPickListsResult result = new ProductionPickListsResult();
        List<ProductionPickListsDetailResult> detailResults = new ArrayList<>();
        for (ProductionPickListsResult productionPickListsResult : results) {
            if (ToolUtil.isNotEmpty(productionPickListsResult.getDetailResults())) {
                detailResults.addAll(productionPickListsResult.getDetailResults());
            }
        }
        result.setDetailResults(detailResults);

        List<Long> skuIds = new ArrayList<>();
        QueryWrapper<ProductionPickListsDetail> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.in("pick_lists_id", productionPickListsParam.getPickListsIds());
        detailQueryWrapper.eq("status", 0);
        List<ProductionPickListsDetail> detailList = pickListsDetailService.list(detailQueryWrapper);
        for (ProductionPickListsDetail productionPickListsDetail : detailList) {
            skuIds.add(productionPickListsDetail.getSkuId());
        }

        List<StorehousePositionsResult> storehousePositionsResults = skuIds.size() == 0 ? new ArrayList<>() : storehousePositionsBindService.treeView(skuIds);
        result.setStorehousePositionsResults(storehousePositionsResults);
        result.setProductionTaskResults(productionTaskResults);
        result.setCartResults(pickListsCartResults);
//         JSON.toJSONString(result);
        String toJSONString =JSON.toJSONString(result,JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask());




        byte[] gzip = ZipUtil.gzip(toJSONString, CharsetUtil.UTF_8);

        String base64 = Base64.encode(gzip);

        return ResponseData.success(base64);


    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionPickListsResult> list(@RequestBody(required = false) ProductionPickListsParam productionPickListsParam) {
        if (ToolUtil.isEmpty(productionPickListsParam)) {
            productionPickListsParam = new ProductionPickListsParam();
        }
        return this.productionPickListsService.findPageBySpec(productionPickListsParam);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/sendPersonPick", method = RequestMethod.POST)
    @ApiOperation("通知领料")
    public ResponseData sendPersonPick(@RequestBody(required = false) ProductionPickListsParam productionPickListsParam) {
        this.productionPickListsService.sendPersonPick(productionPickListsParam);
        return ResponseData.success();
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/selfList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProductionPickListsResult> selfList(@RequestBody(required = false) ProductionPickListsParam productionPickListsParam) {
        if (ToolUtil.isEmpty(productionPickListsParam)) {
            productionPickListsParam = new ProductionPickListsParam();
        }
        productionPickListsParam.setUserId(LoginContextHolder.getContext().getUserId());
        return this.productionPickListsService.findPageBySpec(productionPickListsParam);
    }

    @RequestMapping(value = "/createOutStockOrder", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData createOutStockOrder(@RequestBody(required = false) ProductionPickListsParam productionPickListsParam) {
        if (ToolUtil.isEmpty(productionPickListsParam)) {
            productionPickListsParam = new ProductionPickListsParam();
        }

        this.productionPickListsService.outStock(productionPickListsParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/getByTask", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ProductionPickListsResult> getByTask(@RequestBody ProductionPickListsParam productionPickListsParam) {


        ProductionPickLists detail = this.productionPickListsService.query().eq("source", "productionTask").eq("source_id", productionPickListsParam.getSourceId()).one();
        ProductionPickListsResult result = new ProductionPickListsResult();
        if (ToolUtil.isEmpty(detail)) {
            return ResponseData.success(result);
        }else {
            ToolUtil.copyProperties(detail, result);
            productionPickListsService.formatStatus99(new ArrayList<ProductionPickListsResult>() {{
                add(result);
            }});
        }
        return ResponseData.success(result);
    }


}

