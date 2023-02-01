package cn.atsoft.dasheng.coderule.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.coderule.entity.RestCodeRuleCategory;
import cn.atsoft.dasheng.coderule.model.params.RestCodeRuleCategoryParam;
import cn.atsoft.dasheng.coderule.model.params.RestCodeRuleParam;
import cn.atsoft.dasheng.coderule.model.result.RestCodeRuleCategoryResult;
import cn.atsoft.dasheng.coderule.service.RestCodeRuleCategoryService;
import cn.atsoft.dasheng.coderule.wrapper.RestCodeRuleCategorySelectWrapper;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;


/**
 * 编码规则分类控制器
 *
 * @author song
 * @Date 2021-10-22 17:20:05
 */
@RestController
@RequestMapping("/codingRulesCategory/{version}")
@ApiVersion("2.0")
@Api(tags = "编码规则分类 管理")
public class RestCodeRuleCategoryController extends BaseController {

    @Autowired
    private RestCodeRuleCategoryService codingRulesClassificationService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestCodeRuleCategoryParam codingRulesClassificationParam) {
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
    @BussinessLog(value = "修改编码规则分类", key = "name", dict = RestCodeRuleCategoryParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RestCodeRuleCategoryParam codingRulesClassificationParam) {

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
    @BussinessLog(value = "删除编码规则分类", key = "name", dict = RestCodeRuleParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RestCodeRuleCategoryParam codingRulesClassificationParam) {
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
    public ResponseData detail(@RequestBody RestCodeRuleCategoryParam codingRulesClassificationParam) {
        RestCodeRuleCategory detail = this.codingRulesClassificationService.getById(codingRulesClassificationParam.getCodingRulesClassificationId());
        RestCodeRuleCategoryResult result = new RestCodeRuleCategoryResult();
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
    public PageInfo<RestCodeRuleCategoryResult> list(@RequestBody(required = false) RestCodeRuleCategoryParam codingRulesClassificationParam) {
        if (ToolUtil.isEmpty(codingRulesClassificationParam)) {
            codingRulesClassificationParam = new RestCodeRuleCategoryParam();
        }
        return this.codingRulesClassificationService.findPageBySpec(codingRulesClassificationParam);
    }

    /**
     * 下拉接口
     *
     * @return
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    public ResponseData listSelect() {
        List<Map<String, Object>> list = this.codingRulesClassificationService.listMaps();
        RestCodeRuleCategorySelectWrapper codingRulesClassificationSelectWrapper = new RestCodeRuleCategorySelectWrapper(list);
        List<Map<String, Object>> result = codingRulesClassificationSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}


