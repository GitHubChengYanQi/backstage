package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.wrapper.OrderSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.OutboundSelectWrapper;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Outbound;
import cn.atsoft.dasheng.app.model.params.OutboundParam;
import cn.atsoft.dasheng.app.model.result.OutboundResult;
import cn.atsoft.dasheng.app.service.OutboundService;
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
 * 出库表控制器
 *
 * @author 
 * @Date 2021-07-15 17:41:40
 */
@RestController
@RequestMapping("/outbound")
@Api(tags = "出库表")
public class OutboundController extends BaseController {

    @Autowired
    private OutboundService outboundService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OutboundParam outboundParam) {
        this.outboundService.add(outboundParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody OutboundParam outboundParam) {

        this.outboundService.update(outboundParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OutboundParam outboundParam)  {
        this.outboundService.delete(outboundParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<OutboundResult> detail(@RequestBody OutboundParam outboundParam) {
        Outbound detail = this.outboundService.getById(outboundParam.getOutboundId());
        OutboundResult result = new OutboundResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OutboundResult> list(@RequestBody(required = false) OutboundParam outboundParam) {
        if(ToolUtil.isEmpty(outboundParam)){
            outboundParam = new OutboundParam();
        }
        return this.outboundService.findPageBySpec(outboundParam);
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)

    public ResponseData<List<Map<String, Object>>> listSelect() {
        List<Map<String, Object>> list = this.outboundService.listMaps();

        OrderSelectWrapper orderSelectWrapper = new OrderSelectWrapper(list);
        OutboundSelectWrapper outboundSelectWrapper = new OutboundSelectWrapper(list);
        List<Map<String, Object>> result = outboundSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}


