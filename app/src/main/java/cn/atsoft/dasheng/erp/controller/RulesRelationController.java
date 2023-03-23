package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.RulesRelation;
import cn.atsoft.dasheng.erp.model.params.RulesRelationParam;
import cn.atsoft.dasheng.erp.model.result.RulesRelationResult;
import cn.atsoft.dasheng.erp.service.RulesRelationService;
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
 * 编码规则和模块的对应关系控制器
 *
 * @author song
 * @Date 2021-10-25 14:05:08
 */
@RestController
@RequestMapping("/rulesRelation")
@Api(tags = "编码规则和模块的对应关系")
public class RulesRelationController extends BaseController {

    @Autowired
    private RulesRelationService rulesRelationService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RulesRelationParam rulesRelationParam) {
        this.rulesRelationService.add(rulesRelationParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RulesRelationParam rulesRelationParam) {

        this.rulesRelationService.update(rulesRelationParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RulesRelationParam rulesRelationParam)  {
        this.rulesRelationService.delete(rulesRelationParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody RulesRelationParam rulesRelationParam) {
        RulesRelation detail = this.rulesRelationService.getById(rulesRelationParam.getRulesRelationId());
        RulesRelationResult result = new RulesRelationResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RulesRelationResult> list(@RequestBody(required = false) RulesRelationParam rulesRelationParam) {
        if(ToolUtil.isEmpty(rulesRelationParam)){
            rulesRelationParam = new RulesRelationParam();
        }
        return this.rulesRelationService.findPageBySpec(rulesRelationParam);
    }




}


