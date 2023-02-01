package cn.atsoft.dasheng.goods.classz.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.classz.entity.RestAttribute;
import cn.atsoft.dasheng.goods.classz.entity.RestAttributeValues;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeParam;
import cn.atsoft.dasheng.goods.classz.model.result.RestAttributeResult;
import cn.atsoft.dasheng.goods.classz.model.result.RestAttributeValuesResult;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeService;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeValuesService;
import cn.atsoft.dasheng.goods.classz.wrapper.RestAttributeSelectWrapper;
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
@RequestMapping("/itemAttribute/{version}")
@ApiVersion("2.0")
@Api(tags = "产品属性管理")
public class RestAttributeController extends BaseController {

    @Autowired
    private RestAttributeService restAttributeService;
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
    public ResponseData addItem(@RequestBody RestAttributeParam restAttributeParam) {
        this.restAttributeService.add(restAttributeParam);
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
    public ResponseData update(@RequestBody RestAttributeParam restAttributeParam) {

        this.restAttributeService.update(restAttributeParam);
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
    public ResponseData delete(@RequestBody RestAttributeParam restAttributeParam) {
        this.restAttributeService.delete(restAttributeParam);
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
    public ResponseData detail(@RequestBody RestAttributeParam restAttributeParam) {
        RestAttribute detail = this.restAttributeService.getById(restAttributeParam.getAttributeId());
        List<RestAttributeValuesResult> attributeValuesResults = new ArrayList<>();
        if (ToolUtil.isNotEmpty(detail)) {
            List<RestAttributeValues> attributeValues = restAttributeValuesService.lambdaQuery()
                    .in(RestAttributeValues::getAttributeId, detail.getAttributeId())
                    .list();
            if (ToolUtil.isNotEmpty(attributeValues)) {
                for (RestAttributeValues attributeValue : attributeValues) {
                    RestAttributeValuesResult attributeValuesResult = new RestAttributeValuesResult();
                    ToolUtil.copyProperties(attributeValue, attributeValuesResult);
                    attributeValuesResults.add(attributeValuesResult);
                }
            }
        }
        RestAttributeResult result = new RestAttributeResult();
        ToolUtil.copyProperties(detail, result);
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
    public PageInfo<RestAttributeResult> list(@RequestBody(required = false) RestAttributeParam restAttributeParam) {
        if (ToolUtil.isEmpty(restAttributeParam)) {
            restAttributeParam = new RestAttributeParam();
        }
        return this.restAttributeService.findPageBySpec(restAttributeParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.GET)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(Long itemId) {
        QueryWrapper<RestAttribute> itemAttributeQueryWrapper = new QueryWrapper<>();
        itemAttributeQueryWrapper.in("display", 1);
        if (ToolUtil.isNotEmpty(itemId)) {
            itemAttributeQueryWrapper.in("item_id", itemId);
        }
        List<Map<String, Object>> list = this.restAttributeService.listMaps(itemAttributeQueryWrapper);
        RestAttributeSelectWrapper itemAttributeSelectWrapper = new RestAttributeSelectWrapper(list);
        List<Map<String, Object>> result = itemAttributeSelectWrapper.wrap();
        return ResponseData.success(result);
    }
}


