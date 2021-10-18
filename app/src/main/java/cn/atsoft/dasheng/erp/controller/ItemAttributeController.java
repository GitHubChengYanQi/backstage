package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import cn.atsoft.dasheng.erp.model.params.ItemAttributeParam;
import cn.atsoft.dasheng.erp.model.result.ItemAttributeResult;
import cn.atsoft.dasheng.erp.service.ItemAttributeService;
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
 * 产品属性表控制器
 *
 * @author song
 * @Date 2021-10-18 12:00:02
 */
@RestController
@RequestMapping("/itemAttribute")
@Api(tags = "产品属性表")
public class ItemAttributeController extends BaseController {

    @Autowired
    private ItemAttributeService itemAttributeService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ItemAttributeParam itemAttributeParam) {
        this.itemAttributeService.add(itemAttributeParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ItemAttributeParam itemAttributeParam) {

        this.itemAttributeService.update(itemAttributeParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ItemAttributeParam itemAttributeParam)  {
        this.itemAttributeService.delete(itemAttributeParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ItemAttributeResult> detail(@RequestBody ItemAttributeParam itemAttributeParam) {
        ItemAttribute detail = this.itemAttributeService.getById(itemAttributeParam.getAttributeId());
        ItemAttributeResult result = new ItemAttributeResult();
        ToolUtil.copyProperties(detail, result);

        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ItemAttributeResult> list(@RequestBody(required = false) ItemAttributeParam itemAttributeParam) {
        if(ToolUtil.isEmpty(itemAttributeParam)){
            itemAttributeParam = new ItemAttributeParam();
        }
        return this.itemAttributeService.findPageBySpec(itemAttributeParam);
    }




}


