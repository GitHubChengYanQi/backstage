package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.crm.entity.CompetitorQuote;
import cn.atsoft.dasheng.crm.model.params.CompetitorQuoteParam;
import cn.atsoft.dasheng.crm.model.result.CompetitorQuoteResult;
import cn.atsoft.dasheng.crm.service.CompetitorQuoteService;
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
 * 竞争对手报价控制器
 *
 * @author 
 * @Date 2021-09-07 13:38:31
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
     * @Date 2021-09-07
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    @Permission
    public ResponseData addItem(@RequestBody CompetitorQuoteParam competitorQuoteParam) {
        this.competitorQuoteService.add(competitorQuoteParam);
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
    @Permission
    public ResponseData update(@RequestBody CompetitorQuoteParam competitorQuoteParam) {

        this.competitorQuoteService.update(competitorQuoteParam);
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
    @Permission
    public ResponseData delete(@RequestBody CompetitorQuoteParam competitorQuoteParam)  {
        this.competitorQuoteService.delete(competitorQuoteParam);
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
    @Permission
    public ResponseData detail(@RequestBody CompetitorQuoteParam competitorQuoteParam) {
        CompetitorQuote detail = this.competitorQuoteService.getById(competitorQuoteParam.getQuoteId());
        CompetitorQuoteResult result = new CompetitorQuoteResult();
        ToolUtil.copyProperties(detail, result);

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
    @Permission
    public PageInfo<CompetitorQuoteResult> list(@RequestBody(required = false) CompetitorQuoteParam competitorQuoteParam) {
        if(ToolUtil.isEmpty(competitorQuoteParam)){
            competitorQuoteParam = new CompetitorQuoteParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.competitorQuoteService.findPageBySpec(competitorQuoteParam,null);
        }else{
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.competitorQuoteService.findPageBySpec(competitorQuoteParam,dataScope);
        }
    }




}


