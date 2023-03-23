package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockLog;
import cn.atsoft.dasheng.erp.model.params.InstockLogParam;
import cn.atsoft.dasheng.erp.model.result.InstockLogResult;
import cn.atsoft.dasheng.erp.service.InstockLogService;
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
 * 入库记录表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-04-14 15:27:58
 */
@RestController
@RequestMapping("/instockLog")
@Api(tags = "入库记录表")
public class InstockLogController extends BaseController {

    @Autowired
    private InstockLogService instockLogService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InstockLogParam instockLogParam) {
        this.instockLogService.add(instockLogParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody InstockLogParam instockLogParam) {

        this.instockLogService.update(instockLogParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody InstockLogParam instockLogParam)  {
//        this.instockLogService.delete(instockLogParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody InstockLogParam instockLogParam) {
        InstockLog detail = this.instockLogService.getById(instockLogParam.getInstockLogId());
        InstockLogResult result = new InstockLogResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InstockLogResult> list(@RequestBody(required = false) InstockLogParam instockLogParam) {
        if(ToolUtil.isEmpty(instockLogParam)){
            instockLogParam = new InstockLogParam();
        }
        return this.instockLogService.findPageBySpec(instockLogParam);
    }
    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    @RequestMapping(value = "/getListByInstockOrder", method = RequestMethod.GET)
    @ApiOperation("列表")
    public ResponseData list(@RequestParam Long id) {
        if(ToolUtil.isEmpty(id)){
            return null;
        }
        return ResponseData.success(this.instockLogService.listByInstockOrderId(id));
    }




}


