package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.BatchDeleteRequest;
import cn.atsoft.dasheng.app.wrapper.TemplateSelectWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.model.params.TemplateParam;
import cn.atsoft.dasheng.app.model.result.TemplateResult;
import cn.atsoft.dasheng.app.service.TemplateService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
        Long add = this.templateService.add(templateParam);
        return ResponseData.success(add);
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
    public ResponseData delete(@RequestBody TemplateParam templateParam) {
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
        if (ToolUtil.isEmpty(templateParam)) {
            templateParam = new TemplateParam();
        }
//        return this.templateService.findPageBySpec(templateParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.templateService.findPageBySpec(templateParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.templateService.findPageBySpec(templateParam, dataScope);
        }
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    public ResponseData<List<Map<String, Object>>> listSelect() {
        QueryWrapper<Template> templateQueryWrapper = new QueryWrapper<>();
        templateQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.templateService.listMaps(templateQueryWrapper);
        TemplateSelectWrapper templateSelectWrapper = new TemplateSelectWrapper(list);
        List<Map<String, Object>> result = templateSelectWrapper.wrap();
        return ResponseData.success(result);
    }

    /**
     * 批量删除
     *
     * @author
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData batchDelete(@RequestBody BatchDeleteRequest batchDeleteRequest) {
        this.templateService.batchDelete(batchDeleteRequest.getIds());
        return ResponseData.success();
    }


    @RequestMapping(value = "/getLabel", method = RequestMethod.GET)
    public ResponseData getLabel(@RequestParam Long id) {
        List<String> label = this.templateService.getLabel(id);
        return ResponseData.success(label);
    }
}


