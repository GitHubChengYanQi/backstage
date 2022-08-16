package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ShipSetpBind;
import cn.atsoft.dasheng.production.model.params.ShipSetpBindParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpBindResult;
import cn.atsoft.dasheng.production.service.ShipSetpBindService;
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
 * 工序关联绑定工具与设备表控制器
 *
 * @author song
 * @Date 2022-02-10 11:08:15
 */
@RestController
@RequestMapping("/shipSetpBind")
@Api(tags = "工序关联绑定工具与设备表")
public class ShipSetpBindController extends BaseController {

    @Autowired
    private ShipSetpBindService shipSetpBindService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ShipSetpBindParam shipSetpBindParam) {
        this.shipSetpBindService.add(shipSetpBindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ShipSetpBindParam shipSetpBindParam) {

        this.shipSetpBindService.update(shipSetpBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ShipSetpBindParam shipSetpBindParam)  {
        this.shipSetpBindService.delete(shipSetpBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ShipSetpBindParam shipSetpBindParam) {
        ShipSetpBind detail = this.shipSetpBindService.getById(shipSetpBindParam.getShipSetpBindId());
        ShipSetpBindResult result = new ShipSetpBindResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ShipSetpBindResult> list(@RequestBody(required = false) ShipSetpBindParam shipSetpBindParam) {
        if(ToolUtil.isEmpty(shipSetpBindParam)){
            shipSetpBindParam = new ShipSetpBindParam();
        }
        return this.shipSetpBindService.findPageBySpec(shipSetpBindParam);
    }




}


