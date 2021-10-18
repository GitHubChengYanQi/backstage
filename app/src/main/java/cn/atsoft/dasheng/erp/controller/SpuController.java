package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.service.SpuService;
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
 * @author 
 * @Date 2021-10-18 14:14:21
 */
@RestController
@RequestMapping("/spu")
@Api(tags = "")
public class SpuController extends BaseController {

    @Autowired
    private SpuService spuService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SpuParam spuParam) {
        this.spuService.add(spuParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SpuParam spuParam) {

        this.spuService.update(spuParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SpuParam spuParam)  {
        this.spuService.delete(spuParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<SpuResult> detail(@RequestBody SpuParam spuParam) {
        Spu detail = this.spuService.getById(spuParam.getSpuId());
        SpuResult result = new SpuResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SpuResult> list(@RequestBody(required = false) SpuParam spuParam) {
        if(ToolUtil.isEmpty(spuParam)){
            spuParam = new SpuParam();
        }
        return this.spuService.findPageBySpec(spuParam);
    }




}


