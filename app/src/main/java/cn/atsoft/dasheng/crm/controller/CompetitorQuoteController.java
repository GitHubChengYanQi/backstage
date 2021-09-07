package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.CompanyRole;
import cn.atsoft.dasheng.crm.entity.CompetitorQuote;
import cn.atsoft.dasheng.crm.model.params.CompetitorQuoteParam;
import cn.atsoft.dasheng.crm.model.result.CompetitorQuoteResult;
import cn.atsoft.dasheng.crm.service.CompetitorQuoteService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.wrapper.CompanyRoleSelectWrapper;
import cn.atsoft.dasheng.crm.wrapper.CompetitorQuoteSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 竞争对手报价控制器
 *
 * @author 
 * @Date 2021-09-06 16:08:01
 */
@RestController
@RequestMapping("/competitorQuote")
@Api(tags = "竞争对手报价")
public class CompetitorQuoteController extends BaseController {

    @Autowired
    private CompetitorQuoteService competitorQuoteService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-09-06
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CompetitorQuoteParam competitorQuoteParam) {
        CompetitorQuote add = this.competitorQuoteService.add(competitorQuoteParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-09-06
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CompetitorQuoteParam competitorQuoteParam) {
        CompetitorQuote update = this.competitorQuoteService.update(competitorQuoteParam);
        return ResponseData.success(update);
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-09-06
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CompetitorQuoteParam competitorQuoteParam)  {
        this.competitorQuoteService.delete(competitorQuoteParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-09-06
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CompetitorQuoteResult> detail(@RequestBody CompetitorQuoteParam competitorQuoteParam) {
        CompetitorQuote detail = this.competitorQuoteService.getById(competitorQuoteParam.getCompetitorsQuoteId());
        CompetitorQuoteResult result = new CompetitorQuoteResult();
        ToolUtil.copyProperties(detail, result);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-09-06
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CompetitorQuoteResult> list(@RequestBody(required = false) CompetitorQuoteParam competitorQuoteParam) {
        if(ToolUtil.isEmpty(competitorQuoteParam)){
            competitorQuoteParam = new CompetitorQuoteParam();
        }
        return this.competitorQuoteService.findPageBySpec(competitorQuoteParam);
    }

    /**
     * 选择列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String,Object>>> listSelect() {
        QueryWrapper<CompetitorQuote> competitorQuoteQueryWrapper = new QueryWrapper<>();
        competitorQuoteQueryWrapper.in("display",1);
        List<Map<String,Object>> list = this.competitorQuoteService.listMaps(competitorQuoteQueryWrapper);
        CompetitorQuoteSelectWrapper factory = new CompetitorQuoteSelectWrapper(list);
        List<Map<String,Object>> result = factory.wrap();
        return ResponseData.success(result);
    }





}


