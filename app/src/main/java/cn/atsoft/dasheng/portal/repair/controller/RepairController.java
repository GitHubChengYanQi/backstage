package cn.atsoft.dasheng.portal.repair.controller;

import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.dispatching.model.params.DispatchingParam;
import cn.atsoft.dasheng.portal.repair.entity.Repair;
import cn.atsoft.dasheng.portal.repair.model.params.RepairParam;
import cn.atsoft.dasheng.portal.repair.model.result.RepairResult;
import cn.atsoft.dasheng.portal.repair.service.RepairService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.portal.repair.service.WxTemplate;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONPatch;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSONPatch.OperationType.add;


/**
 * 报修控制器
 *
 * @author siqiang
 * @Date 2021-08-20 17:11:20
 */
@RestController
@RequestMapping("/repair")
@Api(tags = "报修")
public class RepairController extends BaseController {

    @Autowired
    private RepairService repairService;
    @Autowired
    private WxTemplate wxTemplate;
    @Autowired
    private CustomerService customerService;

    /**
     * 新增接口
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RepairParam repairParam) {
        Repair add = this.repairService.add(repairParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RepairParam repairParam) {
        Repair update = this.repairService.update(repairParam);
        return ResponseData.success(update);
    }

    @RequestMapping(value = "/editdy", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData updatedynamic(@RequestBody RepairParam repairParam) {
        repairService.updatedynamic(repairParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/addWx", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addWx(String openid, String templateId, String page, RepairParam repairParam) {
        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        customerQueryWrapper.in("customer_id", repairParam.getCustomerId());
        List<Customer> customers = customerService.list(customerQueryWrapper);
        String repairName = null;
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(repairParam.getCustomerId())) {
                repairName = customer.getCustomerName();
            }
        }

        List<WxMaSubscribeMessage.MsgData> data = new ArrayList();

        data.add(new WxMaSubscribeMessage.MsgData("name", repairParam.getPeople()));
        data.add(new WxMaSubscribeMessage.MsgData("address", repairParam.getAddress()));
        String telephone = String.valueOf(repairParam.getTelephone());
        data.add(new WxMaSubscribeMessage.MsgData("phone", telephone));
        String reateTime = String.valueOf(repairParam.getCreateTime());
        DateTime parse = DateUtil.parse(reateTime);
        String time = String.valueOf(parse);
        data.add(new WxMaSubscribeMessage.MsgData("time", time));
        data.add(new WxMaSubscribeMessage.MsgData("repairName", repairName));
        wxTemplate.send(openid, templateId, page, data);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RepairParam repairParam) {
        this.repairService.delete(repairParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<RepairResult> detail(@RequestBody RepairParam repairParam) {
        Long result = repairParam.getRepairId();
        RepairResult repairResult = repairService.detail(result);
        return ResponseData.success(repairResult);
    }

    /**
     * 查询列表
     *
     * @author siqiang
     * @Date 2021-08-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RepairResult> list(@RequestBody(required = false) RepairParam repairParam) {
        if (ToolUtil.isEmpty(repairParam)) {
            repairParam = new RepairParam();
        }
        return this.repairService.findPageBySpec(repairParam);
    }


}


