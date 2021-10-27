package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityCheck;
import cn.atsoft.dasheng.erp.model.params.QualityCheckParam;
import cn.atsoft.dasheng.erp.model.result.QualityCheckResult;
import cn.atsoft.dasheng.erp.service.QualityCheckService;
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
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody QualityCheckParam qualityCheckParam)  {
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
    public ResponseData<QualityCheckResult> detail(@RequestBody QualityCheckParam qualityCheckParam) {
        QualityCheck detail = this.qualityCheckService.getById(qualityCheckParam.getQualityCheckId());
        QualityCheckResult result = new QualityCheckResult();
        ToolUtil.copyProperties(detail, result);

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
    public PageInfo<QualityCheckResult> list(@RequestBody(required = false) QualityCheckParam qualityCheckParam) {
        if(ToolUtil.isEmpty(qualityCheckParam)){
            qualityCheckParam = new QualityCheckParam();
        }
        return this.qualityCheckService.findPageBySpec(qualityCheckParam);
    }




}


