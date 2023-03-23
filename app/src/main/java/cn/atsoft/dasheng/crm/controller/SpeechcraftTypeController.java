package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.app.model.result.BatchDeleteRequest;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.DataClassification;
import cn.atsoft.dasheng.crm.entity.SpeechcraftType;
import cn.atsoft.dasheng.crm.model.params.SpeechcraftTypeParam;
import cn.atsoft.dasheng.crm.model.result.SpeechcraftTypeResult;
import cn.atsoft.dasheng.crm.service.SpeechcraftTypeService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.wrapper.DataClassificationSelectWrapper;
import cn.atsoft.dasheng.crm.wrapper.SpeechcraftSelectWrapper;
import cn.atsoft.dasheng.crm.wrapper.SpeechcraftTypeSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
     * 批量删除接口
     * @param batchDeleteRequest
     * @return
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("批量删除")
    public ResponseData batchDelete(@RequestBody (required = false) BatchDeleteRequest batchDeleteRequest) {
        this.speechcraftTypeService.batchDelete(batchDeleteRequest.getIds());
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
    public ResponseData detail(@RequestBody SpeechcraftTypeParam speechcraftTypeParam) {
        SpeechcraftType detail = this.speechcraftTypeService.getById(speechcraftTypeParam.getSpeechcraftTypeId());
        SpeechcraftTypeResult result = new SpeechcraftTypeResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


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


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<SpeechcraftType> speechcraftTypeQueryWrapper = new QueryWrapper<>();
        speechcraftTypeQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.speechcraftTypeService.listMaps(speechcraftTypeQueryWrapper);
        SpeechcraftTypeSelectWrapper speechcraftTypeSelectWrapper = new SpeechcraftTypeSelectWrapper(list);
        List<Map<String, Object>> result = speechcraftTypeSelectWrapper.wrap();
        return ResponseData.success(result);
    }





}


