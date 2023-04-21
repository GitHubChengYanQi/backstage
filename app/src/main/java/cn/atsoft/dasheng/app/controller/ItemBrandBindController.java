package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ItemBrandBind;
import cn.atsoft.dasheng.app.model.params.ItemBrandBindParam;
import cn.atsoft.dasheng.app.model.result.ItemBrandBindResult;
import cn.atsoft.dasheng.app.service.ItemBrandBindService;
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
 * @Date 2021-09-24 09:09:27
 */
@RestController
@RequestMapping("/itemBrandBind")
@Api(tags = "商品品牌绑定表")
public class ItemBrandBindController extends BaseController {

    @Autowired
    private ItemBrandBindService itemBrandBindService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-09-24
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ItemBrandBindParam itemBrandBindParam) {
        this.itemBrandBindService.add(itemBrandBindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-09-24
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ItemBrandBindParam itemBrandBindParam) {

        this.itemBrandBindService.update(itemBrandBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-09-24
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ItemBrandBindParam itemBrandBindParam)  {
        this.itemBrandBindService.delete(itemBrandBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-09-24
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ItemBrandBindParam itemBrandBindParam) {
        ItemBrandBind detail = this.itemBrandBindService.getById(itemBrandBindParam.getItemBrandBindId());
        ItemBrandBindResult result = new ItemBrandBindResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-09-24
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) ItemBrandBindParam itemBrandBindParam) {
        if(ToolUtil.isEmpty(itemBrandBindParam)){
            itemBrandBindParam = new ItemBrandBindParam();
        }
//        return this.itemBrandBindService.findPageBySpec(itemBrandBindParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.itemBrandBindService.findPageBySpec(itemBrandBindParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return this.itemBrandBindService.findPageBySpec(itemBrandBindParam, dataScope);
        }
    }




}


