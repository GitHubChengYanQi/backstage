package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.InvoiceBill;
import cn.atsoft.dasheng.crm.model.params.InvoiceBillParam;
import cn.atsoft.dasheng.crm.model.result.InvoiceBillResult;
import cn.atsoft.dasheng.crm.service.InvoiceBillService;
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
 * 发票控制器
 *
 * @author song
 * @Date 2022-04-22 16:17:44
 */
@RestController
@RequestMapping("/invoiceBill")
@Api(tags = "发票")
public class InvoiceBillController extends BaseController {

    @Autowired
    private InvoiceBillService invoiceBillService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-04-22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody InvoiceBillParam invoiceBillParam) {
        Object add = this.invoiceBillService.add(invoiceBillParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-04-22
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody InvoiceBillParam invoiceBillParam) {

        this.invoiceBillService.update(invoiceBillParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-04-22
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody InvoiceBillParam invoiceBillParam)  {
        this.invoiceBillService.delete(invoiceBillParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-04-22
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody InvoiceBillParam invoiceBillParam) {
        InvoiceBill detail = this.invoiceBillService.getById(invoiceBillParam.getInvoiceBillId());
        InvoiceBillResult result = new InvoiceBillResult();
        ToolUtil.copyProperties(detail, result);
        invoiceBillService.format(new ArrayList<InvoiceBillResult>(){{
            add(result);
        }});


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-04-22
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<InvoiceBillResult> list(@RequestBody(required = false) InvoiceBillParam invoiceBillParam) {
        if(ToolUtil.isEmpty(invoiceBillParam)){
            invoiceBillParam = new InvoiceBillParam();
        }
        return this.invoiceBillService.findPageBySpec(invoiceBillParam);
    }




}


