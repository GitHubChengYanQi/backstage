package cn.atsoft.dasheng.storehouse.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.storehouse.entity.StorehouseSpuClassBind;
import cn.atsoft.dasheng.storehouse.model.params.StorehouseSpuClassBindParam;
import cn.atsoft.dasheng.storehouse.model.result.StorehouseSpuClassBindResult;
import cn.atsoft.dasheng.storehouse.service.StorehouseSpuClassBindService;
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
 * 仓库物料分类绑定表控制器
 *
 * @author Captain_Jazz
 * @Date 2023-05-15 13:44:50
 */
@RestController
@RequestMapping("/storehouseSpuClassBind")
@Api(tags = "仓库物料分类绑定表")
public class StorehouseSpuClassBindController extends BaseController {

    @Autowired
    private StorehouseSpuClassBindService storehouseSpuClassBindService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody StorehouseSpuClassBindParam storehouseSpuClassBindParam) {
        this.storehouseSpuClassBindService.add(storehouseSpuClassBindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody StorehouseSpuClassBindParam storehouseSpuClassBindParam) {
        StorehouseSpuClassBind detail = this.storehouseSpuClassBindService.getById(storehouseSpuClassBindParam.getStorehouseSpuClassBindId());
        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())){
            throw new ServiceException(500,"您无权操作该数据");
        }
        this.storehouseSpuClassBindService.update(storehouseSpuClassBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody StorehouseSpuClassBindParam storehouseSpuClassBindParam)  {
        this.storehouseSpuClassBindService.delete(storehouseSpuClassBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<StorehouseSpuClassBindResult> detail(@RequestBody StorehouseSpuClassBindParam storehouseSpuClassBindParam) {
        StorehouseSpuClassBind detail = this.storehouseSpuClassBindService.getById(storehouseSpuClassBindParam.getStorehouseSpuClassBindId());
        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())){
            throw new ServiceException(500,"您无权查看该数据");
        }
        StorehouseSpuClassBindResult result = new StorehouseSpuClassBindResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2023-05-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<StorehouseSpuClassBindResult> list(@RequestBody(required = false) StorehouseSpuClassBindParam storehouseSpuClassBindParam) {
        if(ToolUtil.isEmpty(storehouseSpuClassBindParam)){
            storehouseSpuClassBindParam = new StorehouseSpuClassBindParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
             return this.storehouseSpuClassBindService.findPageBySpec(storehouseSpuClassBindParam,null);
        } else {
             DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
             return  this.storehouseSpuClassBindService.findPageBySpec(storehouseSpuClassBindParam,dataScope);
        }
//        return this.storehouseSpuClassBindService.findPageBySpec(storehouseSpuClassBindParam);
    }




}


