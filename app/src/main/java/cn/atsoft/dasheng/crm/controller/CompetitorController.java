package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.entity.CompetitorQuote;
import cn.atsoft.dasheng.crm.model.params.CompanyRoleParam;
import cn.atsoft.dasheng.crm.model.params.CompetitorIdsRequest;
import cn.atsoft.dasheng.crm.model.params.CompetitorParam;
import cn.atsoft.dasheng.crm.model.params.ListSelectRequest;
import cn.atsoft.dasheng.crm.model.result.CompetitorResult;
import cn.atsoft.dasheng.crm.service.CompetitorQuoteService;
import cn.atsoft.dasheng.crm.service.CompetitorService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.wrapper.CompetitorSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author
 * @Date 2021-09-07 09:50:09
 */
@RestController
@RequestMapping("/competitor")
@Api(tags = "")
public class CompetitorController extends BaseController {

    @Autowired
    private CompetitorService competitorService;
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
    public ResponseData addItem(@RequestBody CompetitorParam competitorParam) {
        Competitor competitor = competitorService.add(competitorParam);

        return ResponseData.success(competitor);
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-09-07
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @BussinessLog(value = "竞争对手", key = "name", dict = CompanyRoleParam.class)

    @Permission
    public ResponseData update(@RequestBody CompetitorParam competitorParam) {

        this.competitorService.update(competitorParam);
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
    public ResponseData delete(@RequestBody CompetitorParam competitorParam) {
        this.competitorService.delete(competitorParam);
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
    public ResponseData detail(@RequestBody CompetitorParam competitorParam) {
        Long competitorId = competitorParam.getCompetitorId();
        final CompetitorResult detail = competitorService.detail(competitorId);
        return ResponseData.success(detail);
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
    public PageInfo<CompetitorResult> list(@RequestBody(required = false) CompetitorParam competitorParam) {
        if (ToolUtil.isEmpty(competitorParam)) {
            competitorParam = new CompetitorParam();
        }
//        if (LoginContextHolder.getContext().isAdmin()) {
            return this.competitorService.findPageBySpec(null, competitorParam);
//        } else {
//            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
//            return this.competitorService.findPageBySpec(dataScope, competitorParam);
//        }
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(@RequestBody(required = false) ListSelectRequest listSelectRequest) {
        if (ToolUtil.isEmpty(listSelectRequest)) {
            listSelectRequest = new ListSelectRequest();
        }
        QueryWrapper<Competitor> competitorQueryWrapper = new QueryWrapper<>();
        competitorQueryWrapper.in("display", 1);
        //通过传入参数去找当前的下拉参数
        if (ToolUtil.isNotEmpty(listSelectRequest.getIds())) {
            List<Long> competitorIds = new ArrayList<>();
            List<CompetitorQuote> competitorQuotes = competitorQuoteService.lambdaQuery().eq(CompetitorQuote::getBusinessId, listSelectRequest.getIds()).list();
            for (CompetitorQuote competitorQuote : competitorQuotes) {
                competitorIds.add(competitorQuote.getCompetitorId());
            }
            competitorQueryWrapper.in("competitor_id", competitorIds);
        }
        List<Map<String, Object>> list = this.competitorService.listMaps(competitorQueryWrapper);
        CompetitorSelectWrapper competitorSelectWrapper = new CompetitorSelectWrapper(list);
        List<Map<String, Object>> result = competitorSelectWrapper.wrap();
        return ResponseData.success(result);

    }

    @RequestMapping(value = "/deleteByIds", method = RequestMethod.POST)
    @ApiOperation("多选删除")
    @Permission
    public ResponseData deleteByIds(@RequestBody CompetitorIdsRequest competitorIdsRequest) {
        List<Long> ids = competitorIdsRequest.getCompetitorId();
        this.competitorService.deleteByIds(ids);
        return ResponseData.success();
    }

}


