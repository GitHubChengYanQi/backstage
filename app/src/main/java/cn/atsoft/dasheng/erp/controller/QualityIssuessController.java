package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityIssuess;
import cn.atsoft.dasheng.erp.model.params.QualityIssuessParam;
import cn.atsoft.dasheng.erp.model.result.QualityIssuessResult;
import cn.atsoft.dasheng.erp.service.QualityIssuessService;
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
 * 质检事项表控制器
 *
 * @author 
 * @Date 2021-11-15 13:44:25
 */
@RestController
@RequestMapping("/qualityIssuess")
@Api(tags = "质检事项表")
public class QualityIssuessController extends BaseController {

    @Autowired
    private QualityIssuessService qualityIssuessService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-11-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody QualityIssuessParam qualityIssuessParam) {
        this.qualityIssuessService.add(qualityIssuessParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-11-15
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody QualityIssuessParam qualityIssuessParam) {

        this.qualityIssuessService.update(qualityIssuessParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-11-15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody QualityIssuessParam qualityIssuessParam)  {
        this.qualityIssuessService.delete(qualityIssuessParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-11-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody QualityIssuessParam qualityIssuessParam) {
        QualityIssuess detail = this.qualityIssuessService.getById(qualityIssuessParam.getQualityIssuesId());
        QualityIssuessResult result = new QualityIssuessResult();
        ToolUtil.copyProperties(detail, result);
        qualityIssuessService.detailFormat(result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-11-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<QualityIssuessResult> list(@RequestBody(required = false) QualityIssuessParam qualityIssuessParam) {
        if(ToolUtil.isEmpty(qualityIssuessParam)){
            qualityIssuessParam = new QualityIssuessParam();
        }
        return this.qualityIssuessService.findPageBySpec(qualityIssuessParam);
    }




}


