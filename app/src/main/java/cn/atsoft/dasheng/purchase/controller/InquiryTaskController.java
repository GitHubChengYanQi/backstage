package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.InquiryTask;
import cn.atsoft.dasheng.purchase.model.params.InquiryTaskParam;
import cn.atsoft.dasheng.purchase.model.result.InquiryTaskResult;
import cn.atsoft.dasheng.purchase.service.InquiryTaskService;
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
 * 询价任务控制器
 *
 * @author Captain_Jazz
 * @Date 2021-12-23 10:25:05
 */
@RestController
@RequestMapping("/inquiryTask")
@Api(tags = "询价任务")
public class InquiryTaskController extends BaseController {

    @Autowired
    private InquiryTaskService inquiryTaskService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InquiryTaskParam inquiryTaskParam) {
        this.inquiryTaskService.add(inquiryTaskParam);
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
    public ResponseData update(@RequestBody InquiryTaskParam inquiryTaskParam) {

        this.inquiryTaskService.update(inquiryTaskParam);
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
    public ResponseData delete(@RequestBody InquiryTaskParam inquiryTaskParam) {
        this.inquiryTaskService.delete(inquiryTaskParam);
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
    public ResponseData detail(@RequestBody InquiryTaskParam inquiryTaskParam) {
        InquiryTaskResult detail = this.inquiryTaskService.detail(inquiryTaskParam.getInquiryTaskId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InquiryTaskResult> list(@RequestBody(required = false) InquiryTaskParam inquiryTaskParam) {
        if (ToolUtil.isEmpty(inquiryTaskParam)) {
            inquiryTaskParam = new InquiryTaskParam();
        }
        return this.inquiryTaskService.findPageBySpec(inquiryTaskParam);
    }


}


