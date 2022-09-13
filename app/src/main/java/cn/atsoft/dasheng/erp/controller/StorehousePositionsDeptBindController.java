package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StorehousePositionsDeptBind;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsDeptBindParam;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsDeptBindResult;
import cn.atsoft.dasheng.erp.service.StorehousePositionsDeptBindService;
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
 * 库位权限绑定表控制器
 *
 * @author
 * @Date 2022-01-25 09:24:15
 */
@RestController
@RequestMapping("/storehousePositionsDeptBind")
@Api(tags = "库位权限绑定表")
public class StorehousePositionsDeptBindController extends BaseController {

    @Autowired
    private StorehousePositionsDeptBindService storehousePositionsDeptBindService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2022-01-25
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody StorehousePositionsDeptBindParam storehousePositionsDeptBindParam) {
        this.storehousePositionsDeptBindService.add(storehousePositionsDeptBindParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/getDeptIds", method = RequestMethod.GET)
    @ApiOperation("新增")
    public List<String> getDeptIds(Long positionId) {
        List<String> deptIds = new ArrayList<>();
        if (ToolUtil.isNotEmpty(positionId)) {
            StorehousePositionsDeptBind detail = storehousePositionsDeptBindService.lambdaQuery().eq(StorehousePositionsDeptBind::getStorehousePositionsId, positionId).one();
            if (ToolUtil.isNotEmpty(detail)) {
                String[] split = detail.getDeptId().split(",");
                for (String deptId : split) {
                    deptIds.add(deptId);
                }
            }
        }
        return deptIds;
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2022-01-25
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody StorehousePositionsDeptBindParam storehousePositionsDeptBindParam) {

        this.storehousePositionsDeptBindService.update(storehousePositionsDeptBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2022-01-25
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody StorehousePositionsDeptBindParam storehousePositionsDeptBindParam) {
        this.storehousePositionsDeptBindService.delete(storehousePositionsDeptBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2022-01-25
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody StorehousePositionsDeptBindParam storehousePositionsDeptBindParam) {
        StorehousePositionsDeptBind detail = this.storehousePositionsDeptBindService.getById(storehousePositionsDeptBindParam.getBindId());
        StorehousePositionsDeptBindResult result = new StorehousePositionsDeptBindResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2022-01-25
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<StorehousePositionsDeptBindResult> list(@RequestBody(required = false) StorehousePositionsDeptBindParam storehousePositionsDeptBindParam) {
        if (ToolUtil.isEmpty(storehousePositionsDeptBindParam)) {
            storehousePositionsDeptBindParam = new StorehousePositionsDeptBindParam();
        }
        return this.storehousePositionsDeptBindService.findPageBySpec(storehousePositionsDeptBindParam);
    }


}


