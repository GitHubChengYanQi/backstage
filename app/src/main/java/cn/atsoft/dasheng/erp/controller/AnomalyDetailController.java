package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AnomalyDetail;
import cn.atsoft.dasheng.erp.model.params.AnomalyDetailParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyDetailResult;
import cn.atsoft.dasheng.erp.service.AnomalyDetailService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
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
 * 控制器
 *
 * @author song
 * @Date 2022-05-27 15:30:57
 */
@RestController
@RequestMapping("/anomalyDetail")
@Api(tags = "")
public class AnomalyDetailController extends BaseController {

    @Autowired
    private AnomalyDetailService anomalyDetailService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-05-27
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody AnomalyDetailParam anomalyDetailParam) {
        this.anomalyDetailService.add(anomalyDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-05-27
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody @Valid AnomalyDetailParam anomalyDetailParam) {

        this.anomalyDetailService.update(anomalyDetailParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-05-27
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody AnomalyDetailParam anomalyDetailParam) {
        this.anomalyDetailService.delete(anomalyDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-05-27
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody AnomalyDetailParam anomalyDetailParam) {
        AnomalyDetail detail = this.anomalyDetailService.getById(anomalyDetailParam.getDetailId());
        AnomalyDetailResult result = new AnomalyDetailResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-05-27
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<AnomalyDetailResult> list(@RequestBody(required = false) AnomalyDetailParam anomalyDetailParam) {
        if (ToolUtil.isEmpty(anomalyDetailParam)) {
            anomalyDetailParam = new AnomalyDetailParam();
        }
        return this.anomalyDetailService.findPageBySpec(anomalyDetailParam);
    }


}


