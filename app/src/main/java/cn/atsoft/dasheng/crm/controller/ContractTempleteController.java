package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContractTemplete;
import cn.atsoft.dasheng.crm.model.params.ContractTempleteParam;
import cn.atsoft.dasheng.crm.model.result.ContractTempleteResult;
import cn.atsoft.dasheng.crm.service.ContractTempleteService;
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
 * 自定义合同变量控制器
 *
 * @author Captain_Jazz
 * @Date 2022-04-18 13:31:18
 */
@RestController
@RequestMapping("/contractTemplete")
@Api(tags = "自定义合同变量")
public class ContractTempleteController extends BaseController {

    @Autowired
    private ContractTempleteService contractTempleteService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ContractTempleteParam contractTempleteParam) {

        return ResponseData.success(this.contractTempleteService.add(contractTempleteParam));
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ContractTempleteParam contractTempleteParam) {

        this.contractTempleteService.update(contractTempleteParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ContractTempleteParam contractTempleteParam)  {
        this.contractTempleteService.delete(contractTempleteParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<ContractTempleteResult> detail(@RequestBody ContractTempleteParam contractTempleteParam) {
        ContractTemplete detail = this.contractTempleteService.getById(contractTempleteParam.getContractTemplateId());
        ContractTempleteResult result = new ContractTempleteResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ContractTempleteResult> list(@RequestBody(required = false) ContractTempleteParam contractTempleteParam) {
        if(ToolUtil.isEmpty(contractTempleteParam)){
            contractTempleteParam = new ContractTempleteParam();
        }
        return this.contractTempleteService.findPageBySpec(contractTempleteParam);
    }




}


