package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.SpeechcraftType;
import cn.atsoft.dasheng.crm.entity.SpeechcraftTypeDetail;
import cn.atsoft.dasheng.crm.model.params.SpeechcraftTypeDetailParam;
import cn.atsoft.dasheng.crm.model.result.SpeechcraftTypeDetailResult;
import cn.atsoft.dasheng.crm.service.SpeechcraftTypeDetailService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.wrapper.SpeechcraftTypeDetailSelectWrapper;
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
 * 话术分类详细控制器
 *
 * @author cheng
 * @Date 2021-09-13 15:24:19
 */
@RestController
@RequestMapping("/speechcraftTypeDetail")
@Api(tags = "话术分类详细")
public class SpeechcraftTypeDetailController extends BaseController {

    @Autowired
    private SpeechcraftTypeDetailService speechcraftTypeDetailService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SpeechcraftTypeDetailParam speechcraftTypeDetailParam) {
        this.speechcraftTypeDetailService.add(speechcraftTypeDetailParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SpeechcraftTypeDetailParam speechcraftTypeDetailParam) {

        this.speechcraftTypeDetailService.update(speechcraftTypeDetailParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SpeechcraftTypeDetailParam speechcraftTypeDetailParam)  {
        this.speechcraftTypeDetailService.delete(speechcraftTypeDetailParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody SpeechcraftTypeDetailParam speechcraftTypeDetailParam) {
        SpeechcraftTypeDetail detail = this.speechcraftTypeDetailService.getById(speechcraftTypeDetailParam.getSpeechcraftTypeDetailId());
        SpeechcraftTypeDetailResult result = new SpeechcraftTypeDetailResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-09-13
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SpeechcraftTypeDetailResult> list(@RequestBody(required = false) SpeechcraftTypeDetailParam speechcraftTypeDetailParam) {
        if(ToolUtil.isEmpty(speechcraftTypeDetailParam)){
            speechcraftTypeDetailParam = new SpeechcraftTypeDetailParam();
        }
        return this.speechcraftTypeDetailService.findPageBySpec(speechcraftTypeDetailParam);
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(@RequestBody(required = false) SpeechcraftTypeDetailParam speechcraftTypeDetailParam) {
        QueryWrapper<SpeechcraftTypeDetail> speechcraftTypeDetailQueryWrapper = new QueryWrapper<>();
        speechcraftTypeDetailQueryWrapper.in("display", 1);
        if (ToolUtil.isNotEmpty(speechcraftTypeDetailParam) && ToolUtil.isNotEmpty(speechcraftTypeDetailParam.getSpeechcraftTypeId())){
            speechcraftTypeDetailQueryWrapper.in("speechcraft_type_id", speechcraftTypeDetailParam.getSpeechcraftTypeId());
        }
        List<Map<String, Object>> list = this.speechcraftTypeDetailService.listMaps(speechcraftTypeDetailQueryWrapper);
        SpeechcraftTypeDetailSelectWrapper speechcraftTypeDetailSelectWrapper = new SpeechcraftTypeDetailSelectWrapper(list);
        List<Map<String, Object>> result = speechcraftTypeDetailSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}


