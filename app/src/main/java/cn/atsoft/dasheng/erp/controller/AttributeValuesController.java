package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.params.AttributeValuesParam;
import cn.atsoft.dasheng.erp.model.result.AttributeValuesResult;
import cn.atsoft.dasheng.erp.service.AttributeValuesService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.wrapper.AttributeValuesSelectWrapper;
import cn.atsoft.dasheng.erp.wrapper.ItemAttributeSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 产品属性数据表控制器
 *
 * @author song
 * @Date 2021-10-18 12:00:02
 */
@RestController
@RequestMapping("/attributeValues")
@Api(tags = "产品属性数据表")
public class AttributeValuesController extends BaseController {

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
    public ResponseData addItem(@RequestBody AttributeValuesParam attributeValuesParam) {
        this.attributeValuesService.add(attributeValuesParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改属性值", key = "name", dict = AttributeValuesParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody AttributeValuesParam attributeValuesParam) {

        this.attributeValuesService.update(attributeValuesParam);
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
    @BussinessLog(value = "删除属性值", key = "name", dict = AttributeValuesParam.class)
    public ResponseData delete(@RequestBody AttributeValuesParam attributeValuesParam) {
        this.attributeValuesService.delete(attributeValuesParam);
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
    public ResponseData detail(@RequestBody AttributeValuesParam attributeValuesParam) {
        AttributeValues detail = this.attributeValuesService.getById(attributeValuesParam.getAttributeValuesId());
        AttributeValuesResult result = new AttributeValuesResult();
        ToolUtil.copyProperties(detail, result);


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
    public PageInfo<AttributeValuesResult> list(@RequestBody(required = false) AttributeValuesParam attributeValuesParam) {
        if (ToolUtil.isEmpty(attributeValuesParam)) {
            attributeValuesParam = new AttributeValuesParam();
        }
        return this.attributeValuesService.findPageBySpec(attributeValuesParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.GET)
    @ApiOperation("Select数据接口")
    @Permission
    public ResponseData listSelect(Long attributeId) {
        QueryWrapper<AttributeValues> attributeValuesQueryWrapper = new QueryWrapper<>();
        attributeValuesQueryWrapper.in("attribute_id", attributeId);
        attributeValuesQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.attributeValuesService.listMaps(attributeValuesQueryWrapper);
        AttributeValuesSelectWrapper attributeValuesSelectWrapper = new AttributeValuesSelectWrapper(list);
        List<Map<String, Object>> result = attributeValuesSelectWrapper.wrap();
        return ResponseData.success(result);
    }

}


