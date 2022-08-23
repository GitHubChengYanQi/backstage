package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Speechcraft;
import cn.atsoft.dasheng.crm.model.params.SpeechcraftParam;
import cn.atsoft.dasheng.crm.model.result.SpeechcraftResult;
import cn.atsoft.dasheng.crm.service.SpeechcraftService;
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
 * 话术基础资料控制器
 *
 * @author 
 * @Date 2021-09-11 13:27:08
 */
@RestController
@RequestMapping("/speechcraft")
@Api(tags = "话术基础资料")
public class SpeechcraftController extends BaseController {

    @Autowired
    private SpeechcraftService speechcraftService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SpeechcraftParam speechcraftParam) {
        this.speechcraftService.add(speechcraftParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SpeechcraftParam speechcraftParam) {

        this.speechcraftService.update(speechcraftParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SpeechcraftParam speechcraftParam)  {
        this.speechcraftService.delete(speechcraftParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody SpeechcraftParam speechcraftParam) {
        Speechcraft detail = this.speechcraftService.getById(speechcraftParam.getSpeechcraftId());
        SpeechcraftResult result = new SpeechcraftResult();
        ToolUtil.copyProperties(detail, result);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-09-11
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SpeechcraftResult> list(@RequestBody(required = false) SpeechcraftParam speechcraftParam) {
        if(ToolUtil.isEmpty(speechcraftParam)){
            speechcraftParam = new SpeechcraftParam();
        }
        return this.speechcraftService.findPageBySpec(speechcraftParam);
    }




}


