package cn.atsoft.dasheng.db.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.db.entity.FieldConfig;
import cn.atsoft.dasheng.db.model.params.FieldConfigParam;
import cn.atsoft.dasheng.db.model.result.FieldConfigResult;
import cn.atsoft.dasheng.db.service.FieldConfigService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 字段配置控制器
 *
 * @author Sing
 * @Date 2020-12-11 16:52:57
 */
@RestController
@RequestMapping("/fieldConfig")
@Api(tags = "字段配置")
public class FieldConfigController extends BaseController {

    @Autowired
    private FieldConfigService fieldConfigService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2020-12-11
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody FieldConfigParam fieldConfigParam) {
        this.fieldConfigService.add(fieldConfigParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Sing
     * @Date 2020-12-11
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody FieldConfigParam fieldConfigParam) {
        this.fieldConfigService.update(fieldConfigParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Sing
     * @Date 2020-12-11
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody FieldConfigParam fieldConfigParam)  {
        this.fieldConfigService.delete(fieldConfigParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Sing
     * @Date 2020-12-11
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<FieldConfig> detail(@RequestBody FieldConfigParam fieldConfigParam) {
        FieldConfig detail = this.fieldConfigService.getById(fieldConfigParam.getFieldName());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author Sing
     * @Date 2020-12-11
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<FieldConfigResult> list(@RequestBody(required = false) FieldConfigParam fieldConfigParam) {
        return this.fieldConfigService.findPageBySpec(fieldConfigParam);
    }

}


