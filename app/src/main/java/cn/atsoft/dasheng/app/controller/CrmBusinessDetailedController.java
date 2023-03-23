package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.params.BusinessDetailedParam;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessDetailed;
import cn.atsoft.dasheng.app.model.params.CrmBusinessDetailedParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessDetailedResult;
import cn.atsoft.dasheng.app.service.CrmBusinessDetailedService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 商机明细表控制器
 *
 * @author qr
 * @Date 2021-08-04 13:17:57
 */
@RestController
@RequestMapping("/crmBusinessDetailed")
@Api(tags = "商机明细表")
public class CrmBusinessDetailedController extends BaseController {

    @Autowired
    private CrmBusinessDetailedService crmBusinessDetailedService;

    /**
     * 新增接口
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CrmBusinessDetailedParam crmBusinessDetailedParam) {
        this.crmBusinessDetailedService.add(crmBusinessDetailedParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/batchAdd", method = RequestMethod.POST)
    @ApiOperation("批量新增")
    public ResponseData addAll(@RequestBody BusinessDetailedParam businessDetailedParam) {
        this.crmBusinessDetailedService.addAll(businessDetailedParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/addAllPackages", method = RequestMethod.POST)
    @ApiOperation("批量新增")
    public ResponseData addAllPackages(@RequestBody CrmBusinessDetailedParam crmBusinessDetailedParam) {
        this.crmBusinessDetailedService.addAllPackages(crmBusinessDetailedParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CrmBusinessDetailedParam crmBusinessDetailedParam) {
        crmBusinessDetailedParam.setTotalPrice(crmBusinessDetailedParam.getSalePrice() * crmBusinessDetailedParam.getQuantity());
        this.crmBusinessDetailedService.update(crmBusinessDetailedParam);
//        if(crmBusinessDetailedParam.getProcessId()==1){
//            return ResponseData.error("aaaa");
//        }else if(crmBusinessDetailedParam.getProcessId()==2){
//            return ResponseData.error("bbbb");
//        }else if(crmBusinessDetailedParam.getProcessId()==3){
//            return ResponseData.error("cccc");
//        }else  if (crmBusinessDetailedParam.getBusinessId()==4){
//            return ResponseData.error("dddd");
//        }

        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CrmBusinessDetailedParam crmBusinessDetailedParam)  {
        this.crmBusinessDetailedService.delete(crmBusinessDetailedParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody  CrmBusinessDetailedParam crmBusinessDetailedParam) {
        CrmBusinessDetailed detail = this.crmBusinessDetailedService.getById(crmBusinessDetailedParam.getId());

        CrmBusinessDetailedResult result = new CrmBusinessDetailedResult();

if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author qr
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) CrmBusinessDetailedParam crmBusinessDetailedParam) {
        if(ToolUtil.isEmpty(crmBusinessDetailedParam)){
            crmBusinessDetailedParam = new CrmBusinessDetailedParam();
        }
//        return this.crmBusinessDetailedService.findPageBySpec(crmBusinessDetailedParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.crmBusinessDetailedService.findPageBySpec(crmBusinessDetailedParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.crmBusinessDetailedService.findPageBySpec(crmBusinessDetailedParam, dataScope);
        }
    }




}


