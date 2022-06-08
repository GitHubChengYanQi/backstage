package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AnomalyBind;
import cn.atsoft.dasheng.erp.model.params.AnomalyBindParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyBindResult;
import cn.atsoft.dasheng.erp.service.AnomalyBindService;
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
 * 异常生成的实物绑定控制器
 *
 * @author song
 * @Date 2022-05-28 13:13:34
 */
@RestController
@RequestMapping("/anomalyBind")
@Api(tags = "异常生成的实物绑定")
public class AnomalyBindController extends BaseController {

    @Autowired
    private AnomalyBindService anomalyBindService;

//    /**
//     * 新增接口
//     *
//     * @author song
//     * @Date 2022-05-28
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addItem(@RequestBody AnomalyBindParam anomalyBindParam) {
//        this.anomalyBindService.add(anomalyBindParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 编辑接口
//     *
//     * @author song
//     * @Date 2022-05-28
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody AnomalyBindParam anomalyBindParam) {
//
//        this.anomalyBindService.update(anomalyBindParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author song
//     * @Date 2022-05-28
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody AnomalyBindParam anomalyBindParam)  {
//        this.anomalyBindService.delete(anomalyBindParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 查看详情接口
//     *
//     * @author song
//     * @Date 2022-05-28
//     */
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData<AnomalyBindResult> detail(@RequestBody AnomalyBindParam anomalyBindParam) {
//        AnomalyBind detail = this.anomalyBindService.getById(anomalyBindParam.getBindId());
//        AnomalyBindResult result = new AnomalyBindResult();
//        ToolUtil.copyProperties(detail, result);
//        return ResponseData.success(result);
//    }
//
//    /**
//     * 查询列表
//     *
//     * @author song
//     * @Date 2022-05-28
//     */
//    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    @ApiOperation("列表")
//    public PageInfo<AnomalyBindResult> list(@RequestBody(required = false) AnomalyBindParam anomalyBindParam) {
//        if(ToolUtil.isEmpty(anomalyBindParam)){
//            anomalyBindParam = new AnomalyBindParam();
//        }
//        return this.anomalyBindService.findPageBySpec(anomalyBindParam);
//    }
//



}


