package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockReceipt;
import cn.atsoft.dasheng.erp.model.params.InstockReceiptParam;
import cn.atsoft.dasheng.erp.model.result.InstockReceiptResult;
import cn.atsoft.dasheng.erp.service.InstockReceiptService;
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
 * 入库记录单控制器
 *
 * @author song
 * @Date 2022-08-11 09:28:12
 */
@RestController
@RequestMapping("/instockReceipt")
@Api(tags = "入库记录单")
public class InstockReceiptController extends BaseController {

    @Autowired
    private InstockReceiptService instockReceiptService;

//    /**
//     * 新增接口
//     *
//     * @author song
//     * @Date 2022-08-11
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addItem(@RequestBody InstockReceiptParam instockReceiptParam) {
//        this.instockReceiptService.add(instockReceiptParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 编辑接口
//     *
//     * @author song
//     * @Date 2022-08-11
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody InstockReceiptParam instockReceiptParam) {
//
//        this.instockReceiptService.update(instockReceiptParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author song
//     * @Date 2022-08-11
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody InstockReceiptParam instockReceiptParam)  {
//        this.instockReceiptService.delete(instockReceiptParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<InstockReceiptResult> detail(@RequestBody InstockReceiptParam instockReceiptParam) {
        InstockReceiptResult result = this.instockReceiptService.detail(instockReceiptParam.getReceiptId());
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-08-11
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InstockReceiptResult> list(@RequestBody(required = false) InstockReceiptParam instockReceiptParam) {
        if(ToolUtil.isEmpty(instockReceiptParam)){
            instockReceiptParam = new InstockReceiptParam();
        }
        return this.instockReceiptService.findPageBySpec(instockReceiptParam);
    }




}


