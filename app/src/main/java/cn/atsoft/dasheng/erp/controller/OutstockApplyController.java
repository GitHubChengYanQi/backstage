package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.OutstockApply;
import cn.atsoft.dasheng.erp.model.params.ApplyDetailsParam;
import cn.atsoft.dasheng.erp.model.params.ItemAttributeParam;
import cn.atsoft.dasheng.erp.model.params.OutstockApplyParam;
import cn.atsoft.dasheng.erp.model.result.OutstockApplyResult;
import cn.atsoft.dasheng.erp.service.OutstockApplyService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 出库申请控制器
 *
 * @author song
 * @Date 2021-09-14 16:49:41
 */
@RestController
@RequestMapping("/outstockApply")
@Api(tags = "出库申请")
public class OutstockApplyController extends BaseController {

    @Autowired
    private OutstockApplyService outstockApplyService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-09-14
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OutstockApplyParam outstockApplyParam) {
        Map<Object, Object> map = new HashMap<>();
        long l = 0L;
        for (ApplyDetailsParam applyDetail : outstockApplyParam.getApplyDetails()) {
            l = applyDetail.getItemId() + applyDetail.getBrandId();
            if (map.containsKey(applyDetail.getItemId() + applyDetail.getBrandId())) {
                throw new ServiceException(500, "请不要填写相同产品");
            }
            map.put(l, applyDetail.getNumber());
        }

        this.outstockApplyService.add(outstockApplyParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-09-14
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改出库申请", key = "name", dict = OutstockApplyParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody OutstockApplyParam outstockApplyParam) {
        this.outstockApplyService.update(outstockApplyParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-09-14
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除出库申请", key = "name", dict = OutstockApplyParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody OutstockApplyParam outstockApplyParam) {
        this.outstockApplyService.delete(outstockApplyParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-09-14
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody OutstockApplyParam outstockApplyParam) {
        PageInfo<OutstockApplyResult> pageBySpec = this.outstockApplyService.findPageBySpec(outstockApplyParam);
        return ResponseData.success(pageBySpec.getData().get(0));
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-09-14
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<OutstockApplyResult> list(@RequestBody(required = false) OutstockApplyParam outstockApplyParam) {
        if (ToolUtil.isEmpty(outstockApplyParam)) {
            outstockApplyParam = new OutstockApplyParam();
        }
        return this.outstockApplyService.findPageBySpec(outstockApplyParam);
    }


}


