package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.InstockLogDetail;
import cn.atsoft.dasheng.erp.model.params.InstockLogDetailParam;
import cn.atsoft.dasheng.erp.model.result.InstockLogDetailResult;
import cn.atsoft.dasheng.erp.service.InstockLogDetailService;
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
 * 控制器
 *
 * @author Captain_Jazz
 * @Date 2022-04-14 15:27:58
 */
@RestController
@RequestMapping("/instockLogDetail")
@Api(tags = "")
public class InstockLogDetailController extends BaseController {

    @Autowired
    private InstockLogDetailService instockLogDetailService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InstockLogDetailParam instockLogDetailParam) {
        this.instockLogDetailService.add(instockLogDetailParam);
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
    public ResponseData update(@RequestBody InstockLogDetailParam instockLogDetailParam) {

        this.instockLogDetailService.update(instockLogDetailParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody InstockLogDetailParam instockLogDetailParam) {
        this.instockLogDetailService.delete(instockLogDetailParam);
        return ResponseData.success();
    }


    /**
     * 历史记录
     *
     * @param instockLogDetailParam
     * @return
     */
    @RequestMapping(value = "/history", method = RequestMethod.POST)
    public ResponseData history(@RequestBody InstockLogDetailParam instockLogDetailParam) {
        List<InstockLogDetailResult> history = this.instockLogDetailService.history(instockLogDetailParam);
        return ResponseData.success(history);
    }


    @RequestMapping(value = "/timeHistory", method = RequestMethod.POST)
    public ResponseData timeHistory(@RequestBody InstockLogDetailParam instockLogDetailParam) {
        List<InstockLogDetailResult> timeHistory = this.instockLogDetailService.timeHistory(instockLogDetailParam);
        return ResponseData.success(timeHistory);
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody InstockLogDetailParam instockLogDetailParam) {
        InstockLogDetail detail = this.instockLogDetailService.getById(instockLogDetailParam.getInstockLogDetailId());
        InstockLogDetailResult result = new InstockLogDetailResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
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
    public PageInfo<InstockLogDetailResult> list(@RequestBody(required = false) InstockLogDetailParam instockLogDetailParam) {
        if (ToolUtil.isEmpty(instockLogDetailParam)) {
            instockLogDetailParam = new InstockLogDetailParam();
        }
        return this.instockLogDetailService.findPageBySpec(instockLogDetailParam);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-04-14
     */
    @RequestMapping(value = "/getOutStockLogs", method = RequestMethod.POST)
    @ApiOperation("出库记录列表")
    public ResponseData getOutStockLogs(@RequestBody(required = false) InstockLogDetailParam instockLogDetailParam) {
        if (ToolUtil.isEmpty(instockLogDetailParam)) {
            return ResponseData.success();
        }
        instockLogDetailParam.setSource("pick_lists");
        return ResponseData.success(this.instockLogDetailService.getOutStockLogs(instockLogDetailParam));
    }


}


