package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.params.TemplateParam;
import cn.atsoft.dasheng.app.model.request.ContractDetailSetRequest;
import cn.atsoft.dasheng.app.model.result.ContractDetailResult;
import cn.atsoft.dasheng.app.model.result.ContractRequest;
import cn.atsoft.dasheng.app.model.result.TemplateResult;
import cn.atsoft.dasheng.app.service.TemplateService;
import cn.atsoft.dasheng.app.wrapper.ContractDetailSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.ContractMachineSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.CustomerSelectWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 合同表控制器
 *
 * @author
 * @Date 2021-07-21 13:36:21
 */
@RestController
@RequestMapping("/contract")
@Api(tags = "合同表")
public class ContractController extends BaseController {

    @Autowired
    private ContractService contractService;
    private Long customerId;
    @Autowired
    private TemplateService templateService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-07-21
     */
    @Permission
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ContractParam contractParam) {
        if (contractParam.getTemplateId() != null) {
            TemplateParam templateParam = new TemplateParam();
            templateParam.setTemplateId(contractParam.getTemplateId());
            PageInfo<TemplateResult> pageBySpec = templateService.findPageBySpec(templateParam, null);
            contractParam.setContent(pageBySpec.getData().get(0).getContent());
        }
        ContractResult contractResult = this.contractService.addResult(contractParam);

        return ResponseData.success(contractResult);
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-07-21
     */
    @Permission
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ContractParam contractParam) {
//        if (contractParam.getTemplateId()!=null){
//            TemplateParam templateParam = new TemplateParam();
//            templateParam.setTemplateId(contractParam.getTemplateId());
//            PageInfo<TemplateResult> pageBySpec = templateService.findPageBySpec(templateParam);
//            contractParam.setContent(pageBySpec.getData().get(0).getContent());
//        }
        this.contractService.update(contractParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-07-21
     */
    @Permission
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ContractParam contractParam) {
        this.contractService.delete(contractParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-07-21
     */
    @Permission
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ContractResult> detail(@RequestBody ContractParam contractParam) {
        Long customerId = contractParam.getContractId();
        ContractResult detail = contractService.detail(customerId);
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-21
     */
    @Permission
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ContractResult> list(@RequestBody(required = false) ContractParam contractParam) {
        if (ToolUtil.isEmpty(contractParam)) {
            contractParam = new ContractParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.contractService.findPageBySpec(contractParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.contractService.findPageBySpec(contractParam, dataScope);
        }
    }

    @Permission
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("批量删除")
    public ResponseData batchDelete(@RequestBody ContractRequest contractIdRequest) {
        contractService.batchDelete(contractIdRequest.getContractId());
        return ResponseData.success();
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    @Permission
    public ResponseData<List<Map<String, Object>>> listSelect(@RequestBody(required = false) ContractParam contractParam) {
        QueryWrapper<Contract> queryWrapper = new QueryWrapper();
        queryWrapper.in("display", 1);
        if (ToolUtil.isNotEmpty(contractParam) && ToolUtil.isNotEmpty(contractParam.getContractId())) {
            queryWrapper.in("contract_id", contractParam.getContractId());
        }
        List<Map<String, Object>> list = this.contractService.listMaps(queryWrapper);
        ContractMachineSelectWrapper customerSelectWrapper = new ContractMachineSelectWrapper(list);
        List<Map<String, Object>> result = customerSelectWrapper.wrap();
        return ResponseData.success(result);
    }

    @RequestMapping(value = "/pendingProductionPlan", method = RequestMethod.POST)
    @ApiOperation("待生产计划")
    public ResponseData pendingProductionPlan() {
        Set<ContractDetailSetRequest> contractDetailSetRequests = this.contractService.pendingProductionPlan();
        return ResponseData.success(contractDetailSetRequests);
    }


}


