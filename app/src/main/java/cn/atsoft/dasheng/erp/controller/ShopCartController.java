package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ShopCart;
import cn.atsoft.dasheng.erp.model.params.ShopCartParam;
import cn.atsoft.dasheng.erp.model.result.ShopCartResult;
import cn.atsoft.dasheng.erp.service.ShopCartService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 购物车控制器
 *
 * @author song
 * @Date 2022-06-06 10:19:11
 */
@RestController
@RequestMapping("/shopCart")
@Api(tags = "购物车")
public class ShopCartController extends BaseController {

    @Autowired
    private ShopCartService shopCartService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-06-06
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem( @RequestBody ShopCartParam shopCartParam) {
        Long id = this.shopCartService.add(shopCartParam);
        return ResponseData.success(id);
    }


    @RequestMapping(value = "/addList", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addList(@RequestBody ShopCartParam shopCartParam) {
        this.shopCartService.addList(shopCartParam.getShopCartParams());
        return ResponseData.success();
    }


    @RequestMapping(value = "/sendBack", method = RequestMethod.POST)
    @ApiOperation("退回")
    public ResponseData sendBack(@RequestBody ShopCartParam shopCartParam) {
        this.shopCartService.sendBack(shopCartParam.getIds());
        return ResponseData.success();
    }

    @RequestMapping(value = "/backType", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData backType(@RequestBody ShopCartParam shopCartParam) {
        Set<String> set = this.shopCartService.backType(shopCartParam.getTypes());
        return ResponseData.success(set);
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-06-06
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ShopCartParam shopCartParam) {
        Long id = this.shopCartService.update(shopCartParam);
        return ResponseData.success(id);
    }


    /**
     * 查询所有
     *
     * @author song
     * @Date 2022-06-06
     */
    @RequestMapping(value = "/allList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData allList(@RequestBody(required = false) ShopCartParam shopCartParam) {
        if (ToolUtil.isEmpty(shopCartParam)) {
            shopCartParam = new ShopCartParam();
        }
        List<ShopCartResult> shopCartResults = this.shopCartService.allList(shopCartParam);
        return ResponseData.success(shopCartResults);
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-06-06
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ShopCartParam shopCartParam) {
        List<Long> ids = this.shopCartService.delete(shopCartParam);
        return ResponseData.success(ids);
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-06-06
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ShopCartResult> detail(@RequestBody ShopCartParam shopCartParam) {
        ShopCart detail = this.shopCartService.getById(shopCartParam.getCartId());
        ShopCartResult result = new ShopCartResult();
        ToolUtil.copyProperties(detail, result);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-06-06
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ShopCartResult> list(@RequestBody(required = false) ShopCartParam shopCartParam) {
        if (ToolUtil.isEmpty(shopCartParam)) {
            shopCartParam = new ShopCartParam();
        }
        return this.shopCartService.findPageBySpec(shopCartParam);
    }


}


