package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityPlanDetail;
import cn.atsoft.dasheng.erp.model.params.QualityPlanDetailParam;
import cn.atsoft.dasheng.erp.model.params.QualityPlanParam;
import cn.atsoft.dasheng.erp.model.params.TypeRequest;
import cn.atsoft.dasheng.erp.model.result.QualityPlanDetailResult;
import cn.atsoft.dasheng.erp.service.QualityPlanDetailService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 质检方案详情控制器
 *
 * @author Captain_Jazz
 * @Date 2021-10-28 10:29:56
 */
@RestController
@RequestMapping("/qualityPlanDetail")
@Api(tags = "质检方案详情")
public class QualityPlanDetailController extends BaseController {

    @Autowired
    private QualityPlanDetailService qualityPlanDetailService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody QualityPlanDetailParam qualityPlanDetailParam) {
        this.qualityPlanDetailService.add(qualityPlanDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改质检方案详情", key = "name", dict = QualityPlanDetailParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody QualityPlanDetailParam qualityPlanDetailParam) {

        this.qualityPlanDetailService.update(qualityPlanDetailParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除质检方案详情", key = "name", dict = QualityPlanDetailParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody QualityPlanDetailParam qualityPlanDetailParam) {
        this.qualityPlanDetailService.delete(qualityPlanDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody QualityPlanDetailParam qualityPlanDetailParam) {
        QualityPlanDetail detail = this.qualityPlanDetailService.getById(qualityPlanDetailParam.getPlanDetailId());


        QualityPlanDetailResult result = new QualityPlanDetailResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<QualityPlanDetailResult> list(@RequestBody(required = false) QualityPlanDetailParam qualityPlanDetailParam) {
        if (ToolUtil.isEmpty(qualityPlanDetailParam)) {
            qualityPlanDetailParam = new QualityPlanDetailParam();
        }
        return this.qualityPlanDetailService.findPageBySpec(qualityPlanDetailParam);
    }


}


