package cn.atsoft.dasheng.crm.controller;

import cn.atsoft.dasheng.app.entity.Adress;
import cn.atsoft.dasheng.app.wrapper.AdressSelectWrapper;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.crm.model.params.SupplyParam;
import cn.atsoft.dasheng.crm.model.result.SupplyResult;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.wrapper.SupplySelectWrapper;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 供应商供应物料控制器
 *
 * @author song
 * @Date 2021-12-20 10:08:44
 */
@RestController
@RequestMapping("/supply")
@Api(tags = "供应商供应物料")
public class SupplyController extends BaseController {

    @Autowired
    private SupplyService supplyService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SupplyParam supplyParam) {
        this.supplyService.add(supplyParam);
        return ResponseData.success();
    }

    /**
     * 批量增加
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/addList", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addList(@RequestBody SupplyParam supplyParam) {
        this.supplyService.addList(supplyParam.getSupplyParams(), supplyParam.getCustomerId());
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SupplyParam supplyParam) {

        this.supplyService.update(supplyParam);
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
    public ResponseData delete(@RequestBody SupplyParam supplyParam) {
        this.supplyService.delete(supplyParam);
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
    public ResponseData detail(@RequestBody SupplyParam supplyParam) {
        if (ToolUtil.isNotEmpty(supplyParam.getSkuId())) {
            throw new ServiceException(500, "请确认客户");
        }
        List<SupplyResult> detail = supplyService.detail(supplyParam.getCustomerId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-12-20
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SupplyResult> list(@RequestBody(required = false) SupplyParam supplyParam) {
        if (ToolUtil.isEmpty(supplyParam)) {
            supplyParam = new SupplyParam();
        }
        return this.supplyService.findPageBySpec(supplyParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect(@RequestBody(required = false) SupplyParam supplyParam) {
        QueryWrapper<Supply> supplyQueryWrapper = new QueryWrapper<>();
        supplyQueryWrapper.in("display", 1);
        if (ToolUtil.isNotEmpty(supplyParam.getCustomerId())) {
            supplyQueryWrapper.eq("customer_id", supplyParam.getCustomerId());
        }
        List<Map<String, Object>> list = this.supplyService.listMaps(supplyQueryWrapper);
        SupplySelectWrapper supplySelectWrapper = new SupplySelectWrapper(list);
        List<Map<String, Object>> result = supplySelectWrapper.wrap();
        return ResponseData.success(result);
    }


}

