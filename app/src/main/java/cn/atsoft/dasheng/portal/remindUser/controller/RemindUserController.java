package cn.atsoft.dasheng.portal.remindUser.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.portal.remindUser.entity.RemindUser;
import cn.atsoft.dasheng.portal.remindUser.model.params.RemindUserParam;
import cn.atsoft.dasheng.portal.remindUser.model.result.RemindUserResult;
import cn.atsoft.dasheng.portal.remindUser.service.RemindUserService;
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
 * @author c
 * @Date 2021-08-27 08:02:17
 */
@RestController
@RequestMapping("/remindUser")
@Api(tags = "")
public class RemindUserController extends BaseController {

    @Autowired
    private RemindUserService remindUserService;

    /**
     * 新增接口
     *
     * @author c
     * @Date 2021-08-27
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RemindUserParam remindUserParam) {
        this.remindUserService.add(remindUserParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author c
     * @Date 2021-08-27
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RemindUserParam remindUserParam) {

        this.remindUserService.update(remindUserParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author c
     * @Date 2021-08-27
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RemindUserParam remindUserParam)  {
        this.remindUserService.delete(remindUserParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author c
     * @Date 2021-08-27
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody RemindUserParam remindUserParam) {
        RemindUser detail = this.remindUserService.getById(remindUserParam.getRemindUserId());
        RemindUserResult result = new RemindUserResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author c
     * @Date 2021-08-27
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RemindUserResult> list(@RequestBody(required = false) RemindUserParam remindUserParam) {
        if(ToolUtil.isEmpty(remindUserParam)){
            remindUserParam = new RemindUserParam();
        }
        return this.remindUserService.findPageBySpec(remindUserParam);
    }




}


