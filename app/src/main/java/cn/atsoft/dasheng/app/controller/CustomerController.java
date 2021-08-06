package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.CustomerSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.params.CustomerParam;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 客户管理表控制器
 *
 * @author
 * @Date 2021-07-23 10:06:12
 */
@RestController
@RequestMapping("/customer")
@Api(tags = "客户管理表")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CustomerParam customerParam) {
        Long add = this.customerService.add(customerParam);
        System.err.println(customerParam+"---------------------------------------------------------------");
        return ResponseData.success(add);


    }



    /**
     * 编辑接口`
     *
     * @author
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CustomerParam customerParam) {

        this.customerService.update(customerParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CustomerParam customerParam) {
        this.customerService.delete(customerParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CustomerResult> detail(@RequestBody CustomerParam customerParam) {
        CustomerResult bySpec = customerService.findBySpec(customerParam);

        return ResponseData.success(bySpec);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-23
     */

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CustomerResult> list(@RequestBody(required = false) CustomerParam customerParam) {
        if (ToolUtil.isEmpty(customerParam)) {
            customerParam = new CustomerParam();
        }

        return this.customerService.findPageBySpec(customerParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect() {
        List<Map<String, Object>> list = this.customerService.listMaps();
        CustomerSelectWrapper customerSelectWrapper = new CustomerSelectWrapper(list);
        List<Map<String, Object>> result = customerSelectWrapper.wrap();
        return ResponseData.success(result);
    }


    @RequestMapping(value = "/Batchdelete", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CustomerResult>Batchdelete(@RequestBody(required = false)   List<Long> id) {


    }
}


