package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.CrmBusinessSalesProcessSelectWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessSalesProcess;
import cn.atsoft.dasheng.app.model.params.CrmBusinessSalesProcessParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessSalesProcessResult;
import cn.atsoft.dasheng.app.service.CrmBusinessSalesProcessService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 销售流程控制器
 *
 * @author
 * @Date 2021-08-04 11:29:22
 */
@RestController
@RequestMapping("/crmBusinessSalesProcess")
@Api(tags = "销售流程")
public class CrmBusinessSalesProcessController extends BaseController {

    @Autowired
    private CrmBusinessSalesProcessService crmBusinessSalesProcessService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CrmBusinessSalesProcessParam crmBusinessSalesProcessParam) {
        crmBusinessSalesProcessParam.setSalesId(salesId);
        this.crmBusinessSalesProcessService.add(crmBusinessSalesProcessParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CrmBusinessSalesProcessParam crmBusinessSalesProcessParam) {

        this.crmBusinessSalesProcessService.update(crmBusinessSalesProcessParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CrmBusinessSalesProcessParam crmBusinessSalesProcessParam) {
        this.crmBusinessSalesProcessService.delete(crmBusinessSalesProcessParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody CrmBusinessSalesProcessParam crmBusinessSalesProcessParam) {
        if (LoginContextHolder.getContext().isAdmin()){
            PageInfo<CrmBusinessSalesProcessResult> pageBySpec = crmBusinessSalesProcessService.findPageBySpec(crmBusinessSalesProcessParam, null);
            return ResponseData.success(pageBySpec.getData().get(0));
        }else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            PageInfo<CrmBusinessSalesProcessResult> pageBySpec = crmBusinessSalesProcessService.findPageBySpec(crmBusinessSalesProcessParam, dataScope);
            return ResponseData.success(pageBySpec.getData().get(0));
        }

    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-08-04
     */
    Long salesId;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CrmBusinessSalesProcessResult> list(@RequestBody(required = false) CrmBusinessSalesProcessParam crmBusinessSalesProcessParam) {
        salesId = crmBusinessSalesProcessParam.getSalesId();
        if (ToolUtil.isEmpty(crmBusinessSalesProcessParam)) {
            crmBusinessSalesProcessParam = new CrmBusinessSalesProcessParam();
        }
//        return this.crmBusinessSalesProcessService.findPageBySpec(crmBusinessSalesProcessParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.crmBusinessSalesProcessService.findPageBySpec(crmBusinessSalesProcessParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.crmBusinessSalesProcessService.findPageBySpec(crmBusinessSalesProcessParam, dataScope);
        }
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<CrmBusinessSalesProcess> processQueryWrapper = new QueryWrapper<>();
        processQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.crmBusinessSalesProcessService.listMaps(processQueryWrapper);
        CrmBusinessSalesProcessSelectWrapper salesSelectWrapper = new CrmBusinessSalesProcessSelectWrapper(list);
        List<Map<String, Object>> result = salesSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}


