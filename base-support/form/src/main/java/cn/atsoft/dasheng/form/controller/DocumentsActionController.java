package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsAction;
import cn.atsoft.dasheng.form.model.params.DocumentsActionParam;
import cn.atsoft.dasheng.form.model.result.DocumentsActionResult;
import cn.atsoft.dasheng.form.service.DocumentsActionService;
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
 * 单据动作控制器
 *
 * @author song
 * @Date 2022-04-28 18:09:19
 */
@RestController
@RequestMapping("/documentsAction")
@Api(tags = "单据动作")
public class DocumentsActionController extends BaseController {

    @Autowired
    private DocumentsActionService documentsActionService;

//    /**
//     * 新增接口
//     *
//     * @author song
//     * @Date 2022-04-28
//     */
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addItem(@RequestBody DocumentsActionParam documentsActionParam) {
//        this.documentsActionService.add(documentsActionParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 编辑接口
//     *
//     * @author song
//     * @Date 2022-04-28
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody DocumentsActionParam documentsActionParam) {
//
//        this.documentsActionService.update(documentsActionParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author song
//     * @Date 2022-04-28
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody DocumentsActionParam documentsActionParam)  {
//        this.documentsActionService.delete(documentsActionParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-04-28
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody DocumentsActionParam documentsActionParam) {
        DocumentsAction detail = this.documentsActionService.getById(documentsActionParam.getDocumentsActionId());
        DocumentsActionResult result = new DocumentsActionResult();
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
    public PageInfo list(@RequestBody(required = false) DocumentsActionParam documentsActionParam) {
        if(ToolUtil.isEmpty(documentsActionParam)){
            documentsActionParam = new DocumentsActionParam();
        }
        return this.documentsActionService.findPageBySpec(documentsActionParam);
    }




}


