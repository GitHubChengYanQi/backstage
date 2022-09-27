package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormStyle;
import cn.atsoft.dasheng.form.model.params.FormStyleParam;
import cn.atsoft.dasheng.form.model.result.FormStyleResult;
import cn.atsoft.dasheng.form.service.FormStyleService;
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
 * 表单风格控制器
 *
 * @author 
 * @Date 2022-09-23 09:20:36
 */
@RestController
@RequestMapping("/formStyle")
@Api(tags = "表单风格")
public class FormStyleController extends BaseController {

    @Autowired
    private FormStyleService formStyleService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2022-09-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody FormStyleParam formStyleParam) {
        this.formStyleService.add(formStyleParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2022-09-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody FormStyleParam formStyleParam) {

        this.formStyleService.update(formStyleParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2022-09-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody FormStyleParam formStyleParam)  {
        this.formStyleService.delete(formStyleParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2022-09-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<FormStyleResult> detail(@RequestBody FormStyleParam formStyleParam) {
        FormStyle detail = this.formStyleService.getById(formStyleParam.getStyleId());
        FormStyleResult result = new FormStyleResult();
        ToolUtil.copyProperties(detail, result);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2022-09-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<FormStyleResult> list(@RequestBody(required = false) FormStyleParam formStyleParam) {
        if(ToolUtil.isEmpty(formStyleParam)){
            formStyleParam = new FormStyleParam();
        }
        return this.formStyleService.findPageBySpec(formStyleParam);
    }




}


