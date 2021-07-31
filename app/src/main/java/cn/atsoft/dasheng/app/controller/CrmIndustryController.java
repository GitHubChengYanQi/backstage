package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmIndustry;
import cn.atsoft.dasheng.app.model.params.CrmIndustryParam;
import cn.atsoft.dasheng.app.model.result.CrmIndustryResult;
import cn.atsoft.dasheng.app.service.CrmIndustryService;
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
 * 行业表控制器
 *
 * @author 
 * @Date 2021-07-31 16:28:22
 */
@RestController
@RequestMapping("/crmIndustry")
@Api(tags = "行业表")
public class CrmIndustryController extends BaseController {

    @Autowired
    private CrmIndustryService crmIndustryService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-07-31
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CrmIndustryParam crmIndustryParam) {
        this.crmIndustryService.add(crmIndustryParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-07-31
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CrmIndustryParam crmIndustryParam) {

        this.crmIndustryService.update(crmIndustryParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-07-31
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CrmIndustryParam crmIndustryParam)  {
        this.crmIndustryService.delete(crmIndustryParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-07-31
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CrmIndustryResult> detail(@RequestBody CrmIndustryParam crmIndustryParam) {
        CrmIndustry detail = this.crmIndustryService.getById(crmIndustryParam.getIndustryId());
        CrmIndustryResult result = new CrmIndustryResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-07-31
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CrmIndustryResult> list(@RequestBody(required = false) CrmIndustryParam crmIndustryParam) {
        if(ToolUtil.isEmpty(crmIndustryParam)){
            crmIndustryParam = new CrmIndustryParam();
        }
        return this.crmIndustryService.findPageBySpec(crmIndustryParam);
    }




}


