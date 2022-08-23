package cn.atsoft.dasheng.portal.remind.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;

import cn.atsoft.dasheng.portal.remind.model.params.RemindParam;
import cn.atsoft.dasheng.portal.remind.model.result.RemindResult;
import cn.atsoft.dasheng.portal.remind.service.RemindService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 提醒表控制器
 *
 * @author cheng
 * @Date 2021-08-26 15:50:39
 */
@RestController
@RequestMapping("/remind")
@Api(tags = "提醒表")
public class RemindController extends BaseController {

    @Autowired
    private RemindService remindService;


    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-08-26
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RemindParam remindParam) {
        this.remindService.add(remindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-08-26
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RemindParam remindParam) {

        this.remindService.update(remindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2021-08-26
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RemindParam remindParam) {
        this.remindService.delete(remindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-08-26
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody RemindParam remindParam) {
        PageInfo<RemindResult> pageBySpec = this.remindService.findPageBySpec(remindParam);
        return ResponseData.success(pageBySpec.getData().get(0));
    }

    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-08-26
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RemindResult> list(@RequestBody(required = false) RemindParam remindParam) {
        if (ToolUtil.isEmpty(remindParam)) {
            remindParam = new RemindParam();
        }
        return this.remindService.findPageBySpec(remindParam);
    }


}


