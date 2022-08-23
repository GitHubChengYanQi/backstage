package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.OutstockRequest;
import cn.atsoft.dasheng.app.service.StockDetailsService;
import cn.atsoft.dasheng.app.service.StockService;
import cn.atsoft.dasheng.app.wrapper.OutstockSelectWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Outstock;
import cn.atsoft.dasheng.app.model.params.OutstockParam;
import cn.atsoft.dasheng.app.model.result.OutstockResult;
import cn.atsoft.dasheng.app.service.OutstockService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;


/**
 * 出库表控制器
 *
 * @author song
 * @Date 2021-07-17 10:46:08
 */
@RestController
@RequestMapping("/outstock")
@Api(tags = "出库表")
public class OutstockController extends BaseController {

    @Autowired
    private OutstockService outstockService;




    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    @Permission
    public ResponseData addItem(@RequestBody OutstockParam outstockParam) {

        outstockService.add(outstockParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    @Permission
    public ResponseData update(@RequestBody OutstockParam outstockParam) {

        this.outstockService.update(outstockParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    @Permission
    public ResponseData delete(@RequestBody OutstockParam outstockParam) {
        this.outstockService.delete(outstockParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    @Permission
    public ResponseData detail(@RequestBody OutstockParam outstockParam) {
        Long outstockId = outstockParam.getOutstockId();
        OutstockResult detail = outstockService.detail(outstockId);
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-07-17
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo list(@RequestBody(required = false) OutstockParam outstockParam) {
        if (ToolUtil.isEmpty(outstockParam)) {
            outstockParam = new OutstockParam();
        }
//        return this.outstockService.findPageBySpec(outstockParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.outstockService.findPageBySpec(outstockParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.outstockService.findPageBySpec(outstockParam, dataScope);
        }
    }


    /**
     * 选择列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    @Permission
    public ResponseData listSelect() {
        QueryWrapper<Outstock> outstockQueryWrapper = new QueryWrapper<>();
        outstockQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.outstockService.listMaps(outstockQueryWrapper);
        OutstockSelectWrapper factory = new OutstockSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }

    @RequestMapping(value = "/bulkShipment", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public ResponseData bulkShipment(@RequestBody OutstockRequest outstockRequest) {
        System.err.println(outstockRequest);

        return ResponseData.success();
    }

}
