package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.model.params.CompetitorParam;
import cn.atsoft.dasheng.crm.model.result.CompetitorResult;
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

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-09-07
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CompetitorParam competitorParam) {
         competitorService.add(competitorParam);

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
    public ResponseData<CompetitorResult> detail(@RequestBody CompetitorParam competitorParam) {
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
    public PageInfo<CompetitorResult> list(@RequestBody(required = false) CompetitorParam competitorParam) {
        if (ToolUtil.isEmpty(competitorParam)) {
            competitorParam = new CompetitorParam();
        }
        return this.competitorService.findPageBySpec(competitorParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect() {
        QueryWrapper<Competitor> competitorQueryWrapper = new QueryWrapper<>();
        competitorQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.competitorService.listMaps(competitorQueryWrapper);
        CompetitorSelectWrapper competitorSelectWrapper = new CompetitorSelectWrapper(list);
        List<Map<String, Object>> result = competitorSelectWrapper.wrap();
        return ResponseData.success(result);

    }


}


