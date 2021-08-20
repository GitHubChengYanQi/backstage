package cn.atsoft.dasheng.portal.companyaddress.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.companyaddress.entity.CompanyAddress;
import cn.atsoft.dasheng.portal.companyaddress.model.params.CompanyAddressParam;
import cn.atsoft.dasheng.portal.companyaddress.model.result.CompanyAddressResult;
import cn.atsoft.dasheng.portal.companyaddress.service.CompanyAddressService;
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
 * 报修控制器
 *
 * @author siqiang
 * @Date 2021-08-20 17:16:16
 */
@RestController
@RequestMapping("/companyAddress")
@Api(tags = "报修")
public class CompanyAddressController extends BaseController {

    @Autowired
    private CompanyAddressService companyAddressService;

    /**
     * 新增接口
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CompanyAddressParam companyAddressParam) {
        this.companyAddressService.add(companyAddressParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CompanyAddressParam companyAddressParam) {

        this.companyAddressService.update(companyAddressParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CompanyAddressParam companyAddressParam)  {
        this.companyAddressService.delete(companyAddressParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CompanyAddressResult> detail(@RequestBody CompanyAddressParam companyAddressParam) {
        CompanyAddress detail = this.companyAddressService.getById(companyAddressParam.getCompanyId());
        CompanyAddressResult result = new CompanyAddressResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CompanyAddressResult> list(@RequestBody(required = false) CompanyAddressParam companyAddressParam) {
        if(ToolUtil.isEmpty(companyAddressParam)){
            companyAddressParam = new CompanyAddressParam();
        }
        return this.companyAddressService.findPageBySpec(companyAddressParam);
    }




}


