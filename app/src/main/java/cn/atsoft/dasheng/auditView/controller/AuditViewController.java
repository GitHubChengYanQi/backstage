package cn.atsoft.dasheng.auditView.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.auditView.entity.AuditView;
import cn.atsoft.dasheng.auditView.model.params.AuditViewParam;
import cn.atsoft.dasheng.auditView.model.result.AuditViewResult;
import cn.atsoft.dasheng.auditView.service.AuditViewService;
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
 * 所有审核控制器
 *
 * @author song
 * @Date 2021-12-16 15:05:58
 */
@RestController
@RequestMapping("/auditView")
@Api(tags = "所有审核")
public class AuditViewController extends BaseController {

    @Autowired
    private AuditViewService auditViewService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-12-16
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody AuditViewParam auditViewParam) {
        this.auditViewService.add(auditViewParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-12-16
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody AuditViewParam auditViewParam) {

        this.auditViewService.update(auditViewParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-12-16
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody AuditViewParam auditViewParam)  {
        this.auditViewService.delete(auditViewParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-12-16
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody AuditViewParam auditViewParam) {
        AuditView detail = this.auditViewService.getById(auditViewParam.getAuditViewId());
        AuditViewResult result = new AuditViewResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-12-16
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<AuditViewResult> list(@RequestBody(required = false) AuditViewParam auditViewParam) {
        if(ToolUtil.isEmpty(auditViewParam)){
            auditViewParam = new AuditViewParam();
        }
        return this.auditViewService.findPageBySpec(auditViewParam);
    }




}


