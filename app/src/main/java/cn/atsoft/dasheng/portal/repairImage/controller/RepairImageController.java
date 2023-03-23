package cn.atsoft.dasheng.portal.repairImage.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.repairImage.entity.RepairImage;
import cn.atsoft.dasheng.portal.repairImage.model.params.RepairImageParam;
import cn.atsoft.dasheng.portal.repairImage.model.result.RepairImageResult;
import cn.atsoft.dasheng.portal.repairImage.service.RepairImageService;
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
 * 报修图片控制器
 *
 * @author 1
 * @Date 2021-09-01 14:08:22
 */
@RestController
@RequestMapping("/repairImage")
@Api(tags = "报修图片")
public class RepairImageController extends BaseController {

    @Autowired
    private RepairImageService repairImageService;

    /**
     * 新增接口
     *
     * @author 1
     * @Date 2021-09-01
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RepairImageParam repairImageParam) {
        this.repairImageService.add(repairImageParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 1
     * @Date 2021-09-01
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RepairImageParam repairImageParam) {

        this.repairImageService.update(repairImageParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 1
     * @Date 2021-09-01
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RepairImageParam repairImageParam)  {
        this.repairImageService.delete(repairImageParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 1
     * @Date 2021-09-01
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody RepairImageParam repairImageParam) {
        RepairImage detail = this.repairImageService.getById(repairImageParam.getRepairImageId());
        RepairImageResult result = new RepairImageResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 1
     * @Date 2021-09-01
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RepairImageResult> list(@RequestBody(required = false) RepairImageParam repairImageParam) {
        if(ToolUtil.isEmpty(repairImageParam)){
            repairImageParam = new RepairImageParam();
        }
        return this.repairImageService.findPageBySpec(repairImageParam);
    }




}


