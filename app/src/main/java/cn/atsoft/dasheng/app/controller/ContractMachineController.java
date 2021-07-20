package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ContractMachine;
import cn.atsoft.dasheng.app.model.params.ContractMachineParam;
import cn.atsoft.dasheng.app.model.result.ContractMachineResult;
import cn.atsoft.dasheng.app.service.ContractMachineService;
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
 * 机床合同表控制器
 *
 * @author 
 * @Date 2021-07-20 13:34:41
 */
@RestController
@RequestMapping("/contractMachine")
@Api(tags = "机床合同表")
public class ContractMachineController extends BaseController {

    @Autowired
    private ContractMachineService contractMachineService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ContractMachineParam contractMachineParam) {
        this.contractMachineService.add(contractMachineParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ContractMachineParam contractMachineParam) {

        this.contractMachineService.update(contractMachineParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ContractMachineParam contractMachineParam)  {
        this.contractMachineService.delete(contractMachineParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ContractMachineResult> detail(@RequestBody ContractMachineParam contractMachineParam) {
        ContractMachine detail = this.contractMachineService.getById(contractMachineParam.getContractId());
        ContractMachineResult result = new ContractMachineResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-07-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ContractMachineResult> list(@RequestBody(required = false) ContractMachineParam contractMachineParam) {
        if(ToolUtil.isEmpty(contractMachineParam)){
            contractMachineParam = new ContractMachineParam();
        }
        return this.contractMachineService.findPageBySpec(contractMachineParam);
    }




}


