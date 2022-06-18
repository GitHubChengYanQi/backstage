package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPickListsCart;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsCartParam;
import cn.atsoft.dasheng.production.model.request.CartGroupByUserListRequest;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsCartResult;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
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
 * 领料单详情表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-03-25 16:18:02
 */
@RestController
    @RequestMapping("/productionPickListsCart")
@Api(tags = "领料单详情表")
public class ProductionPickListsCartController extends BaseController {

    @Autowired
    private ProductionPickListsCartService productionPickListsCartService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProductionPickListsCartParam productionPickListsCartParam) {
        this.productionPickListsCartService.add(productionPickListsCartParam);
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
    public ResponseData update(@RequestBody ProductionPickListsCartParam productionPickListsCartParam) {

        this.productionPickListsCartService.update(productionPickListsCartParam);
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
    public ResponseData delete(@RequestBody ProductionPickListsCartParam productionPickListsCartParam)  {
        this.productionPickListsCartService.delete(productionPickListsCartParam);
        return ResponseData.success();
    }



    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData deleteBatch(@RequestBody ProductionPickListsCartParam productionPickListsCartParam)  {
        if (ToolUtil.isEmpty(productionPickListsCartParam)) {
            productionPickListsCartParam = new ProductionPickListsCartParam();
        }
        if (productionPickListsCartParam.getProductionPickListsCartParams().size()!= 0){
            this.productionPickListsCartService.deleteBatchByIds(productionPickListsCartParam.getProductionPickListsCartParams());
        }
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
    public ResponseData<ProductionPickListsCartResult> detail(@RequestBody ProductionPickListsCartParam productionPickListsCartParam) {
        ProductionPickListsCart detail = this.productionPickListsCartService.getById(productionPickListsCartParam.getPickListsCart());
        ProductionPickListsCartResult result = new ProductionPickListsCartResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public List<ProductionPickListsCartResult> list(@RequestBody(required = false) ProductionPickListsCartParam productionPickListsCartParam) {
        if(ToolUtil.isEmpty(productionPickListsCartParam)){
            productionPickListsCartParam = new ProductionPickListsCartParam();
        }
        return this.productionPickListsCartService.findListBySpec(productionPickListsCartParam);
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/groupByUserList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public List<CartGroupByUserListRequest> groupByUserList(@RequestBody(required = false) ProductionPickListsCartParam productionPickListsCartParam) {
        if(ToolUtil.isEmpty(productionPickListsCartParam)){
            productionPickListsCartParam = new ProductionPickListsCartParam();
        }
        return this.productionPickListsCartService.groupByUser(productionPickListsCartParam);
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/getSelfCarts", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData getSelfList(@RequestBody(required = false) ProductionPickListsCartParam productionPickListsCartParam) {
        if(ToolUtil.isEmpty(productionPickListsCartParam)){
            productionPickListsCartParam = new ProductionPickListsCartParam();
        }
        return ResponseData.success(this.productionPickListsCartService.getSelfCarts(productionPickListsCartParam));
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/getSelfCartsByLists", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData getSelfCartsByLists(@RequestBody(required = false) ProductionPickListsCartParam productionPickListsCartParam) {
        if(ToolUtil.isEmpty(productionPickListsCartParam)){
            productionPickListsCartParam = new ProductionPickListsCartParam();
        }
        return ResponseData.success(this.productionPickListsCartService.getSelfCartsByLists(productionPickListsCartParam));
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-03-25
     */
    @RequestMapping(value = "/getSelfCartsBySku", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData getSelfCartsBySku(@RequestBody(required = false) ProductionPickListsCartParam productionPickListsCartParam) {
        if(ToolUtil.isEmpty(productionPickListsCartParam)){
            productionPickListsCartParam = new ProductionPickListsCartParam();
        }
        return ResponseData.success(this.productionPickListsCartService.getSelfCartsBySku(productionPickListsCartParam));
    }



}


