package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormDataValue;
import cn.atsoft.dasheng.form.model.params.FormDataValueParam;
import cn.atsoft.dasheng.form.model.result.FormDataValueResult;
import cn.atsoft.dasheng.form.service.FormDataValueService;
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
 * 自定义表单各个字段数据控制器
 *
 * @author Sing
 * @Date 2021-11-10 16:53:55
 */
@RestController
@RequestMapping("/formDataValue")
@Api(tags = "自定义表单各个字段数据")
public class FormDataValueController extends BaseController {

    @Autowired
    private FormDataValueService formDataValueService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody FormDataValueParam formDataValueParam) {
        this.formDataValueService.add(formDataValueParam);
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
    public ResponseData update(@RequestBody FormDataValueParam formDataValueParam) {

        this.formDataValueService.update(formDataValueParam);
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
    public ResponseData delete(@RequestBody FormDataValueParam formDataValueParam)  {
        this.formDataValueService.delete(formDataValueParam);
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
    public ResponseData detail(@RequestBody FormDataValueParam formDataValueParam) {
        FormDataValue detail = this.formDataValueService.getById(formDataValueParam.getValueId());
        FormDataValueResult result = new FormDataValueResult();
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
    public PageInfo list(@RequestBody(required = false) FormDataValueParam formDataValueParam) {
        if(ToolUtil.isEmpty(formDataValueParam)){
            formDataValueParam = new FormDataValueParam();
        }
        return this.formDataValueService.findPageBySpec(formDataValueParam);
    }




}


