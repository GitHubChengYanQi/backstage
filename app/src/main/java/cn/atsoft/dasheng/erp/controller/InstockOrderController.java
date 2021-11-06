package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockOrder;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.params.InstockOrderParam;
import cn.atsoft.dasheng.erp.model.result.InstockOrderResult;
import cn.atsoft.dasheng.erp.service.InstockOrderService;
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
 * 入库单控制器
 *
 * @author song
 * @Date 2021-10-06 09:43:44
 */
@RestController
@RequestMapping("/instockOrder")
@Api(tags = "入库单")
public class InstockOrderController extends BaseController {

    @Autowired
    private InstockOrderService instockOrderService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InstockOrderParam instockOrderParam) {
        this.instockOrderService.add(instockOrderParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改入库单", key = "name", dict = InstockOrderParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody InstockOrderParam instockOrderParam) {

        this.instockOrderService.update(instockOrderParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除入库单", key = "name", dict = InstockOrderParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody InstockOrderParam instockOrderParam)  {
        this.instockOrderService.delete(instockOrderParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<InstockOrderResult> detail(@RequestBody InstockOrderParam instockOrderParam) {
        InstockOrder detail = this.instockOrderService.getById(instockOrderParam.getInstockOrderId());
        InstockOrderResult result = new InstockOrderResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-06
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InstockOrderResult> list(@RequestBody(required = false) InstockOrderParam instockOrderParam) {
        if(ToolUtil.isEmpty(instockOrderParam)){
            instockOrderParam = new InstockOrderParam();
        }
        return this.instockOrderService.findPageBySpec(instockOrderParam);
    }




}


