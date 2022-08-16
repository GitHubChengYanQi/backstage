package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Anomaly;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyResult;
import cn.atsoft.dasheng.erp.pojo.AnomalyCensus;
import cn.atsoft.dasheng.erp.service.AnomalyService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
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
@RequestMapping("/anomaly")
@Api(tags = "")
public class AnomalyController extends BaseController {

    @Autowired
    private AnomalyService anomalyService;


    @RequestMapping(value = "/temporary", method = RequestMethod.POST)
    @ApiOperation("暂存")
    public ResponseData temporary(@RequestBody @Valid AnomalyParam anomalyParam) {
        Long id = this.anomalyService.temporary(anomalyParam);
        return ResponseData.success(id);
    }

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-05-27
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody @Valid AnomalyParam anomalyParam) {
        Anomaly anomaly = this.anomalyService.add(anomalyParam);
        return ResponseData.success(anomaly);
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-05-27
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody AnomalyParam anomalyParam) {
         Anomaly update = this.anomalyService.update(anomalyParam);
        return ResponseData.success(update);
    }


    @RequestMapping(value = "/anomalyCensus", method = RequestMethod.POST)
    public ResponseData anomalyCensus(@RequestBody AnomalyParam anomalyParam) {
        List<AnomalyCensus> anomalyCensuses = anomalyService.anomalyCensus(anomalyParam);
        return ResponseData.success(anomalyCensuses);
    }


    @RequestMapping(value = "/detailed", method = RequestMethod.POST)
    public ResponseData detailed(@RequestBody AnomalyParam anomalyParam) {
        Map<Integer, List<AnomalyResult>> detailed = anomalyService.detailed(anomalyParam);
        return ResponseData.success(detailed);
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-05-27
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody AnomalyParam anomalyParam) {
        if (ToolUtil.isEmpty(anomalyParam.getAnomalyId())) {
            throw new ServiceException(500, "缺少参数");
        }
        this.anomalyService.delete(anomalyParam);
        return ResponseData.success();
    }


    @RequestMapping(value = "/dealWithError", method = RequestMethod.POST)
    @ApiOperation("处理")
    public ResponseData dealWithError(@RequestBody AnomalyParam anomalyParam) {
        this.anomalyService.dealWithError(anomalyParam);
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
    public ResponseData<AnomalyResult> detail(@RequestBody AnomalyParam anomalyParam) {
        AnomalyResult result = this.anomalyService.detail(anomalyParam.getAnomalyId());
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
    public PageInfo<AnomalyResult> list(@RequestBody(required = false) AnomalyParam anomalyParam) {
        if (ToolUtil.isEmpty(anomalyParam)) {
            anomalyParam = new AnomalyParam();
        }
        return this.anomalyService.findPageBySpec(anomalyParam);
    }


}


