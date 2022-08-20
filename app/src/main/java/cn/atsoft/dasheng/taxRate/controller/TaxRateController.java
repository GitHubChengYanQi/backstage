package cn.atsoft.dasheng.taxRate.controller;

import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.wrapper.AdressSelectWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.taxRate.entity.TaxRate;
import cn.atsoft.dasheng.taxRate.model.params.TaxRateParam;
import cn.atsoft.dasheng.taxRate.model.result.TaxRateResult;
import cn.atsoft.dasheng.taxRate.service.TaxRateService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.taxRate.wrapper.TaxRateSelectWrapper;
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
 * @author
 * @Date 2021-12-21 11:29:07
 */
@RestController
@RequestMapping("/taxRate")
@Api(tags = "")
public class TaxRateController extends BaseController {

    @Autowired
    private TaxRateService taxRateService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody TaxRateParam taxRateParam) {
        this.taxRateService.add(taxRateParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody TaxRateParam taxRateParam) {

        this.taxRateService.update(taxRateParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody TaxRateParam taxRateParam) {
        this.taxRateService.delete(taxRateParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody TaxRateParam taxRateParam) {
        TaxRate detail = this.taxRateService.getById(taxRateParam.getTaxRateId());
        TaxRateResult result = new TaxRateResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-12-21
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<TaxRateResult> list(@RequestBody(required = false) TaxRateParam taxRateParam) {
        if (ToolUtil.isEmpty(taxRateParam)) {
            taxRateParam = new TaxRateParam();
        }
        return this.taxRateService.findPageBySpec(taxRateParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<TaxRate> taxRateQueryWrapper = new QueryWrapper<>();
        taxRateQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.taxRateService.listMaps(taxRateQueryWrapper);
        TaxRateSelectWrapper taxRateSelectWrapper = new TaxRateSelectWrapper(list);
        List<Map<String, Object>> result = taxRateSelectWrapper.wrap();
        return ResponseData.success(result);
    }
}


