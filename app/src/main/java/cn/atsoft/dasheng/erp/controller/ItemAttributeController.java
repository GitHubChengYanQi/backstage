package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.wrapper.AdressSelectWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.params.ItemAttributeParam;
import cn.atsoft.dasheng.erp.model.result.AttributeValuesResult;
import cn.atsoft.dasheng.erp.model.result.ItemAttributeResult;
import cn.atsoft.dasheng.erp.service.AttributeValuesService;
import cn.atsoft.dasheng.erp.service.ItemAttributeService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.wrapper.ItemAttributeSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    @Autowired
    private AttributeValuesService attributeValuesService;

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
    @BussinessLog(value = "修改属性", key = "name", dict = ItemAttributeParam.class)
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
    @BussinessLog(value = "删除属性", key = "name", dict = ItemAttributeParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ItemAttributeParam itemAttributeParam) {
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
    public ResponseData detail(@RequestBody ItemAttributeParam itemAttributeParam) {
        ItemAttribute detail = this.itemAttributeService.getById(itemAttributeParam.getAttributeId());
        List<AttributeValuesResult> attributeValuesResults = new ArrayList<>();
        if (ToolUtil.isNotEmpty(detail)) {
            List<AttributeValues> attributeValues = attributeValuesService.lambdaQuery()
                    .in(AttributeValues::getAttributeId, detail.getAttributeId())
                    .list();
            if (ToolUtil.isNotEmpty(attributeValues)) {
                for (AttributeValues attributeValue : attributeValues) {
                    AttributeValuesResult attributeValuesResult = new AttributeValuesResult();
                    ToolUtil.copyProperties(attributeValue, attributeValuesResult);
                    attributeValuesResults.add(attributeValuesResult);
                }
            }
        }
        ItemAttributeResult result = new ItemAttributeResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        result.setAttributeValuesResults(attributeValuesResults);

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
        if (ToolUtil.isEmpty(itemAttributeParam)) {
            itemAttributeParam = new ItemAttributeParam();
        }
        return this.itemAttributeService.findPageBySpec(itemAttributeParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.GET)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(Long itemId) {
        QueryWrapper<ItemAttribute> itemAttributeQueryWrapper = new QueryWrapper<>();
        itemAttributeQueryWrapper.in("display", 1);
        if (ToolUtil.isNotEmpty(itemId)) {
            itemAttributeQueryWrapper.in("item_id", itemId);
        }
        List<Map<String, Object>> list = this.itemAttributeService.listMaps(itemAttributeQueryWrapper);
        ItemAttributeSelectWrapper itemAttributeSelectWrapper = new ItemAttributeSelectWrapper(list);
        List<Map<String, Object>> result = itemAttributeSelectWrapper.wrap();
        return ResponseData.success(result);
    }
}


