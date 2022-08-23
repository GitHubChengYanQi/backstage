package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.app.model.result.AdressResult;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.production.entity.ShipSetp;
import cn.atsoft.dasheng.production.entity.Sop;
import cn.atsoft.dasheng.production.model.params.ShipSetpParam;
import cn.atsoft.dasheng.production.model.params.SopParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpResult;
import cn.atsoft.dasheng.production.service.ShipSetpService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.production.wrapper.ShipSetpSelectWrapper;
import cn.atsoft.dasheng.production.wrapper.SopSelectWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 工序表控制器
 *
 * @author song
 * @Date 2022-02-10 09:16:24
 */
@RestController
@RequestMapping("/shipSetp")
@Api(tags = "工序表")
public class ShipSetpController extends BaseController {

    @Autowired
    private ShipSetpService shipSetpService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ShipSetpParam ShipSetpParam) {
        this.shipSetpService.add(ShipSetpParam);
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
    public ResponseData update(@RequestBody ShipSetpParam shipSetpParam) {

        this.shipSetpService.update(shipSetpParam);
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
    public ResponseData delete(@RequestBody ShipSetpParam shipSetpParam)  {
        this.shipSetpService.delete(shipSetpParam);
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
    public ResponseData detail(@RequestBody ShipSetpParam shipSetpParam) {
        ShipSetp detail = this.shipSetpService.getById(shipSetpParam.getShipSetpId());
        ShipSetpResult  result= new ShipSetpResult();
        ToolUtil.copyProperties(detail,result);
        List<ShipSetpResult> results = new ArrayList<ShipSetpResult>(){{
            add(result);
        }};
        this.shipSetpService.format(results);

        return ResponseData.success(ToolUtil.isEmpty(results)? new ShipSetpResult() : results.get(0));
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<ShipSetpResult> list(@RequestBody(required = false) ShipSetpParam shipSetpParam) {
        if(ToolUtil.isEmpty(shipSetpParam)){
            shipSetpParam = new ShipSetpParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.shipSetpService.findPageBySpec(shipSetpParam, null);

        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.shipSetpService.findPageBySpec(shipSetpParam,dataScope);
        }

    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData listSelect(@RequestBody(required = false) SopParam sopParam) {

        QueryWrapper<ShipSetp> shipSetpQueryWrapper = new QueryWrapper<>();
        shipSetpQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.shipSetpService.listMaps(shipSetpQueryWrapper);
        ShipSetpSelectWrapper shipSetpSelectWrapper = new ShipSetpSelectWrapper(list);
        List<Map<String, Object>> result = shipSetpSelectWrapper.wrap();

        return ResponseData.success(result);
    }




}


