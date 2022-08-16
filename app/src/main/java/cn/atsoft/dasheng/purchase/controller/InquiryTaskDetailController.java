package cn.atsoft.dasheng.purchase.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.InquiryTaskDetail;
import cn.atsoft.dasheng.purchase.model.params.InquiryTaskDetailParam;
import cn.atsoft.dasheng.purchase.model.result.InquiryTaskDetailResult;
import cn.atsoft.dasheng.purchase.service.InquiryTaskDetailService;
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
 * 询价任务详情控制器
 *
 * @author Captain_Jazz
 * @Date 2021-12-23 10:25:05
 */
@RestController
@RequestMapping("/inquiryTaskDetail")
@Api(tags = "询价任务详情")
public class InquiryTaskDetailController extends BaseController {

    @Autowired
    private InquiryTaskDetailService inquiryTaskDetailService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2021-12-23
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InquiryTaskDetailParam inquiryTaskDetailParam) {
        this.inquiryTaskDetailService.add(inquiryTaskDetailParam);
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
    public ResponseData update(@RequestBody InquiryTaskDetailParam inquiryTaskDetailParam) {

        this.inquiryTaskDetailService.update(inquiryTaskDetailParam);
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
    public ResponseData delete(@RequestBody InquiryTaskDetailParam inquiryTaskDetailParam)  {
        this.inquiryTaskDetailService.delete(inquiryTaskDetailParam);
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
    public ResponseData detail(@RequestBody InquiryTaskDetailParam inquiryTaskDetailParam) {
        InquiryTaskDetail detail = this.inquiryTaskDetailService.getById(inquiryTaskDetailParam.getInquiryDetailId());
        InquiryTaskDetailResult result = new InquiryTaskDetailResult();
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
    public PageInfo<InquiryTaskDetailResult> list(@RequestBody(required = false) InquiryTaskDetailParam inquiryTaskDetailParam) {
        if(ToolUtil.isEmpty(inquiryTaskDetailParam)){
            inquiryTaskDetailParam = new InquiryTaskDetailParam();
        }
        return this.inquiryTaskDetailService.findPageBySpec(inquiryTaskDetailParam);
    }




}


