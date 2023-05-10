package cn.atsoft.dasheng.sys.modular.system.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.PositionBind;
import cn.atsoft.dasheng.sys.modular.system.model.params.PositionBindParam;
import cn.atsoft.dasheng.sys.modular.system.model.result.PositionBindResult;
import cn.atsoft.dasheng.sys.modular.system.service.PositionBindService;
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
 * 租户用户位置绑定表控制器
 *
 * @author Captain_Jazz
 * @Date 2023-05-09 11:36:02
 */
@RestController
@RequestMapping("/positionBind")
@Api(tags = "租户用户位置绑定表")
public class PositionBindController extends BaseController {

    @Autowired
    private PositionBindService positionBindService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PositionBindParam positionBindParam) {
        this.positionBindService.add(positionBindParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PositionBindParam positionBindParam) {
        PositionBind detail = this.positionBindService.getById(positionBindParam.getPositionBindId());
        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())){
            throw new ServiceException(500,"您无权操作该数据");
        }
        this.positionBindService.update(positionBindParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PositionBindParam positionBindParam)  {
        this.positionBindService.delete(positionBindParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<PositionBindResult> detail(@RequestBody PositionBindParam positionBindParam) {
        PositionBind detail = this.positionBindService.getById(positionBindParam.getPositionBindId());
        if (!LoginContextHolder.getContext().getTenantId().equals(detail.getTenantId())){
            throw new ServiceException(500,"您无权查看该数据");
        }
        PositionBindResult result = new PositionBindResult();
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
     * @Date 2023-05-09
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PositionBindResult> list(@RequestBody(required = false) PositionBindParam positionBindParam) {
        if(ToolUtil.isEmpty(positionBindParam)){
            positionBindParam = new PositionBindParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
             return this.positionBindService.findPageBySpec(positionBindParam,null);
        } else {
             DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
             return  this.positionBindService.findPageBySpec(positionBindParam,dataScope);
        }
//        return this.positionBindService.findPageBySpec(positionBindParam);
    }




}


