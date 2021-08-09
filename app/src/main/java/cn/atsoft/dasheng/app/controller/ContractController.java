package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.params.TemplateParam;
import cn.atsoft.dasheng.app.model.result.TemplateResult;
import cn.atsoft.dasheng.app.service.TemplateService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import cn.atsoft.dasheng.app.service.ContractService;
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
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ContractParam contractParam) {
        if (customerId != null) {
            contractParam.setPartyA(customerId);
        }
        if (contractParam.getTemplateId()!=null){
            TemplateParam templateParam = new TemplateParam();
            templateParam.setTemplateId(contractParam.getTemplateId());
            PageInfo<TemplateResult> pageBySpec = templateService.findPageBySpec(templateParam);
            System.err.println(pageBySpec);
            contractParam.setContent(pageBySpec.getData().get(0).getContent());
        }
        Long add = this.contractService.add(contractParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ContractParam contractParam) {

        this.contractService.update(contractParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-07-21
     */
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
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ContractResult> detail(@RequestBody ContractParam contractParam) {
//        Contract detail = this.contractService.getById(contractParam.getContractId());
//        ContractResult result = new ContractResult();
//        ToolUtil.copyProperties(detail, result);
        ContractResult contractResult = this.contractService.findPageBySpec(contractParam).getData().get(0);
        return ResponseData.success(contractResult);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-21
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ContractResult> list(@RequestBody(required = false) ContractParam contractParam) {
        if (ToolUtil.isEmpty(contractParam)) {
            contractParam = new ContractParam();
        }
        return this.contractService.findPageBySpec(contractParam);
    }

    @RequestMapping(value = "/listCustomer", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ContractResult> listCustomer(@RequestBody(required = false) ContractParam contractParam) {
        customerId = contractParam.getPartyA();
        if (ToolUtil.isEmpty(contractParam)) {
            contractParam = new ContractParam();
        }
        return this.contractService.findPageBySpec(contractParam);
    }


}


