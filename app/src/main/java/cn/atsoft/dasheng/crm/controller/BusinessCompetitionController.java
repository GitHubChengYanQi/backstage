package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.BusinessCompetition;
import cn.atsoft.dasheng.crm.model.params.BusinessCompetitionParam;
import cn.atsoft.dasheng.crm.model.result.BusinessCompetitionResult;
import cn.atsoft.dasheng.crm.model.result.CompetitorResult;
import cn.atsoft.dasheng.crm.service.BusinessCompetitionService;
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
 * 商机竞争对手绑定控制器
 *
 * @author
 * @Date 2021-09-07 09:55:06
 */
@RestController
@RequestMapping("/businessCompetition")
@Api(tags = "商机竞争对手绑定")
public class BusinessCompetitionController extends BaseController {

    @Autowired
    private BusinessCompetitionService businessCompetitionService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-09-07
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody BusinessCompetitionParam businessCompetitionParam) {
        this.businessCompetitionService.add(businessCompetitionParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-09-07
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody BusinessCompetitionParam businessCompetitionParam) {

        this.businessCompetitionService.update(businessCompetitionParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-09-07
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody BusinessCompetitionParam businessCompetitionParam) {
        this.businessCompetitionService.delete(businessCompetitionParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-09-07
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody BusinessCompetitionParam businessCompetitionParam) {
        BusinessCompetition detail = this.businessCompetitionService.getById(businessCompetitionParam.getBusinessCompetitionId());
        BusinessCompetitionResult result = new BusinessCompetitionResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-09-07
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<BusinessCompetitionResult> list(@RequestBody(required = false) BusinessCompetitionParam businessCompetitionParam) {
        if (ToolUtil.isEmpty(businessCompetitionParam)) {
            businessCompetitionParam = new BusinessCompetitionParam();
        }
        return this.businessCompetitionService.findPageBySpec(businessCompetitionParam);
    }


    @RequestMapping(value = "/listCompetition", method = RequestMethod.POST)
    @ApiOperation("查询竞争对手")
    public ResponseData listCompetition(@RequestBody(required = false) BusinessCompetitionParam businessCompetitionParam) {
        List<CompetitorResult> comptitor = businessCompetitionService.findComptitor(businessCompetitionParam);
        return ResponseData.success(comptitor);
    }


}


