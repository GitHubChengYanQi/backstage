package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.model.params.CodingRulesParam;
import cn.atsoft.dasheng.erp.model.result.CodingRulesResult;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
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
 * 编码规则控制器
 *
 * @author song
 * @Date 2021-10-22 17:20:05
 */
@RestController
@RequestMapping("/codingRules")
@Api(tags = "编码规则")
public class CodingRulesController extends BaseController {

    @Autowired
    private CodingRulesService codingRulesService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CodingRulesParam codingRulesParam) {
        this.codingRulesService.add(codingRulesParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-22
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CodingRulesParam codingRulesParam) {

        this.codingRulesService.update(codingRulesParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-22
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CodingRulesParam codingRulesParam) {
        this.codingRulesService.delete(codingRulesParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-22
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CodingRulesResult> detail(@RequestBody CodingRulesParam codingRulesParam) {
        CodingRules detail = this.codingRulesService.getById(codingRulesParam.getCodingRulesId());
        CodingRulesResult result = new CodingRulesResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-22
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CodingRulesResult> list(@RequestBody(required = false) CodingRulesParam codingRulesParam) {
        if (ToolUtil.isEmpty(codingRulesParam)) {
            codingRulesParam = new CodingRulesParam();
        }
        return this.codingRulesService.findPageBySpec(codingRulesParam);
    }


    /**
     * 生成编码
     *
     * @author song
     * @Date 2021-10-23
     */
    @RequestMapping(value = "/backCoding", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData backCoding(@RequestBody CodingRulesParam codingRulesParam) {
        if (ToolUtil.isEmpty(codingRulesParam)) {
            codingRulesParam = new CodingRulesParam();
        }
        String backCoding = this.codingRulesService.backCoding(codingRulesParam.getCodingRulesId());
        return ResponseData.success(backCoding);
    }

}


