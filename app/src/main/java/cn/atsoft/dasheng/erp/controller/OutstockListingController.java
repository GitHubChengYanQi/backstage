package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.OutstockListing;
import cn.atsoft.dasheng.erp.model.params.OutstockApplyParam;
import cn.atsoft.dasheng.erp.model.params.OutstockListingParam;
import cn.atsoft.dasheng.erp.model.result.OutstockListingResult;
import cn.atsoft.dasheng.erp.service.OutstockListingService;
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
 * 出库清单控制器
 *
 * @author cheng
 * @Date 2021-09-15 11:15:44
 */
@RestController
@RequestMapping("/outstockListing")
@Api(tags = "出库清单")
public class OutstockListingController extends BaseController {

    @Autowired
    private OutstockListingService outstockListingService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-09-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OutstockListingParam outstockListingParam) {
        this.outstockListingService.add(outstockListingParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-09-15
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改出库清单", key = "name", dict = OutstockListingParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody OutstockListingParam outstockListingParam) {

        this.outstockListingService.update(outstockListingParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2021-09-15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除出库清单", key = "name", dict = OutstockListingParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OutstockListingParam outstockListingParam) {
        this.outstockListingService.delete(outstockListingParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-09-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody OutstockListingParam outstockListingParam) {
        OutstockListing detail = this.outstockListingService.getById(outstockListingParam.getOutstockListingId());
        OutstockListingResult result = new OutstockListingResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-09-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OutstockListingResult> list(@RequestBody(required = false) OutstockListingParam outstockListingParam) {
        if (ToolUtil.isEmpty(outstockListingParam)) {
            outstockListingParam = new OutstockListingParam();
        }
        return this.outstockListingService.findPageBySpec(outstockListingParam);
    }


}


