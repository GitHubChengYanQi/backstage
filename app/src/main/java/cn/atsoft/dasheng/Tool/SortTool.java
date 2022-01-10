package cn.atsoft.dasheng.Tool;

import cn.atsoft.dasheng.Tool.pojo.UpdateSort;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sortUpdate")
public class SortTool {
    @Autowired
    private CustomerService customerService;


    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    private ResponseData update(@Valid @RequestBody UpdateSort sort) {

        switch (sort.getType()) {
            case "customer":
                List<Customer> customerList = new ArrayList<>();
                for (UpdateSort.SortParam sortParam : sort.getSortParams()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(sortParam.getId());
                    customer.setSort(sortParam.getSort());
                    customerList.add(customer);
                }
                customerService.updateBatchById(customerList);
                break;
        }

        return ResponseData.success();
    }

}
