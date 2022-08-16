package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.entity.CrmBusiness;
import cn.atsoft.dasheng.app.model.result.BusinessRequest;
import cn.atsoft.dasheng.app.wrapper.CrmBusinessSelectWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.model.params.CrmBusinessParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessResult;
import cn.atsoft.dasheng.app.service.CrmBusinessService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.model.params.BusinessCompetitionParam;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * 商机表控制器
 *
 * @author
 * @Date 2021-08-03 14:04:51
 */
@RestController
@RequestMapping("/crmBusiness")
@Api(tags = "商机表")
public class CrmBusinessController extends BaseController {

    @Autowired
    private CrmBusinessService crmBusinessService;



    /**
     * 新增接口
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    @Permission
    public ResponseData addItem(@RequestBody @Valid CrmBusinessParam crmBusinessParam) {

        CrmBusiness result = crmBusinessService.add(crmBusinessParam);
        return ResponseData.success(result.getBusinessId());
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
    public ResponseData update(@RequestBody CrmBusinessParam crmBusinessParam) {

        this.crmBusinessService.update(crmBusinessParam);
        if (crmBusinessParam.getBusinessId() == null) {
            return ResponseData.error("请选择你要的商机");
        }
        return ResponseData.success();
    }

    /**
     * 更新负责人
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/updateChargePerson", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
    public ResponseData updateChargePerson(@RequestBody CrmBusinessParam crmBusinessParam) {

        if (crmBusinessParam.getBusinessId() == null) {
            throw new ServiceException(500,"请选择你要的商机");
        }
        this.crmBusinessService.updateChargePerson(crmBusinessParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    @Permission
    public ResponseData delete(@RequestBody CrmBusinessParam crmBusinessParam) {
        this.crmBusinessService.delete(crmBusinessParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    @Permission
    public ResponseData detail(@RequestBody CrmBusinessParam crmBusinessParam) {
        CrmBusinessResult bySpec = crmBusinessService.detail(crmBusinessParam.getBusinessId());
        return ResponseData.success(bySpec);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo<CrmBusinessResult> list(@RequestBody(required = false) CrmBusinessParam crmBusinessParam) {
        if (ToolUtil.isEmpty(crmBusinessParam)) {
            crmBusinessParam = new CrmBusinessParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.crmBusinessService.findPageBySpec(null, crmBusinessParam);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.crmBusinessService.findPageBySpec(dataScope, crmBusinessParam);
        }
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public Integer listAll(@RequestBody(required = false) CrmBusinessParam crmBusinessParam) {
        return this.crmBusinessService.findListBySpec(crmBusinessParam).size();
    }


    /**
     * 选择列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    @Permission
    public ResponseData listSelect(@RequestBody(required = false) CrmBusinessParam crmBusinessParam) {
        QueryWrapper<CrmBusiness> businessQueryWrapper = new QueryWrapper<>();
        businessQueryWrapper.in("display", 1);
        if (ToolUtil.isNotEmpty(crmBusinessParam) && ToolUtil.isNotEmpty(crmBusinessParam.getBusinessId())){
            businessQueryWrapper.in("business_id", crmBusinessParam.getBusinessId());
        }
        List<Map<String, Object>> list = this.crmBusinessService.listMaps(businessQueryWrapper);
        CrmBusinessSelectWrapper factory = new CrmBusinessSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


    @RequestMapping(value = "/UpdateStatus", method = RequestMethod.POST)
    @ApiOperation("更新状态")
    @Permission
    public ResponseData UpdateStatus(@RequestBody CrmBusinessParam crmBusinessParam) {
        String s = crmBusinessService.UpdateStatus(crmBusinessParam);
        return ResponseData.success(s);
    }

    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("批量删除")
    @Permission
    public ResponseData batchDelete(@RequestBody BusinessRequest businessRequest) {
        crmBusinessService.batchDelete(businessRequest.getBusinessId());
        return ResponseData.success();
    }
}


