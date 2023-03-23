package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityCheck;
import cn.atsoft.dasheng.erp.entity.QualityCheckClassification;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.model.params.QualityCheckClassificationParam;
import cn.atsoft.dasheng.erp.model.params.QualityCheckParam;
import cn.atsoft.dasheng.erp.model.result.QualityCheckClassificationResult;
import cn.atsoft.dasheng.erp.model.result.QualityCheckResult;
import cn.atsoft.dasheng.erp.model.result.ToolResult;
import cn.atsoft.dasheng.erp.service.QualityCheckClassificationService;
import cn.atsoft.dasheng.erp.service.QualityCheckService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.ToolService;
import cn.atsoft.dasheng.erp.wrapper.QualityCheckClassificationSelectWrapper;
import cn.atsoft.dasheng.erp.wrapper.QualityCheckSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 质检表控制器
 *
 * @author song
 * @Date 2021-10-27 13:08:57
 */
@RestController
@RequestMapping("/qualityCheck")
@Api(tags = "质检表")
public class QualityCheckController extends BaseController {

    @Autowired
    private QualityCheckService qualityCheckService;
    @Autowired
    private ToolService toolService;
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
    public ResponseData addItem(@RequestBody QualityCheckParam qualityCheckParam) {
        this.qualityCheckService.add(qualityCheckParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-27
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改质检", key = "name", dict = QualityCheckParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody QualityCheckParam qualityCheckParam) {

        this.qualityCheckService.update(qualityCheckParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-27
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除质检", key = "name", dict = QualityCheckParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody QualityCheckParam qualityCheckParam) {
        this.qualityCheckService.delete(qualityCheckParam);
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
    public ResponseData detail(@RequestBody QualityCheckParam qualityCheckParam) {
        QualityCheck detail = this.qualityCheckService.getById(qualityCheckParam.getQualityCheckId());

        if (ToolUtil.isNotEmpty(detail)){
            JSONArray jsonArray = new JSONArray();
            if (ToolUtil.isEmpty(detail.getTool()) || detail.getTool().equals("null") ){
                jsonArray = JSONUtil.parseArray(detail.getTool());
            }
            List<Long> longs = JSONUtil.toList(jsonArray, Long.class);
            List<Tool> tools = longs.size() > 0 ? toolService.query().in("tool_id", longs).list() : new ArrayList<>();

            QualityCheckClassification qualityCheckClassification = qualityCheckClassificationService.query()
                    .eq("quality_check_classification_id", detail.getQualityCheckClassificationId())
                    .one();

            QualityCheckClassificationResult qualityCheckClassificationResult = new QualityCheckClassificationResult();
            if (ToolUtil.isNotEmpty(qualityCheckClassification)) {
                ToolUtil.copyProperties(qualityCheckClassification, qualityCheckClassificationResult);
            }

            List<ToolResult> toolResults = new ArrayList<>();
            for (Tool tool : tools) {
                ToolResult toolResult = new ToolResult();
                ToolUtil.copyProperties(tool, toolResult);
                toolResults.add(toolResult);
            }
            QualityCheckResult result = new QualityCheckResult();
            if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
            result.setTools(toolResults);
            result.setQualityCheckClassificationResult(qualityCheckClassificationResult);
            return ResponseData.success(result);
        }else {
            return null;
        }

    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-27
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<QualityCheckResult> list(@RequestBody(required = false) QualityCheckParam qualityCheckParam) {
        if (ToolUtil.isEmpty(qualityCheckParam)) {
            qualityCheckParam = new QualityCheckParam();
        }
        return this.qualityCheckService.findPageBySpec(qualityCheckParam);
    }

    /**
     * 选择列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(@RequestBody(required = false) QualityCheckParam qualityCheckParam) {

        QueryWrapper<QualityCheck> qualityCheckQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(qualityCheckParam)) {
            if (ToolUtil.isNotEmpty(qualityCheckParam.getQualityCheckClassificationId())) {
                qualityCheckQueryWrapper.in("quality_check_classification_id", qualityCheckParam.getQualityCheckClassificationId());
            }
        }
        List<Map<String, Object>> list = this.qualityCheckService.listMaps(qualityCheckQueryWrapper);
        QualityCheckSelectWrapper factory = new QualityCheckSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}


