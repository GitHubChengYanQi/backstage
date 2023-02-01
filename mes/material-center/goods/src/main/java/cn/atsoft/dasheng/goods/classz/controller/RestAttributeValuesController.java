package cn.atsoft.dasheng.goods.classz.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.classz.entity.RestAttributeValues;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeValuesParam;
import cn.atsoft.dasheng.goods.classz.model.result.RestAttributeValuesResult;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeValuesService;
import cn.atsoft.dasheng.goods.classz.wrapper.RestAttributeValuesSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;


/**
 * 产品属性数据表控制器
 *
 * @author song
 * @Date 2021-10-18 12:00:02
 */
@RestController
@RequestMapping("/attributeValues/{version}")
@ApiVersion("2.0")
@Api(tags = "产品属性数据管理")
public class RestAttributeValuesController extends BaseController {

    @Autowired
    private RestAttributeValuesService restAttributeValuesService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestAttributeValuesParam restAttributeValuesParam) {
        this.restAttributeValuesService.add(restAttributeValuesParam);
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
    public ResponseData update(@RequestBody RestAttributeValuesParam restAttributeValuesParam) {

        this.restAttributeValuesService.update(restAttributeValuesParam);
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
    public ResponseData delete(@RequestBody RestAttributeValuesParam restAttributeValuesParam) {
        this.restAttributeValuesService.delete(restAttributeValuesParam);
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
    public ResponseData detail(@RequestBody RestAttributeValuesParam restAttributeValuesParam) {
        RestAttributeValues detail = this.restAttributeValuesService.getById(restAttributeValuesParam.getAttributeValuesId());
        RestAttributeValuesResult result = new RestAttributeValuesResult();
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
    public PageInfo<RestAttributeValuesResult> list(@RequestBody(required = false) RestAttributeValuesParam restAttributeValuesParam) {
        if (ToolUtil.isEmpty(restAttributeValuesParam)) {
            restAttributeValuesParam = new RestAttributeValuesParam();
        }
        return this.restAttributeValuesService.findPageBySpec(restAttributeValuesParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.GET)
    @ApiOperation("Select数据接口")
    @Permission
    public ResponseData listSelect(Long attributeId) {
        QueryWrapper<RestAttributeValues> attributeValuesQueryWrapper = new QueryWrapper<>();
        attributeValuesQueryWrapper.in("attribute_id", attributeId);
        attributeValuesQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.restAttributeValuesService.listMaps(attributeValuesQueryWrapper);
        RestAttributeValuesSelectWrapper attributeValuesSelectWrapper = new RestAttributeValuesSelectWrapper(list);
        List<Map<String, Object>> result = attributeValuesSelectWrapper.wrap();
        return ResponseData.success(result);
    }

}


