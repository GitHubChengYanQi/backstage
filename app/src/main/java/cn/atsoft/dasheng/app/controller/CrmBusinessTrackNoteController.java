package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmBusinessTrackNote;
import cn.atsoft.dasheng.app.model.params.CrmBusinessTrackNoteParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessTrackNoteResult;
import cn.atsoft.dasheng.app.service.CrmBusinessTrackNoteService;
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
 * 商机跟踪备注控制器
 *
 * @author 
 * @Date 2021-08-04 08:30:07
 */
@RestController
@RequestMapping("/crmBusinessTrackNote")
@Api(tags = "商机跟踪备注")
public class CrmBusinessTrackNoteController extends BaseController {

    @Autowired
    private CrmBusinessTrackNoteService crmBusinessTrackNoteService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CrmBusinessTrackNoteParam crmBusinessTrackNoteParam) {
        this.crmBusinessTrackNoteService.add(crmBusinessTrackNoteParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CrmBusinessTrackNoteParam crmBusinessTrackNoteParam) {

        this.crmBusinessTrackNoteService.update(crmBusinessTrackNoteParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CrmBusinessTrackNoteParam crmBusinessTrackNoteParam)  {
        this.crmBusinessTrackNoteService.delete(crmBusinessTrackNoteParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CrmBusinessTrackNoteResult> detail(@RequestBody CrmBusinessTrackNoteParam crmBusinessTrackNoteParam) {
        CrmBusinessTrackNote detail = this.crmBusinessTrackNoteService.getById(crmBusinessTrackNoteParam.getNoteId());
        CrmBusinessTrackNoteResult result = new CrmBusinessTrackNoteResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-08-04
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CrmBusinessTrackNoteResult> list(@RequestBody(required = false) CrmBusinessTrackNoteParam crmBusinessTrackNoteParam) {
        if(ToolUtil.isEmpty(crmBusinessTrackNoteParam)){
            crmBusinessTrackNoteParam = new CrmBusinessTrackNoteParam();
        }
        return this.crmBusinessTrackNoteService.findPageBySpec(crmBusinessTrackNoteParam);
    }




}


