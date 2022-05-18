package cn.atsoft.dasheng.form.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.form.entity.DocumentsPermissions;
import cn.atsoft.dasheng.form.model.params.DocumentsPermissionsParam;
import cn.atsoft.dasheng.form.model.result.DocumentsPermissionsResult;
import cn.atsoft.dasheng.form.pojo.PermissionParam;
import cn.atsoft.dasheng.form.service.DocumentsPermissionsService;
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
 * 单据权限控制器
 *
 * @author 
 * @Date 2022-05-18 09:30:38
 */
@RestController
@RequestMapping("/documentsPermissions")
@Api(tags = "单据权限")
public class DocumentsPermissionsController extends BaseController {

    @Autowired
    private DocumentsPermissionsService documentsPermissionsService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2022-05-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DocumentsPermissionsParam documentsPermissionsParam) {
        this.documentsPermissionsService.add(documentsPermissionsParam);
        return ResponseData.success();
    }

    /**
     * 新增接口
     *
     * @author
     * @Date 2022-05-18
     */
    @RequestMapping(value = "/addList", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addList(@RequestBody PermissionParam param) {
        documentsPermissionsService.addList(param);
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
    public ResponseData update(@RequestBody DocumentsPermissionsParam documentsPermissionsParam) {

        this.documentsPermissionsService.update(documentsPermissionsParam);
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
    public ResponseData delete(@RequestBody DocumentsPermissionsParam documentsPermissionsParam)  {
        this.documentsPermissionsService.delete(documentsPermissionsParam);
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
    public ResponseData<DocumentsPermissionsResult> detail(@RequestBody DocumentsPermissionsParam documentsPermissionsParam) {
        DocumentsPermissions detail = this.documentsPermissionsService.getById(documentsPermissionsParam.getPermissionsId());
        DocumentsPermissionsResult result = new DocumentsPermissionsResult();
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
    public PageInfo<DocumentsPermissionsResult> list(@RequestBody(required = false) DocumentsPermissionsParam documentsPermissionsParam) {
        if(ToolUtil.isEmpty(documentsPermissionsParam)){
            documentsPermissionsParam = new DocumentsPermissionsParam();
        }
        return this.documentsPermissionsService.findPageBySpec(documentsPermissionsParam);
    }




}


