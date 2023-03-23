package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.ItemsRequest;
import cn.atsoft.dasheng.app.wrapper.ItemsSelectWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.params.ItemsParam;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.ItemsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 物品表控制器
 *
 * @author 1
 * @Date 2021-07-14 13:57:54
 */
@RestController
@RequestMapping("/items")
@Api(tags = "物品表")
public class ItemsController extends BaseController {

    @Autowired
    private ItemsService itemsService;
    public static Map map = new HashMap();

    /**
     * 新增接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    @Permission
    public ResponseData addItem(@RequestBody ItemsParam itemsParam) {

        Long add = this.itemsService.add(itemsParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
    public ResponseData update(@RequestBody ItemsParam itemsParam) {

        this.itemsService.update(itemsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    @Permission
    public ResponseData delete(@RequestBody ItemsParam itemsParam) {
        this.itemsService.delete(itemsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    @Permission
    public ResponseData detail(@RequestBody ItemsParam itemsParam) {
        Items detail = this.itemsService.getById(itemsParam.getItemId());
        ItemsResult result = new ItemsResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        this.itemsService.formatResult(result);
//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo list(@RequestBody(required = false) ItemsParam itemsParam) {
        if (ToolUtil.isEmpty(itemsParam)) {
            itemsParam = new ItemsParam();
        }
//        return this.itemsService.findPageBySpec(itemsParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.itemsService.findPageBySpec(itemsParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.itemsService.findPageBySpec(itemsParam, dataScope);
        }
    }

//    @Permission
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    public ResponseData listSelect() {
        QueryWrapper<Items> itemsQueryWrapper = new QueryWrapper<>();
        itemsQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.itemsService.listMaps(itemsQueryWrapper);
        ItemsSelectWrapper itemsSelectWrapper = new ItemsSelectWrapper(list);
        List<Map<String, Object>> result = itemsSelectWrapper.wrap();
        return ResponseData.success(result);
    }

    @Permission
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    public ResponseData batchDelete(@RequestBody ItemsRequest itemsRequest) {
        itemsService.batchDelete(itemsRequest.getItemId());
        return ResponseData.success();
    }
}


