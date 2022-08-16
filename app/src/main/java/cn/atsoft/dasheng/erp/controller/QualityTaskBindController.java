package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityTaskBind;
import cn.atsoft.dasheng.erp.model.params.QualityTaskBindParam;
import cn.atsoft.dasheng.erp.model.result.QualityTaskBindResult;
import cn.atsoft.dasheng.erp.service.QualityTaskBindService;
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
 * 控制器
 *
 * @author song
 * @Date 2021-11-17 15:46:09
 */
@RestController
@RequestMapping("/qualityTaskBind")
@Api(tags = "")
public class QualityTaskBindController extends BaseController {

    @Autowired
    private QualityTaskBindService qualityTaskBindService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-11-17
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody QualityTaskBindParam qualityTaskBindParam) {
        this.qualityTaskBindService.add(qualityTaskBindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-11-17
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody QualityTaskBindParam qualityTaskBindParam) {

        this.qualityTaskBindService.update(qualityTaskBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-11-17
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody QualityTaskBindParam qualityTaskBindParam)  {
        this.qualityTaskBindService.delete(qualityTaskBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-11-17
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody QualityTaskBindParam qualityTaskBindParam) {
        QualityTaskBind detail = this.qualityTaskBindService.getById(qualityTaskBindParam.getBingId());
        QualityTaskBindResult result = new QualityTaskBindResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-11-17
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<QualityTaskBindResult> list(@RequestBody(required = false) QualityTaskBindParam qualityTaskBindParam) {
        if(ToolUtil.isEmpty(qualityTaskBindParam)){
            qualityTaskBindParam = new QualityTaskBindParam();
        }
        return this.qualityTaskBindService.findPageBySpec(qualityTaskBindParam);
    }




}


