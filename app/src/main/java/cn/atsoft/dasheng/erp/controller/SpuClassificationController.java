package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.SpuClassification;
import cn.atsoft.dasheng.erp.model.params.SpuClassificationParam;
import cn.atsoft.dasheng.erp.model.result.SpuClassificationResult;
import cn.atsoft.dasheng.erp.service.SpuClassificationService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.wrapper.SpuClassificationSelectWrapper;
import cn.atsoft.dasheng.erp.wrapper.SpuSelectWrapper;
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
 * SPU分类控制器
 *
 * @author song
 * @Date 2021-10-25 17:52:03
 */
@RestController
@RequestMapping("/spuClassification")
@Api(tags = "SPU分类")
public class SpuClassificationController extends BaseController {

    @Autowired
    private SpuClassificationService spuClassificationService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SpuClassificationParam spuClassificationParam) {
        this.spuClassificationService.add(spuClassificationParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SpuClassificationParam spuClassificationParam) {

        this.spuClassificationService.update(spuClassificationParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SpuClassificationParam spuClassificationParam) {
        this.spuClassificationService.delete(spuClassificationParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<SpuClassificationResult> detail(@RequestBody SpuClassificationParam spuClassificationParam) {
        SpuClassification detail = this.spuClassificationService.getById(spuClassificationParam.getSpuClassificationId());
        SpuClassificationResult result = new SpuClassificationResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SpuClassificationResult> list(@RequestBody(required = false) SpuClassificationParam spuClassificationParam) {
        if (ToolUtil.isEmpty(spuClassificationParam)) {
            spuClassificationParam = new SpuClassificationParam();
        }
        return this.spuClassificationService.findPageBySpec(spuClassificationParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect() {
        List<Map<String, Object>> list = this.spuClassificationService.listMaps();
        SpuClassificationSelectWrapper spuClassificationSelectWrapper = new SpuClassificationSelectWrapper(list);
        List<Map<String, Object>> result = spuClassificationSelectWrapper.wrap();
        return ResponseData.success(result);
    }

}


