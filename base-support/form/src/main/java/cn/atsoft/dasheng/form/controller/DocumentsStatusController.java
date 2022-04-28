package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsStatus;
import cn.atsoft.dasheng.form.model.params.DocumentsStatusParam;
import cn.atsoft.dasheng.form.model.result.DocumentsStatusResult;
import cn.atsoft.dasheng.form.service.DocumentsStatusService;
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
 * 单据状态控制器
 *
 * @author song
 * @Date 2022-04-28 11:51:37
 */
@RestController
@RequestMapping("/documentsStatus")
@Api(tags = "单据状态")
public class DocumentsStatusController extends BaseController {

    @Autowired
    private DocumentsStatusService documentsStatusService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-04-28
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DocumentsStatusParam documentsStatusParam) {
        this.documentsStatusService.add(documentsStatusParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-04-28
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody DocumentsStatusParam documentsStatusParam) {

        this.documentsStatusService.update(documentsStatusParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-04-28
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody DocumentsStatusParam documentsStatusParam)  {
        this.documentsStatusService.delete(documentsStatusParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-04-28
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<DocumentsStatusResult> detail(@RequestBody DocumentsStatusParam documentsStatusParam) {
        DocumentsStatus detail = this.documentsStatusService.getById(documentsStatusParam.getDocumentsStatusId());
        DocumentsStatusResult result = new DocumentsStatusResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-04-28
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<DocumentsStatusResult> list(@RequestBody(required = false) DocumentsStatusParam documentsStatusParam) {
        if(ToolUtil.isEmpty(documentsStatusParam)){
            documentsStatusParam = new DocumentsStatusParam();
        }
        return this.documentsStatusService.findPageBySpec(documentsStatusParam);
    }




}


