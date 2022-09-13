package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.FormConfig;
import cn.atsoft.dasheng.form.model.params.FormConfigParam;
import cn.atsoft.dasheng.form.model.result.FormConfigResult;
import cn.atsoft.dasheng.form.service.FormConfigService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



/**
 * 自定义表单控制器
 *
 * @author Sing
 * @Date 2021-11-10 16:53:55
 */
@RestController
@RequestMapping("/formConfig")
@Api(tags = "自定义表单")
public class FormConfigController extends BaseController {

    @Autowired
    private FormConfigService formConfigService;

    /**
     * 新增接口
     *
     * @author Sing
     * @Date 2021-11-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody FormConfigParam formConfigParam) {
        this.formConfigService.add(formConfigParam);
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
    public ResponseData update(@RequestBody FormConfigParam formConfigParam) {

        this.formConfigService.update(formConfigParam);
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
    public ResponseData delete(@RequestBody FormConfigParam formConfigParam)  {
        this.formConfigService.delete(formConfigParam);
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
    public ResponseData detail(@RequestBody FormConfigParam formConfigParam) {
        FormConfig detail = this.formConfigService.getById(formConfigParam.getFormId());
        FormConfigResult result = new FormConfigResult();
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
    public PageInfo list(@RequestBody(required = false) FormConfigParam formConfigParam) {
        if(ToolUtil.isEmpty(formConfigParam)){
            formConfigParam = new FormConfigParam();
        }
        
        return this.formConfigService.findPageBySpec(formConfigParam);
    }
}


