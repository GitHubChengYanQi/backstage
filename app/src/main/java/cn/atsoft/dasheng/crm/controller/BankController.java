package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Bank;
import cn.atsoft.dasheng.crm.entity.Data;
import cn.atsoft.dasheng.crm.model.params.BankParam;
import cn.atsoft.dasheng.crm.model.result.BankResult;
import cn.atsoft.dasheng.crm.service.BankService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.wrapper.BankSelectWrapper;
import cn.atsoft.dasheng.crm.wrapper.DataSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * @author song
 * @Date 2022-02-24 14:55:10
 */
@RestController
@RequestMapping("/bank")
@Api(tags = "")
public class BankController extends BaseController {

    @Autowired
    private BankService bankService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-24
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody BankParam bankParam) {
        Bank bank = this.bankService.add(bankParam);
        return ResponseData.success(bank);
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-02-24
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody BankParam bankParam) {

        this.bankService.update(bankParam);
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
    public ResponseData delete(@RequestBody BankParam bankParam) {
        this.bankService.delete(bankParam);
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
    public ResponseData<BankResult> detail(@RequestBody BankParam bankParam) {
        Bank detail = this.bankService.getById(bankParam.getBankId());
        BankResult result = new BankResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
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
    public PageInfo<BankResult> list(@RequestBody(required = false) BankParam bankParam) {
        if (ToolUtil.isEmpty(bankParam)) {
            bankParam = new BankParam();
        }
        return this.bankService.findPageBySpec(bankParam);
    }

    /**
     * 选择列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect() {
        List<Map<String, Object>> list = this.bankService.listMaps();
        BankSelectWrapper dataSelectWrapper = new BankSelectWrapper(list);
        List<Map<String, Object>> result = dataSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}


