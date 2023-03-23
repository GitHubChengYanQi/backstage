package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.CodingRules;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.model.params.CodingRulesClassificationParam;
import cn.atsoft.dasheng.erp.model.params.CodingRulesParam;
import cn.atsoft.dasheng.erp.model.params.Codings;
import cn.atsoft.dasheng.erp.model.result.CodingRulesResult;
import cn.atsoft.dasheng.erp.service.CodingRulesService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.wrapper.CodingRulesClassificationSelectWrapper;
import cn.atsoft.dasheng.erp.wrapper.CodingRulesSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Param;
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
    @BussinessLog(value = "修改编码规则", key = "name", dict = CodingRulesParam.class)
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
    @BussinessLog(value = "删除编码规则", key = "name", dict = CodingRulesParam.class)
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
    public ResponseData detail(@RequestBody CodingRulesParam codingRulesParam) {
        CodingRules detail = this.codingRulesService.getById(codingRulesParam.getCodingRulesId());
        CodingRulesResult result = new CodingRulesResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        if (ToolUtil.isNotEmpty(detail.getCodingRules())) {
            String[] split = detail.getCodingRules().split(",");
            List<Codings> codingsList = new ArrayList<>();
            for (String s : split) {
                Codings codings = new Codings();
                codings.setValues(s);
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
    public PageInfo<CodingRulesResult> list(@RequestBody(required = false) CodingRulesParam codingRulesParam) {
        if (ToolUtil.isEmpty(codingRulesParam)) {
            codingRulesParam = new CodingRulesParam();
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
    public ResponseData backCoding(@RequestBody CodingRulesParam codingRulesParam) {
        if (ToolUtil.isEmpty(codingRulesParam)) {
            codingRulesParam = new CodingRulesParam();
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
    public ResponseData listSelect(@RequestBody(required = false) CodingRulesParam codingRulesParam) {
        QueryWrapper<CodingRules> codingRulesQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(codingRulesParam))
            if (ToolUtil.isNotEmpty(codingRulesParam.getModule()))
                codingRulesQueryWrapper.eq("module", codingRulesParam.getModule());
        List<Map<String, Object>> list = this.codingRulesService.listMaps(codingRulesQueryWrapper);
        CodingRulesSelectWrapper codingRulesSelectWrapper = new CodingRulesSelectWrapper(list);
        List<Map<String, Object>> result = codingRulesSelectWrapper.wrap();
        return ResponseData.success(result);
    }

}


