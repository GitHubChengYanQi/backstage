package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.params.BusinessDetailedParam;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ContractDetail;
import cn.atsoft.dasheng.app.model.params.ContractDetailParam;
import cn.atsoft.dasheng.app.model.result.ContractDetailResult;
import cn.atsoft.dasheng.app.service.ContractDetailService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
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
 * 合同产品明细控制器
 *
 * @author sb
 * @Date 2021-09-18 15:29:24
 */
@RestController
@RequestMapping("/contractDetail")
@Api(tags = "合同产品明细")
public class ContractDetailController extends BaseController {

    @Autowired
    private ContractDetailService contractDetailService;

    /**
     * 新增接口
     *
     * @author sb
     * @Date 2021-09-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ContractDetailParam contractDetailParam) {
        this.contractDetailService.add(contractDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author sb
     * @Date 2021-09-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ContractDetailParam contractDetailParam) {
        contractDetailParam.setTotalPrice(contractDetailParam.getSalePrice() * contractDetailParam.getQuantity());
        this.contractDetailService.update(contractDetailParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/addItems", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData addItems(@RequestBody BusinessDetailedParam businessDetailedParam) {
        this.contractDetailService.addAll(businessDetailedParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author sb
     * @Date 2021-09-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ContractDetailParam contractDetailParam)  {
        this.contractDetailService.delete(contractDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author sb
     * @Date 2021-09-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ContractDetailParam contractDetailParam) {
        ContractDetail detail = this.contractDetailService.getById(contractDetailParam.getId());
        ContractDetailResult result = new ContractDetailResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author sb
     * @Date 2021-09-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) ContractDetailParam contractDetailParam) {
        if(ToolUtil.isEmpty(contractDetailParam)){
            contractDetailParam = new ContractDetailParam();
        }
//        return this.contractDetailService.findPageBySpec(contractDetailParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.contractDetailService.findPageBySpec(contractDetailParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return this.contractDetailService.findPageBySpec(contractDetailParam, dataScope);
        }
    }




}


