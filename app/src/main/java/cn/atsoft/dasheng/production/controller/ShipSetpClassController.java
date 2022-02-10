package cn.atsoft.dasheng.production.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ShipSetpClass;
import cn.atsoft.dasheng.production.model.params.ShipSetpClassParam;
import cn.atsoft.dasheng.production.model.result.ShipSetpClassResult;
import cn.atsoft.dasheng.production.service.ShipSetpClassService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 工序分类表控制器
 *
 * @author song
 * @Date 2022-02-10 09:16:24
 */
@RestController
@RequestMapping("/ShipSetpClass")
@Api(tags = "工序分类表")
public class ShipSetpClassController extends BaseController {

    @Autowired
    private ShipSetpClassService shipSetpClassService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2022-02-10
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ShipSetpClassParam shipSetpClassParam) {
        this.shipSetpClassService.add(shipSetpClassParam);
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
    public ResponseData update(@RequestBody ShipSetpClassParam shipSetpClassParam) {

        this.shipSetpClassService.update(shipSetpClassParam);
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
    public ResponseData delete(@RequestBody ShipSetpClassParam shipSetpClassParam)  {
        this.shipSetpClassService.delete(shipSetpClassParam);
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
    public ResponseData<ShipSetpClassResult> detail(@RequestBody ShipSetpClassParam shipSetpClassParam) {
        ShipSetpClass detail = this.shipSetpClassService.getById(shipSetpClassParam.getShipSetpClassId());
        ShipSetpClassResult result = new ShipSetpClassResult();
        ToolUtil.copyProperties(detail, result);

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
    public PageInfo<ShipSetpClassResult> list(@RequestBody(required = false) ShipSetpClassParam shipSetpClassParam) {
        if(ToolUtil.isEmpty(shipSetpClassParam)){
            shipSetpClassParam = new ShipSetpClassParam();
        }
        return this.shipSetpClassService.findPageBySpec(shipSetpClassParam);
    }




}


