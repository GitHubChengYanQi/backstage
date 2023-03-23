package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.SopDetail;
import cn.atsoft.dasheng.production.model.params.SopDetailParam;
import cn.atsoft.dasheng.production.model.result.SopDetailResult;
import cn.atsoft.dasheng.production.service.SopDetailService;
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
 * sop详情控制器
 *
 * @author song
 * @Date 2022-02-10 09:21:35
 */
@RestController
@RequestMapping("/sopDetail")
@Api(tags = "sop详情")
public class SopDetailController extends BaseController {

    @Autowired
    private SopDetailService sopDetailService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SopDetailParam sopDetailParam) {
        this.sopDetailService.add(sopDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SopDetailParam sopDetailParam) {

        this.sopDetailService.update(sopDetailParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SopDetailParam sopDetailParam)  {
        this.sopDetailService.delete(sopDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody SopDetailParam sopDetailParam) {
        SopDetail detail = this.sopDetailService.getById(sopDetailParam.getSopDetailId());
        SopDetailResult result = new SopDetailResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SopDetailResult> list(@RequestBody(required = false) SopDetailParam sopDetailParam) {
        if(ToolUtil.isEmpty(sopDetailParam)){
            sopDetailParam = new SopDetailParam();
        }
        return this.sopDetailService.findPageBySpec(sopDetailParam);
    }




}


