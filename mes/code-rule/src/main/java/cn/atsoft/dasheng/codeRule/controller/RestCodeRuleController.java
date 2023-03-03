package cn.atsoft.dasheng.codeRule.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.codeRule.entity.RestCodeRule;
import cn.atsoft.dasheng.codeRule.model.GeneralRoleList;
import cn.atsoft.dasheng.codeRule.model.RestCode;
import cn.atsoft.dasheng.codeRule.model.RestCodeRulesCategory;
import cn.atsoft.dasheng.codeRule.model.enums.CodingRuleFiledEnum;
import cn.atsoft.dasheng.codeRule.model.params.RestCodeRuleParam;
import cn.atsoft.dasheng.codeRule.model.result.RestCodeRuleResult;
import cn.atsoft.dasheng.codeRule.service.RestCodeRuleCategoryService;
import cn.atsoft.dasheng.codeRule.service.RestCodeRuleService;
import cn.atsoft.dasheng.codeRule.wrapper.RestCodeRuleSelectWrapper;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 编码规则控制器
 *
 * @author song
 * @Date 2021-10-22 17:20:05
 */
@RestController
@RequestMapping("/codingRules/{version}")
@ApiVersion("2.0")
@Api(tags = "编码规则 管理")
public class RestCodeRuleController extends BaseController {

    @Autowired
    private RestCodeRuleService codingRulesService;

    @Autowired
    private RestCodeRuleCategoryService codeRuleCategoryService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestCodeRuleParam codingRulesParam) {
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
    @BussinessLog(value = "修改编码规则", key = "name", dict = RestCodeRuleParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RestCodeRuleParam codingRulesParam) {

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
    @BussinessLog(value = "删除编码规则", key = "name", dict = RestCodeRuleParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RestCodeRuleParam codingRulesParam) {
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
    public ResponseData detail(@RequestBody RestCodeRuleParam codingRulesParam) {
        RestCodeRule detail = this.codingRulesService.getById(codingRulesParam.getCodingRulesId());
        RestCodeRuleResult result = new RestCodeRuleResult();
        ToolUtil.copyProperties(detail, result);

        if (ToolUtil.isNotEmpty(detail.getCodingRules())) {
            String[] split = detail.getCodingRules().split(",");
            List<RestCode> codingsList = new ArrayList<>();
            for (String s : split) {
                RestCode codings = new RestCode();
                codings.setValue(s);
                codingsList.add(codings);
            }
            result.setCodings(codingsList);
        }

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
    public PageInfo<RestCodeRuleResult> list(@RequestBody(required = false) RestCodeRuleParam codingRulesParam) {
        if (ToolUtil.isEmpty(codingRulesParam)) {
            codingRulesParam = new RestCodeRuleParam();
        }
        return this.codingRulesService.findPageBySpec(codingRulesParam);
    }

    /**
     * 默认编码
     *
     * @return
     */
    @RequestMapping(value = "/defaultEncoding", method = RequestMethod.GET)
    public ResponseData defaultEncoding(@Param("type") Integer type) {
        String encoding = "";

        if (ToolUtil.isNotEmpty(type)) {
            encoding = this.codingRulesService.encoding(type);
        } else {
            encoding = this.codingRulesService.defaultEncoding();
        }
        return ResponseData.success(encoding);
    }

    /**
     * 生成编码
     *
     * @author song
     * @Date 2021-10-23
     */
    @RequestMapping(value = "/backCoding", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData backCoding(@RequestBody RestCodeRuleParam codingRulesParam) {
        if (ToolUtil.isEmpty(codingRulesParam)) {
            codingRulesParam = new RestCodeRuleParam();
        }
        String backCoding = this.codingRulesService.backCoding(codingRulesParam.getCodingRulesId());
        return ResponseData.success(backCoding);
    }

    /**
     * 下拉接口
     *
     * @return
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    public ResponseData listSelect(@RequestBody(required = false) RestCodeRuleParam codingRulesParam) {
        QueryWrapper<RestCodeRule> codingRulesQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(codingRulesParam))
            if (ToolUtil.isNotEmpty(codingRulesParam.getModule()))
                codingRulesQueryWrapper.eq("module", codingRulesParam.getModule());
        List<Map<String, Object>> list = this.codingRulesService.listMaps(codingRulesQueryWrapper);
        RestCodeRuleSelectWrapper codingRulesSelectWrapper = new RestCodeRuleSelectWrapper(list);
        List<Map<String, Object>> result = codingRulesSelectWrapper.wrap();
        return ResponseData.success(result);
    }
    /**
     * 下拉接口
     *
     * @return
     */
    @RequestMapping(value = "/getRule", method = RequestMethod.GET)
    public ResponseData getRule() {
        List<RestCodeRulesCategory> restCodeRulesCategories = codeRuleCategoryService.get();

        Map<String,Object> result = new HashMap<>();
        result.put("generalRuleList", CodingRuleFiledEnum.listByModel(CodingRuleFiledEnum.modelEnum.general));
        result.put("modelRuleList", restCodeRulesCategories);
        return ResponseData.success(result);
    }

}


