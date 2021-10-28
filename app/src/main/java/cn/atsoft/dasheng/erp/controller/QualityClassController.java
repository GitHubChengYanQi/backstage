package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityClass;
import cn.atsoft.dasheng.erp.model.params.QualityClassParam;
import cn.atsoft.dasheng.erp.model.result.QualityClassResult;
import cn.atsoft.dasheng.erp.service.QualityClassService;
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
 * 质检方案详情分类控制器
 *
 * @author song
 * @Date 2021-10-28 15:17:56
 */
@RestController
@RequestMapping("/qualityClass")
@Api(tags = "质检方案详情分类")
public class QualityClassController extends BaseController {

    @Autowired
    private QualityClassService qualityClassService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody QualityClassParam qualityClassParam) {
        this.qualityClassService.add(qualityClassParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody QualityClassParam qualityClassParam) {

        this.qualityClassService.update(qualityClassParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody QualityClassParam qualityClassParam)  {
        this.qualityClassService.delete(qualityClassParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<QualityClassResult> detail(@RequestBody QualityClassParam qualityClassParam) {
        QualityClass detail = this.qualityClassService.getById(qualityClassParam.getQualityPlanClassId());
        QualityClassResult result = new QualityClassResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-28
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<QualityClassResult> list(@RequestBody(required = false) QualityClassParam qualityClassParam) {
        if(ToolUtil.isEmpty(qualityClassParam)){
            qualityClassParam = new QualityClassParam();
        }
        return this.qualityClassService.findPageBySpec(qualityClassParam);
    }




}


