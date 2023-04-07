package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ItemBrand;
import cn.atsoft.dasheng.app.model.params.ItemBrandParam;
import cn.atsoft.dasheng.app.model.result.ItemBrandResult;
import cn.atsoft.dasheng.app.service.ItemBrandService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
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
 * 商品品牌绑定表控制器
 *
 * @author
 * @Date 2021-09-23 12:01:01
 */
@RestController
@RequestMapping("/itemBrand")
@Api(tags = "商品品牌绑定表")
public class ItemBrandController extends BaseController {

    @Autowired
    private ItemBrandService itemBrandService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-09-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ItemBrandParam itemBrandParam) {
        this.itemBrandService.add(itemBrandParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-09-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ItemBrandParam itemBrandParam) {

        this.itemBrandService.update(itemBrandParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-09-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ItemBrandParam itemBrandParam) {
        this.itemBrandService.delete(itemBrandParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-09-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ItemBrandParam itemBrandParam) {
        ItemBrand detail = this.itemBrandService.getById(itemBrandParam.getItemId());
        ItemBrandResult result = new ItemBrandResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-09-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) ItemBrandParam itemBrandParam) {
        if (ToolUtil.isEmpty(itemBrandParam)) {
            itemBrandParam = new ItemBrandParam();
        }
//        return this.itemBrandService.findPageBySpec(itemBrandParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.itemBrandService.findPageBySpec(itemBrandParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return this.itemBrandService.findPageBySpec(itemBrandParam, dataScope);
        }
    }


}


