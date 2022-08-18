package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Message;
import cn.atsoft.dasheng.app.model.params.MessageParam;
import cn.atsoft.dasheng.app.model.result.MessageResult;
import cn.atsoft.dasheng.app.service.MessageService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 消息提醒控制器
 *
 * @author
 * @Date 2021-08-03 11:51:29
 */
@RestController
@RequestMapping("/message")
@Api(tags = "消息提醒")
public class MessageController extends BaseController {

    @Autowired
    private MessageService messageService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody MessageParam messageParam) {
        this.messageService.add(messageParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody MessageParam messageParam) {

        this.messageService.update(messageParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody MessageParam messageParam) {
        this.messageService.delete(messageParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<MessageResult> detail(@RequestBody MessageParam messageParam) {
        Message detail = this.messageService.getById(messageParam.getMessageId());
        MessageResult result = new MessageResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-08-03
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<MessageResult> list(@RequestBody(required = false) MessageParam messageParam) {
        if (ToolUtil.isEmpty(messageParam)) {
            messageParam = new MessageParam();
        }
//        return this.messageService.findPageBySpec(messageParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.messageService.findPageBySpec(messageParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.messageService.findPageBySpec(messageParam, dataScope);
        }
    }


    @RequestMapping(value = "/view", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<MessageResult> view(@RequestBody(required = false) MessageParam messageParam) {
        if (ToolUtil.isEmpty(messageParam)) {
            messageParam = new MessageParam();
        }
        return this.messageService.findPageBySpec(messageParam, null);
    }

    @RequestMapping(value = "/getViewCount", method = RequestMethod.GET)
    @ApiOperation("查询登陆人未读消息数量")
    public ResponseData getViewCount() {
        return ResponseData.success(this.messageService.getViewCount());
    }
}


