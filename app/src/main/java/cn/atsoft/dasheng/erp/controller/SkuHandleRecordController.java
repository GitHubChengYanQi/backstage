package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SkuHandleRecord;
import cn.atsoft.dasheng.erp.model.params.SkuHandleRecordParam;
import cn.atsoft.dasheng.erp.model.result.SkuHandleRecordResult;
import cn.atsoft.dasheng.erp.service.SkuHandleRecordService;
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
 * sku任务操作记录控制器
 *
 * @author 
 * @Date 2022-10-25 10:48:41
 */
@RestController
@RequestMapping("/skuHandleRecord")
@Api(tags = "sku任务操作记录")
public class SkuHandleRecordController extends BaseController {

    @Autowired
    private SkuHandleRecordService skuHandleRecordService;

//    /**
//     * 新增接口
//     *
//     * @author
//     * @Date 2022-10-25
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addItem(@RequestBody SkuHandleRecordParam skuHandleRecordParam) {
//        this.skuHandleRecordService.add(skuHandleRecordParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 编辑接口
//     *
//     * @author
//     * @Date 2022-10-25
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody SkuHandleRecordParam skuHandleRecordParam) {
//
//        this.skuHandleRecordService.update(skuHandleRecordParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author
//     * @Date 2022-10-25
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody SkuHandleRecordParam skuHandleRecordParam)  {
//        this.skuHandleRecordService.delete(skuHandleRecordParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2022-10-25
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<SkuHandleRecordResult> detail(@RequestBody SkuHandleRecordParam skuHandleRecordParam) {
        SkuHandleRecord detail = this.skuHandleRecordService.getById(skuHandleRecordParam.getRecordId());
        SkuHandleRecordResult result = new SkuHandleRecordResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2022-10-25
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SkuHandleRecordResult> list(@RequestBody(required = false) SkuHandleRecordParam skuHandleRecordParam) {
        if(ToolUtil.isEmpty(skuHandleRecordParam)){
            skuHandleRecordParam = new SkuHandleRecordParam();
        }
        return this.skuHandleRecordService.findPageBySpec(skuHandleRecordParam);
    }

}


