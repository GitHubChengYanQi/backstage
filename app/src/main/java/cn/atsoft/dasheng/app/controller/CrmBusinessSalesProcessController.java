package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessSalesProcess;
import cn.atsoft.dasheng.app.model.params.CrmBusinessSalesProcessParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessSalesProcessResult;
import cn.atsoft.dasheng.app.service.CrmBusinessSalesProcessService;
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
 * 销售流程控制器
 *
 * @author 
 * @Date 2021-08-02 15:47:16
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
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CrmBusinessSalesProcessParam crmBusinessSalesProcessParam) {
        this.crmBusinessSalesProcessService.add(crmBusinessSalesProcessParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-08-02
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
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CrmBusinessSalesProcessParam crmBusinessSalesProcessParam)  {
        this.crmBusinessSalesProcessService.delete(crmBusinessSalesProcessParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CrmBusinessSalesProcessResult> detail(@RequestBody CrmBusinessSalesProcessParam crmBusinessSalesProcessParam) {
        CrmBusinessSalesProcess detail = this.crmBusinessSalesProcessService.getById(crmBusinessSalesProcessParam.getSalesProcessId());
        CrmBusinessSalesProcessResult result = new CrmBusinessSalesProcessResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CrmBusinessSalesProcessResult> list(@RequestBody(required = false) CrmBusinessSalesProcessParam crmBusinessSalesProcessParam) {
        if(ToolUtil.isEmpty(crmBusinessSalesProcessParam)){
            crmBusinessSalesProcessParam = new CrmBusinessSalesProcessParam();
        }
        return this.crmBusinessSalesProcessService.findPageBySpec(crmBusinessSalesProcessParam);
    }




}


