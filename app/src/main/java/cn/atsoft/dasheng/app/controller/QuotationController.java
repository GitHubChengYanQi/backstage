package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Quotation;
import cn.atsoft.dasheng.app.model.params.QuotationParam;
import cn.atsoft.dasheng.app.model.result.QuotationResult;
import cn.atsoft.dasheng.app.service.QuotationService;
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
 * 报价表控制器
 *
 * @author cheng
 * @Date 2021-07-19 15:13:58
 */
@RestController
@RequestMapping("/quotation")
@Api(tags = "报价表")
public class QuotationController extends BaseController {

    @Autowired
    private QuotationService quotationService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody QuotationParam quotationParam) {
        this.quotationService.add(quotationParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody QuotationParam quotationParam) {

        this.quotationService.update(quotationParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody QuotationParam quotationParam) {
        this.quotationService.delete(quotationParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<QuotationResult> detail(@RequestBody QuotationParam quotationParam) {
        Quotation detail = this.quotationService.getById(quotationParam.getQuotationId());
        QuotationResult result = new QuotationResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-07-19
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<QuotationResult> list(@RequestBody(required = false) QuotationParam quotationParam) {
        if (ToolUtil.isEmpty(quotationParam)) {
            quotationParam = new QuotationParam();
        }
        return this.quotationService.findPageBySpec(quotationParam);
    }


}


