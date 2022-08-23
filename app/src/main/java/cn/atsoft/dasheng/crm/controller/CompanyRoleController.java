package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.model.result.BatchDeleteRequest;
import cn.atsoft.dasheng.app.wrapper.BrandSelectWrapper;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.CompanyRole;
import cn.atsoft.dasheng.crm.model.params.CompanyRoleParam;
import cn.atsoft.dasheng.crm.model.result.CompanyRoleResult;
import cn.atsoft.dasheng.crm.service.CompanyRoleService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.wrapper.CompanyRoleSelectWrapper;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
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
 * 公司角色表控制器
 *
 * @author
 * @Date 2021-09-06 11:29:56
 */
@RestController
@RequestMapping("/companyRole")
@Api(tags = "公司角色表")
public class CompanyRoleController extends BaseController {

    @Autowired
    private CompanyRoleService companyRoleService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-09-06
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CompanyRoleParam companyRoleParam) {
        CompanyRole add = this.companyRoleService.add(companyRoleParam);
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
    public ResponseData update(@RequestBody CompanyRoleParam companyRoleParam) {

        this.companyRoleService.update(companyRoleParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-09-06
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CompanyRoleParam companyRoleParam) {
        this.companyRoleService.delete(companyRoleParam);
        return ResponseData.success();
    }

    /**
     * 批量删除接口
     *
     * @param batchDeleteRequest
     * @return
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("批量删除")
    public ResponseData batchDelete(@RequestBody(required = false) BatchDeleteRequest batchDeleteRequest) {
        this.companyRoleService.batchDelete(batchDeleteRequest.getIds());
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
    public ResponseData detail(@RequestBody CompanyRoleParam companyRoleParam) {
        CompanyRole detail = this.companyRoleService.getById(companyRoleParam.getCompanyRoleId());
        CompanyRoleResult result = new CompanyRoleResult();
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
    public PageInfo<CompanyRoleResult> list(@RequestBody(required = false) CompanyRoleParam companyRoleParam) {
        if (ToolUtil.isEmpty(companyRoleParam)) {
            companyRoleParam = new CompanyRoleParam();
        }
        return this.companyRoleService.findPageBySpec(companyRoleParam);
    }

    /**
     * 选择列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<CompanyRole> companyRoleQueryWrapper = new QueryWrapper<>();
        companyRoleQueryWrapper.in("display",1);
        List<Map<String,Object>> list = this.companyRoleService.listMaps(companyRoleQueryWrapper);
        CompanyRoleSelectWrapper factory = new CompanyRoleSelectWrapper(list);
        List<Map<String,Object>> result = factory.wrap();
        return ResponseData.success(result);
    }

}


