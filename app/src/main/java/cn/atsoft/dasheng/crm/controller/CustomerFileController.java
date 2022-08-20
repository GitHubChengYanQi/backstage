package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.CustomerFile;
import cn.atsoft.dasheng.crm.model.params.CustomerFileParam;
import cn.atsoft.dasheng.crm.model.result.CustomerFileResult;
import cn.atsoft.dasheng.crm.service.CustomerFileService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 控制器
 *
 * @author 
 * @Date 2021-09-08 14:15:26
 */
@RestController
@RequestMapping("/customerFile")
@Api(tags = "")
public class CustomerFileController extends BaseController {

    @Autowired
    private CustomerFileService customerFileService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-09-08
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CustomerFileParam customerFileParam) {
        this.customerFileService.add(customerFileParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-09-08
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CustomerFileParam customerFileParam) {

        this.customerFileService.update(customerFileParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-09-08
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CustomerFileParam customerFileParam)  {
        this.customerFileService.delete(customerFileParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-09-08
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody CustomerFileParam customerFileParam) {
        CustomerFile detail = this.customerFileService.getById(customerFileParam.getFileId());
        CustomerFileResult result = new CustomerFileResult();
        ToolUtil.copyProperties(detail, result);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-09-08
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData list(@RequestBody(required = false) CustomerFileParam customerFileParam) {
        if(ToolUtil.isEmpty(customerFileParam)){
            customerFileParam = new CustomerFileParam();
        }
        PageInfo<CustomerFileResult> pageBySpec = customerFileService.findPageBySpec(customerFileParam);
        return ResponseData.success(pageBySpec);
    }




}


