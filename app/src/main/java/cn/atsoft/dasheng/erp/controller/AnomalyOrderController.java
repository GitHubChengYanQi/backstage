package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AnomalyOrder;
import cn.atsoft.dasheng.erp.model.params.AnomalyOrderParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyOrderResult;
import cn.atsoft.dasheng.erp.service.AnomalyOrderService;
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
 * @Date 2022-06-09 15:38:59
 */
@RestController
@RequestMapping("/anomalyOrder")
@Api(tags = "")
public class AnomalyOrderController extends BaseController {

    @Autowired
    private AnomalyOrderService anomalyOrderService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-06-09
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody AnomalyOrderParam anomalyOrderParam) {
        this.anomalyOrderService.add(anomalyOrderParam);
        return ResponseData.success();
    }


    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ApiOperation("异常单据提交")
    public ResponseData submit(@RequestBody AnomalyOrderParam anomalyOrderParam) {
        this.anomalyOrderService.submit(anomalyOrderParam);
        return ResponseData.success();
    }
//    /**
//     * 编辑接口
//     *
//     * @author song
//     * @Date 2022-06-09
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody AnomalyOrderParam anomalyOrderParam) {
//
//        this.anomalyOrderService.update(anomalyOrderParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author song
//     * @Date 2022-06-09
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody AnomalyOrderParam anomalyOrderParam)  {
//        this.anomalyOrderService.delete(anomalyOrderParam);
//        return ResponseData.success();
//    }
//

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-06-09
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<AnomalyOrderResult> detail(@RequestBody AnomalyOrderParam anomalyOrderParam) {
        AnomalyOrder detail = this.anomalyOrderService.getById(anomalyOrderParam.getOrderId());
        AnomalyOrderResult result = new AnomalyOrderResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-06-09
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<AnomalyOrderResult> list(@RequestBody(required = false) AnomalyOrderParam anomalyOrderParam) {
        if (ToolUtil.isEmpty(anomalyOrderParam)) {
            anomalyOrderParam = new AnomalyOrderParam();
        }
        return this.anomalyOrderService.findPageBySpec(anomalyOrderParam);
    }


}


