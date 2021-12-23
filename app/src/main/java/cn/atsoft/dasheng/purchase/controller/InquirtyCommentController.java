package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.InquirtyComment;
import cn.atsoft.dasheng.purchase.model.params.InquirtyCommentParam;
import cn.atsoft.dasheng.purchase.model.result.InquirtyCommentResult;
import cn.atsoft.dasheng.purchase.service.InquirtyCommentService;
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
 * @Date 2021-12-23 13:38:41
 */
@RestController
@RequestMapping("/inquirtyComment")
@Api(tags = "")
public class InquirtyCommentController extends BaseController {

    @Autowired
    private InquirtyCommentService inquirtyCommentService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InquirtyCommentParam inquirtyCommentParam) {
        this.inquirtyCommentService.add(inquirtyCommentParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody InquirtyCommentParam inquirtyCommentParam) {

        this.inquirtyCommentService.update(inquirtyCommentParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody InquirtyCommentParam inquirtyCommentParam)  {
        this.inquirtyCommentService.delete(inquirtyCommentParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<InquirtyCommentResult> detail(@RequestBody InquirtyCommentParam inquirtyCommentParam) {
        InquirtyComment detail = this.inquirtyCommentService.getById(inquirtyCommentParam.getCommentId());
        InquirtyCommentResult result = new InquirtyCommentResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InquirtyCommentResult> list(@RequestBody(required = false) InquirtyCommentParam inquirtyCommentParam) {
        if(ToolUtil.isEmpty(inquirtyCommentParam)){
            inquirtyCommentParam = new InquirtyCommentParam();
        }
        return this.inquirtyCommentService.findPageBySpec(inquirtyCommentParam);
    }




}


