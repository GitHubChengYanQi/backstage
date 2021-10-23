package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.CodingRulesClassification;
import cn.atsoft.dasheng.erp.model.params.CodingRulesClassificationParam;
import cn.atsoft.dasheng.erp.model.result.CodingRulesClassificationResult;
import cn.atsoft.dasheng.erp.service.CodingRulesClassificationService;
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
 * 编码规则分类控制器
 *
 * @author song
 * @Date 2021-10-22 17:20:05
 */
@RestController
@RequestMapping("/codingRulesClassification")
@Api(tags = "编码规则分类")
public class CodingRulesClassificationController extends BaseController {

    @Autowired
    private CodingRulesClassificationService codingRulesClassificationService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CodingRulesClassificationParam codingRulesClassificationParam) {
        this.codingRulesClassificationService.add(codingRulesClassificationParam);
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
    public ResponseData update(@RequestBody CodingRulesClassificationParam codingRulesClassificationParam) {

        this.codingRulesClassificationService.update(codingRulesClassificationParam);
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
    public ResponseData delete(@RequestBody CodingRulesClassificationParam codingRulesClassificationParam)  {
        this.codingRulesClassificationService.delete(codingRulesClassificationParam);
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
    public ResponseData<CodingRulesClassificationResult> detail(@RequestBody CodingRulesClassificationParam codingRulesClassificationParam) {
        CodingRulesClassification detail = this.codingRulesClassificationService.getById(codingRulesClassificationParam.getCodingRulesClassificationId());
        CodingRulesClassificationResult result = new CodingRulesClassificationResult();
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
    public PageInfo<CodingRulesClassificationResult> list(@RequestBody(required = false) CodingRulesClassificationParam codingRulesClassificationParam) {
        if(ToolUtil.isEmpty(codingRulesClassificationParam)){
            codingRulesClassificationParam = new CodingRulesClassificationParam();
        }
        return this.codingRulesClassificationService.findPageBySpec(codingRulesClassificationParam);
    }




}


