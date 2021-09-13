package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.SpeechcraftType;
import cn.atsoft.dasheng.crm.model.params.SpeechcraftTypeParam;
import cn.atsoft.dasheng.crm.model.result.SpeechcraftTypeResult;
import cn.atsoft.dasheng.crm.service.SpeechcraftTypeService;
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
 * 话术分类控制器
 *
 * @author 
 * @Date 2021-09-13 13:00:15
 */
@RestController
@RequestMapping("/speechcraftType")
@Api(tags = "话术分类")
public class SpeechcraftTypeController extends BaseController {

    @Autowired
    private SpeechcraftTypeService speechcraftTypeService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SpeechcraftTypeParam speechcraftTypeParam) {
        this.speechcraftTypeService.add(speechcraftTypeParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SpeechcraftTypeParam speechcraftTypeParam) {

        this.speechcraftTypeService.update(speechcraftTypeParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SpeechcraftTypeParam speechcraftTypeParam)  {
        this.speechcraftTypeService.delete(speechcraftTypeParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<SpeechcraftTypeResult> detail(@RequestBody SpeechcraftTypeParam speechcraftTypeParam) {
        SpeechcraftType detail = this.speechcraftTypeService.getById(speechcraftTypeParam.getSpeechcraftTypeId());
        SpeechcraftTypeResult result = new SpeechcraftTypeResult();
        ToolUtil.copyProperties(detail, result);

        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SpeechcraftTypeResult> list(@RequestBody(required = false) SpeechcraftTypeParam speechcraftTypeParam) {
        if(ToolUtil.isEmpty(speechcraftTypeParam)){
            speechcraftTypeParam = new SpeechcraftTypeParam();
        }
        return this.speechcraftTypeService.findPageBySpec(speechcraftTypeParam);
    }




}


