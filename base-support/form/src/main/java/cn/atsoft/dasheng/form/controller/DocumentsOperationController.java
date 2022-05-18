package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsOperation;
import cn.atsoft.dasheng.form.model.params.DocumentsOperationParam;
import cn.atsoft.dasheng.form.model.result.DocumentsOperationResult;
import cn.atsoft.dasheng.form.service.DocumentsOperationService;
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
 * 单据权限操作控制器
 *
 * @author 
 * @Date 2022-05-18 09:30:38
 */
@RestController
@RequestMapping("/documentsOperation")
@Api(tags = "单据权限操作")
public class DocumentsOperationController extends BaseController {

    @Autowired
    private DocumentsOperationService documentsOperationService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2022-05-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DocumentsOperationParam documentsOperationParam) {
        this.documentsOperationService.add(documentsOperationParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2022-05-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody DocumentsOperationParam documentsOperationParam) {

        this.documentsOperationService.update(documentsOperationParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2022-05-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody DocumentsOperationParam documentsOperationParam)  {
        this.documentsOperationService.delete(documentsOperationParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2022-05-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<DocumentsOperationResult> detail(@RequestBody DocumentsOperationParam documentsOperationParam) {
        DocumentsOperation detail = this.documentsOperationService.getById(documentsOperationParam);
        DocumentsOperationResult result = new DocumentsOperationResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2022-05-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<DocumentsOperationResult> list(@RequestBody(required = false) DocumentsOperationParam documentsOperationParam) {
        if(ToolUtil.isEmpty(documentsOperationParam)){
            documentsOperationParam = new DocumentsOperationParam();
        }
        return this.documentsOperationService.findPageBySpec(documentsOperationParam);
    }




}


