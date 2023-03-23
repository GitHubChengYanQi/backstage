package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityCheckClassification;
import cn.atsoft.dasheng.erp.model.params.ProductOrderDetailsParam;
import cn.atsoft.dasheng.erp.model.params.QualityCheckClassificationParam;
import cn.atsoft.dasheng.erp.model.result.QualityCheckClassificationResult;
import cn.atsoft.dasheng.erp.service.QualityCheckClassificationService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.wrapper.QualityCheckClassificationSelectWrapper;
import cn.atsoft.dasheng.erp.wrapper.ToolSelectWrapper;
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
 * 质检分类表控制器
 *
 * @author song
 * @Date 2021-10-27 13:08:57
 */
@RestController
@RequestMapping("/qualityCheckClassification")
@Api(tags = "质检分类表")
public class QualityCheckClassificationController extends BaseController {

    @Autowired
    private QualityCheckClassificationService qualityCheckClassificationService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-27
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody QualityCheckClassificationParam qualityCheckClassificationParam) {
        this.qualityCheckClassificationService.add(qualityCheckClassificationParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-27
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改质检分类", key = "name", dict = QualityCheckClassificationParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody QualityCheckClassificationParam qualityCheckClassificationParam) {

        this.qualityCheckClassificationService.update(qualityCheckClassificationParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-27
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除质检分类", key = "name", dict = QualityCheckClassificationParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody QualityCheckClassificationParam qualityCheckClassificationParam) {
        this.qualityCheckClassificationService.delete(qualityCheckClassificationParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-27
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody QualityCheckClassificationParam qualityCheckClassificationParam) {
        QualityCheckClassification detail = this.qualityCheckClassificationService.getById(qualityCheckClassificationParam.getQualityCheckClassificationId());
        QualityCheckClassificationResult result = new QualityCheckClassificationResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-27
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<QualityCheckClassificationResult> list(@RequestBody(required = false) QualityCheckClassificationParam qualityCheckClassificationParam) {
        if (ToolUtil.isEmpty(qualityCheckClassificationParam)) {
            qualityCheckClassificationParam = new QualityCheckClassificationParam();
        }
        return this.qualityCheckClassificationService.findPageBySpec(qualityCheckClassificationParam);
    }

    /**
     * 选择列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {

        List<Map<String, Object>> list = this.qualityCheckClassificationService.listMaps();
        QualityCheckClassificationSelectWrapper factory = new QualityCheckClassificationSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}


