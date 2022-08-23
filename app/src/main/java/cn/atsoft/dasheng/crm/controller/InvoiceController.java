package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Invoice;
import cn.atsoft.dasheng.crm.model.params.InvoiceParam;
import cn.atsoft.dasheng.crm.model.result.InvoiceResult;
import cn.atsoft.dasheng.crm.service.InvoiceService;
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
 * 供应商开票控制器
 *
 * @author song
 * @Date 2021-12-20 11:29:00
 */
@RestController
@RequestMapping("/invoice")
@Api(tags = "供应商开票")
public class InvoiceController extends BaseController {

    @Autowired
    private InvoiceService invoiceService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InvoiceParam invoiceParam) {
        Invoice invoice = this.invoiceService.add(invoiceParam);
        return ResponseData.success(invoice);
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody InvoiceParam invoiceParam) {

        this.invoiceService.update(invoiceParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody InvoiceParam invoiceParam) {
        this.invoiceService.delete(invoiceParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody InvoiceParam invoiceParam) {
        Invoice detail = this.invoiceService.getById(invoiceParam.getInvoiceId());
        InvoiceResult result = new InvoiceResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InvoiceResult> list(@RequestBody(required = false) InvoiceParam invoiceParam) {
        if (ToolUtil.isEmpty(invoiceParam)) {
            invoiceParam = new InvoiceParam();
        }
        return this.invoiceService.findPageBySpec(invoiceParam);
    }


}


