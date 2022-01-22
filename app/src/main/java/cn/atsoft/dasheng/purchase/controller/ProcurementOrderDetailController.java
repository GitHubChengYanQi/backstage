package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.ProcurementOrderDetail;
import cn.atsoft.dasheng.purchase.model.params.ProcurementOrderDetailParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementOrderDetailResult;
import cn.atsoft.dasheng.purchase.service.ProcurementOrderDetailService;
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
 * 控制器
 *
 * @author song
 * @Date 2022-01-13 11:50:15
 */
@RestController
@RequestMapping("/procurementOrderDetail")
@Api(tags = "")
public class ProcurementOrderDetailController extends BaseController {

    @Autowired
    private ProcurementOrderDetailService procurementOrderDetailService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ProcurementOrderDetailParam procurementOrderDetailParam) {
        this.procurementOrderDetailService.add(procurementOrderDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-01-13
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ProcurementOrderDetailParam procurementOrderDetailParam) {

        this.procurementOrderDetailService.update(procurementOrderDetailParam);
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
    public ResponseData delete(@RequestBody ProcurementOrderDetailParam procurementOrderDetailParam)  {
        this.procurementOrderDetailService.delete(procurementOrderDetailParam);
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
    public ResponseData<ProcurementOrderDetailResult> detail(@RequestBody ProcurementOrderDetailParam procurementOrderDetailParam) {
        ProcurementOrderDetail detail = this.procurementOrderDetailService.getById(procurementOrderDetailParam.getOrderDetailId());
        ProcurementOrderDetailResult result = new ProcurementOrderDetailResult();
        ToolUtil.copyProperties(detail, result);

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
    public PageInfo<ProcurementOrderDetailResult> list(@RequestBody(required = false) ProcurementOrderDetailParam procurementOrderDetailParam) {
        if(ToolUtil.isEmpty(procurementOrderDetailParam)){
            procurementOrderDetailParam = new ProcurementOrderDetailParam();
        }
        return this.procurementOrderDetailService.findPageBySpec(procurementOrderDetailParam);
    }




}

