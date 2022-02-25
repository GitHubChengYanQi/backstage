package cn.atsoft.dasheng.template.controller;

import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.wrapper.AdressSelectWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.template.entity.PaymentTemplate;
import cn.atsoft.dasheng.template.model.params.PaymentTemplateParam;
import cn.atsoft.dasheng.template.model.result.PaymentTemplateResult;
import cn.atsoft.dasheng.template.service.PaymentTemplateService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.template.wrapper.PaymentTemplateSelectWrapper;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 付款模板控制器
 *
 * @author song
 * @Date 2022-02-24 10:36:06
 */
@RestController
@RequestMapping("/paymentTemplate")
@Api(tags = "付款模板")
public class PaymentTemplateController extends BaseController {

    @Autowired
    private PaymentTemplateService paymentTemplateService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-24
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PaymentTemplateParam paymentTemplateParam) {
        this.paymentTemplateService.add(paymentTemplateParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-02-24
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PaymentTemplateParam paymentTemplateParam) {

        this.paymentTemplateService.update(paymentTemplateParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-02-24
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PaymentTemplateParam paymentTemplateParam) {
        this.paymentTemplateService.delete(paymentTemplateParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-02-24
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<PaymentTemplateResult> detail(@RequestBody(required = false) PaymentTemplateParam paymentTemplateParam) {
        PaymentTemplateResult result = this.paymentTemplateService.detail(paymentTemplateParam.getTemplateId());
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-24
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PaymentTemplateResult> list(@RequestBody(required = false) PaymentTemplateParam paymentTemplateParam) {
        if (ToolUtil.isEmpty(paymentTemplateParam)) {
            paymentTemplateParam = new PaymentTemplateParam();
        }
        return this.paymentTemplateService.findPageBySpec(paymentTemplateParam);
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect(@RequestBody(required = false) PaymentTemplateParam param) {
        QueryWrapper<PaymentTemplate> queryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(param.getOftenUser())) {
            queryWrapper.in("often_user", param.getOftenUser());
        }
        queryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.paymentTemplateService.listMaps(queryWrapper);
        PaymentTemplateSelectWrapper selectWrapper = new PaymentTemplateSelectWrapper(list);
        List<Map<String, Object>> result = selectWrapper.wrap();
        return ResponseData.success(result);
    }

}


