package cn.atsoft.dasheng.goods.unit.controller;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.unit.entity.RestUnit;
import cn.atsoft.dasheng.goods.unit.model.params.RestUnitParam;
import cn.atsoft.dasheng.goods.unit.model.result.RestUnitResult;
import cn.atsoft.dasheng.goods.unit.service.RestUnitService;
import cn.atsoft.dasheng.goods.unit.wrapper.RestUnitSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 物料单位表控制器
 */
@RestController
@RequestMapping("/unit/{version}")
@ApiVersion("2.0")
@Api(tags = "物料单位管理")
public class RestUnitController extends BaseController {

    @Autowired
    private RestUnitService restUnitService;

    /**
     * 新增接口
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestUnitParam restUnitParam) {
        this.restUnitService.add(restUnitParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RestUnitParam restUnitParam) {

        this.restUnitService.update(restUnitParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RestUnitParam restUnitParam) {
        this.restUnitService.delete(restUnitParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody RestUnitParam restUnitParam) {
        RestUnit detail = this.restUnitService.getById(restUnitParam.getUnitId());
        RestUnitResult result = new RestUnitResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) RestUnitParam restUnitParam) {
        if (ToolUtil.isEmpty(restUnitParam)) {
            restUnitParam = new RestUnitParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.restUnitService.findPageBySpec(restUnitParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return this.restUnitService.findPageBySpec(restUnitParam, dataScope);
        }
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    public ResponseData listSelect() {
        List<Map<String, Object>> list = this.restUnitService.listMaps();
        RestUnitSelectWrapper unitSelectWrapper = new RestUnitSelectWrapper(list);
        List<Map<String, Object>> result = unitSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}


