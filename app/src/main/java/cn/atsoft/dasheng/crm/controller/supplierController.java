package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.params.CustomerParam;
import cn.atsoft.dasheng.app.model.result.CustomerIdRequest;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.app.wrapper.CustomerSelectWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 客户管理表控制器
 *
 * @author
 * @Date 2021-07-23 10:06:12
 */
@RestController
@RequestMapping("/supplier")
@Api(tags = "客户管理表")
public class supplierController extends BaseController {

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
    @Permission
    public ResponseData addItem(@RequestBody CustomerParam customerParam) {
        customerParam.setSupply(1);
        Customer add = this.customerService.add(customerParam);
        return ResponseData.success(add);
    }


    /**
     * 新增接口
     *
     * @author
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/enterprise", method = RequestMethod.GET)
    @ApiOperation("新增")
    public Customer enterprise() {
        return this.customerService.lambdaQuery().eq(Customer::getStatus, 99).one();
    }


    /**
     * 编辑接口`
     *
     * @author
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
    public ResponseData update(@RequestBody CustomerParam customerParam) {

        Customer result = this.customerService.update(customerParam);
        return ResponseData.success(result.getCustomerId());
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-07-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    @Permission
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
    @Permission
    public ResponseData detail(@RequestBody(required = false) CustomerParam customerParam) {
        if (ToolUtil.isNotEmpty(customerParam) && ToolUtil.isNotEmpty(customerParam.getCustomerId())) {
            Long customerId = customerParam.getCustomerId();
            CustomerResult detail = customerService.detail(customerId);
            return ResponseData.success(detail);
        } else {
            CustomerResult customerResult = new CustomerResult();
            Customer customer = customerService.query().eq("status", 99).eq("display", 1).one();
            if (ToolUtil.isNotEmpty(customer)) {
                customerResult = customerService.detail(customer.getCustomerId());
            } else {
                Customer entity = new Customer();
                entity.setStatus(99);
                entity.setSupply(99);
                this.customerService.save(entity);
                ToolUtil.copyProperties(entity, customerResult);
            }
            return ResponseData.success(customerResult);
        }

    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-23
     */

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo<CustomerResult> list(@RequestBody(required = false) CustomerParam customerParam) {
        if (ToolUtil.isEmpty(customerParam)) {
            customerParam = new CustomerParam();
        }

//        if (LoginContextHolder.getContext().isAdmin()) {
        return this.customerService.findPageBySpec(null, customerParam);
//        }else{
//            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
//            PageInfo<CustomerResult> customer= customerService.findPageBySpec(dataScope,customerParam);
//            return this.customerService.findPageBySpec(dataScope,customerParam);
//        }
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    @Permission
    public ResponseData listSelect(@RequestBody(required = false) CustomerParam customerParam) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper();
        queryWrapper.eq("display", 1);
        if (ToolUtil.isNotEmpty(customerParam)) {
            if (ToolUtil.isNotEmpty(customerParam.getSupply())) {
                queryWrapper.eq("supply", customerParam.getSupply());
            }
        }

        List<Map<String, Object>> list = this.customerService.listMaps(queryWrapper);
        CustomerSelectWrapper customerSelectWrapper = new CustomerSelectWrapper(list);
        List<Map<String, Object>> result = customerSelectWrapper.wrap();
        return ResponseData.success(result);
    }


    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("批量删除")
    @Permission
    public ResponseData batchDelete(@RequestBody CustomerIdRequest customerIdRequest) {
        customerService.batchDelete(customerIdRequest.getCustomerId());
        return ResponseData.success();
    }

    @RequestMapping(value = "/UpdateStatus", method = RequestMethod.POST)
    @ApiOperation("更新状态")
    @Permission
    public ResponseData UpdateStatus(@RequestBody CustomerParam CustomerParam) {
        customerService.updateStatus(CustomerParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/updateChargePerson", method = RequestMethod.POST)
    @ApiOperation("更新负责人")
    @Permission
    public ResponseData updateChargePerson(@RequestBody CustomerParam customerParam) {

        if (customerParam.getCustomerId() == null) {
            throw new ServiceException(500, "请选择你要的商机");
        }
        this.customerService.updateChargePerson(customerParam);
        return ResponseData.success();
    }
}


