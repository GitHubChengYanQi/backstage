package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.model.params.TemplateParam;
import cn.atsoft.dasheng.app.model.result.TemplateResult;
import cn.atsoft.dasheng.app.service.TemplateService;
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
 * 合同模板控制器
 *
 * @author 
 * @Date 2021-07-21 08:22:02
 */
@RestController
@RequestMapping("/template")
@Api(tags = "合同模板")
public class TemplateController extends BaseController {

    @Autowired
    private TemplateService templateService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody TemplateParam templateParam) {
        this.templateService.add(templateParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody TemplateParam templateParam) {

        this.templateService.update(templateParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody TemplateParam templateParam)  {
        this.templateService.delete(templateParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<TemplateResult> detail(@RequestBody TemplateParam templateParam) {
        Template detail = this.templateService.getById(templateParam.getTemplateId());
        TemplateResult result = new TemplateResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<TemplateResult> list(@RequestBody(required = false) TemplateParam templateParam) {
        if(ToolUtil.isEmpty(templateParam)){
            templateParam = new TemplateParam();
        }
        return this.templateService.findPageBySpec(templateParam);
    }




}


