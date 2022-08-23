package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContractTempleteDetail;
import cn.atsoft.dasheng.crm.model.params.ContractTempleteDetailParam;
import cn.atsoft.dasheng.crm.model.result.ContractTempleteDetailResult;
import cn.atsoft.dasheng.crm.service.ContractTempleteDetailService;
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
@RequestMapping("/contractTempleteDetail")
@Api(tags = "自定义合同变量")
public class ContractTempleteDetailController extends BaseController {

    @Autowired
    private ContractTempleteDetailService contractTempleteDetailService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ContractTempleteDetailParam contractTempleteDetailParam) {
        this.contractTempleteDetailService.add(contractTempleteDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ContractTempleteDetailParam contractTempleteDetailParam) {

        this.contractTempleteDetailService.update(contractTempleteDetailParam);
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
    public ResponseData delete(@RequestBody ContractTempleteDetailParam contractTempleteDetailParam)  {
        this.contractTempleteDetailService.delete(contractTempleteDetailParam);
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
    public ResponseData detail(@RequestBody ContractTempleteDetailParam contractTempleteDetailParam) {
        ContractTempleteDetail detail = this.contractTempleteDetailService.getById(contractTempleteDetailParam.getContractTempleteDetailId());
        ContractTempleteDetailResult result = new ContractTempleteDetailResult();
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
    public PageInfo<ContractTempleteDetailResult> list(@RequestBody(required = false) ContractTempleteDetailParam contractTempleteDetailParam) {
        if(ToolUtil.isEmpty(contractTempleteDetailParam)){
            contractTempleteDetailParam = new ContractTempleteDetailParam();
        }
        return this.contractTempleteDetailService.findPageBySpec(contractTempleteDetailParam);
    }




}


