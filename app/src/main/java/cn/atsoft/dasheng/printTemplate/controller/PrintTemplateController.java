package cn.atsoft.dasheng.printTemplate.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.printTemplate.entity.PrintTemplate;
import cn.atsoft.dasheng.printTemplate.model.params.PrintTemplateParam;
import cn.atsoft.dasheng.printTemplate.model.result.PrintTemplateResult;
import cn.atsoft.dasheng.printTemplate.service.PrintTemplateService;
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
 * 编辑模板控制器
 *
 * @author Captain_Jazz
 * @Date 2021-12-28 13:24:55
 */
@RestController
@RequestMapping("/printTemplate")
@Api(tags = "编辑模板")
public class PrintTemplateController extends BaseController {

    @Autowired
    private PrintTemplateService printTemplateService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PrintTemplateParam printTemplateParam) {
        this.printTemplateService.add(printTemplateParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PrintTemplateParam printTemplateParam) {

        this.printTemplateService.update(printTemplateParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PrintTemplateParam printTemplateParam)  {
        this.printTemplateService.delete(printTemplateParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<PrintTemplateResult> detail(@RequestBody PrintTemplateParam printTemplateParam) {
        PrintTemplate detail = this.printTemplateService.getById(printTemplateParam.getPrintTemplateId());
        PrintTemplateResult result = new PrintTemplateResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-28
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PrintTemplateResult> list(@RequestBody(required = false) PrintTemplateParam printTemplateParam) {
        if(ToolUtil.isEmpty(printTemplateParam)){
            printTemplateParam = new PrintTemplateParam();
        }
        return this.printTemplateService.findPageBySpec(printTemplateParam);
    }




}


