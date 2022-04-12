package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Anomaly;
import cn.atsoft.dasheng.erp.model.params.AnomalyParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyResult;
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
 * 异常单据控制器
 *
 * @author song
 * @Date 2022-04-12 09:42:31
 */
@RestController
@RequestMapping("/anomaly")
@Api(tags = "异常单据")
public class AnomalyController extends BaseController {

    @Autowired
    private AnomalyService anomalyService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-04-12
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody @Valid AnomalyParam anomalyParam) {
        if (ToolUtil.isEmpty(anomalyParam.getAnomalyType())) {
            throw new ServiceException(500, "请穿入单据类型 ");
        }

        this.anomalyService.add(anomalyParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-04-12
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody AnomalyParam anomalyParam) {

        this.anomalyService.update(anomalyParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-04-12
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody AnomalyParam anomalyParam) {
        this.anomalyService.delete(anomalyParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-04-12
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<AnomalyResult> detail(@RequestBody AnomalyParam anomalyParam) {
        Anomaly detail = this.anomalyService.getById(anomalyParam.getAnomalyId());
        AnomalyResult result = new AnomalyResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-04-12
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


