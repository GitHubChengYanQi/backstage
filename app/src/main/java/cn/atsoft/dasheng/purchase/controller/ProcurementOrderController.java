package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrder;
import cn.atsoft.dasheng.purchase.model.params.ProcurementOrderParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementOrderResult;
import cn.atsoft.dasheng.purchase.service.ProcurementOrderService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 采购单控制器
 *
 * @author song
 * @Date 2022-01-13 11:50:15
 */
@RestController
@RequestMapping("/procurementOrder")
@Api(tags = "采购单")
public class ProcurementOrderController extends BaseController {

    @Autowired
    private ProcurementOrderService procurementOrderService;

    @Autowired
    private UserService userService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody @Valid ProcurementOrderParam procurementOrderParam) throws Exception {
        this.procurementOrderService.add(procurementOrderParam);
        return ResponseData.success();
    }

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/addOrder", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addOrder(@RequestBody ProcurementOrderParam procurementOrderParam) throws Exception {
        Long addOrderId = this.procurementOrderService.addOrder(procurementOrderParam);
        return ResponseData.success(addOrderId);
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProcurementOrderParam procurementOrderParam) {

        this.procurementOrderService.update(procurementOrderParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ProcurementOrderParam procurementOrderParam) {
        this.procurementOrderService.delete(procurementOrderParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ProcurementOrderParam procurementOrderParam) {
        ProcurementOrder detail = this.procurementOrderService.getById(procurementOrderParam.getProcurementOrderId());
        ProcurementOrderResult result = new ProcurementOrderResult();
        ToolUtil.copyProperties(detail, result);

        if (ToolUtil.isNotEmpty(detail) && ToolUtil.isNotEmpty(detail.getCreateUser())){
            User user = userService.getById(detail.getCreateUser());
            result.setUser(user);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ProcurementOrderResult> list(@RequestBody(required = false) ProcurementOrderParam procurementOrderParam) {
        if (ToolUtil.isEmpty(procurementOrderParam)) {
            procurementOrderParam = new ProcurementOrderParam();
        }
        return this.procurementOrderService.findPageBySpec(procurementOrderParam);
    }


}


