package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.model.result.PlaceResult;
import cn.atsoft.dasheng.app.wrapper.BrandSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.ItemsSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Items;
import cn.atsoft.dasheng.app.model.params.ItemsParam;
import cn.atsoft.dasheng.app.model.result.ItemsResult;
import cn.atsoft.dasheng.app.service.ItemsService;
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

    /**
     * 新增接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ItemsParam itemsParam) {
//        PartsResult partsResult = new PartsResult();
//        if(partsResult!=null){
//            if( partsResult.getItemId()==itemsParam.getItemId()){
//                partsResult.setItemName(itemsParam.getName());
//            }
//        }

        this.itemsService.add(itemsParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
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
    public ResponseData<ItemsResult> detail(@RequestBody ItemsParam itemsParam) {
        Items detail = this.itemsService.getById(itemsParam.getItemId());
        ItemsResult result = new ItemsResult();
        ToolUtil.copyProperties(detail, result);

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
    public PageInfo<ItemsResult> list(@RequestBody(required = false) ItemsParam itemsParam) {
        if (ToolUtil.isEmpty(itemsParam)) {
            itemsParam = new ItemsParam();
        }
        return this.itemsService.findPageBySpec(itemsParam);
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)

    public ResponseData<List<Map<String, Object>>> listSelect() {
        List<Map<String, Object>> list = this.itemsService.listMaps();
//        BrandSelectWrapper factory = new BrandSelectWrapper(list);
        ItemsSelectWrapper itemsSelectWrapper =new ItemsSelectWrapper(list);
        List<Map<String, Object>> result = itemsSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}


