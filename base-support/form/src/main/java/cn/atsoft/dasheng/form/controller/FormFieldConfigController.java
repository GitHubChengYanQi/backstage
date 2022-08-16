package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormFieldConfig;
import cn.atsoft.dasheng.form.model.params.FormFieldConfigParam;
import cn.atsoft.dasheng.form.model.result.FormFieldConfigResult;
import cn.atsoft.dasheng.form.service.FormFieldConfigService;
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
 * 自定义表单字段设置控制器
 *
 * @author Sing
 * @Date 2021-11-10 16:53:55
 */
@RestController
@RequestMapping("/formFieldConfig")
@Api(tags = "自定义表单字段设置")
public class FormFieldConfigController extends BaseController {

    @Autowired
    private FormFieldConfigService formFieldConfigService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody FormFieldConfigParam formFieldConfigParam) {
        this.formFieldConfigService.add(formFieldConfigParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody FormFieldConfigParam formFieldConfigParam) {

        this.formFieldConfigService.update(formFieldConfigParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody FormFieldConfigParam formFieldConfigParam)  {
        this.formFieldConfigService.delete(formFieldConfigParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody FormFieldConfigParam formFieldConfigParam) {
        FormFieldConfig detail = this.formFieldConfigService.getById(formFieldConfigParam.getFieldId());
        FormFieldConfigResult result = new FormFieldConfigResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<FormFieldConfigResult> list(@RequestBody(required = false) FormFieldConfigParam formFieldConfigParam) {
        if(ToolUtil.isEmpty(formFieldConfigParam)){
            formFieldConfigParam = new FormFieldConfigParam();
        }
        return this.formFieldConfigService.findPageBySpec(formFieldConfigParam);
    }




}


