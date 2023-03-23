package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ItemData;
import cn.atsoft.dasheng.crm.model.params.ItemDataParam;
import cn.atsoft.dasheng.crm.model.result.ItemDataResult;
import cn.atsoft.dasheng.crm.service.ItemDataService;
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
 * 产品资料控制器
 *
 * @author song
 * @Date 2021-09-11 13:34:33
 */
@RestController
@RequestMapping("/itemData")
@Api(tags = "产品资料")
public class ItemDataController extends BaseController {

    @Autowired
    private ItemDataService itemDataService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ItemDataParam itemDataParam) {
        this.itemDataService.add(itemDataParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ItemDataParam itemDataParam) {

        this.itemDataService.update(itemDataParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ItemDataParam itemDataParam)  {
        this.itemDataService.delete(itemDataParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ItemDataParam itemDataParam) {
        ItemData detail = this.itemDataService.getById(itemDataParam.getItemsDataId());
        ItemDataResult result = new ItemDataResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ItemDataResult> list(@RequestBody(required = false) ItemDataParam itemDataParam) {
        if(ToolUtil.isEmpty(itemDataParam)){
            itemDataParam = new ItemDataParam();
        }
        return this.itemDataService.findPageBySpec(itemDataParam);
    }




}


