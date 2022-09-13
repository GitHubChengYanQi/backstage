package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityTaskRefuse;
import cn.atsoft.dasheng.erp.model.params.QualityTaskRefuseParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskRefuseResult;
import cn.atsoft.dasheng.erp.service.QualityTaskRefuseService;
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
 * 质检任务拒检控制器
 *
 * @author song
 * @Date 2021-12-14 08:13:21
 */
@RestController
@RequestMapping("/qualityTaskRefuse")
@Api(tags = "质检任务拒检")
public class QualityTaskRefuseController extends BaseController {

    @Autowired
    private QualityTaskRefuseService qualityTaskRefuseService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-12-14
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody QualityTaskRefuseParam qualityTaskRefuseParam) {
        this.qualityTaskRefuseService.add(qualityTaskRefuseParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-12-14
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody QualityTaskRefuseParam qualityTaskRefuseParam) {

        this.qualityTaskRefuseService.update(qualityTaskRefuseParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-12-14
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody QualityTaskRefuseParam qualityTaskRefuseParam) {
        this.qualityTaskRefuseService.delete(qualityTaskRefuseParam);
        return ResponseData.success();
    }


    /**
     * 拒绝检查
     *
     * @author song
     * @Date 2021-12-14
     */
    @RequestMapping(value = "/refuse", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData refuse(@RequestBody QualityTaskRefuseParam qualityTaskRefuseParam) {
        this.qualityTaskRefuseService.refuse(qualityTaskRefuseParam);
        return ResponseData.success();
    }


    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-12-14
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody QualityTaskRefuseParam qualityTaskRefuseParam) {
        QualityTaskRefuse detail = this.qualityTaskRefuseService.getById(qualityTaskRefuseParam.getRefuseId());
        QualityTaskRefuseResult result = new QualityTaskRefuseResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-12-14
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<QualityTaskRefuseResult> list(@RequestBody(required = false) QualityTaskRefuseParam qualityTaskRefuseParam) {
        if (ToolUtil.isEmpty(qualityTaskRefuseParam)) {
            qualityTaskRefuseParam = new QualityTaskRefuseParam();
        }
        return this.qualityTaskRefuseService.findPageBySpec(qualityTaskRefuseParam);
    }


}


