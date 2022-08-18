package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ItemClass;
import cn.atsoft.dasheng.app.model.params.ItemClassParam;
import cn.atsoft.dasheng.app.model.result.ItemClassResult;
import cn.atsoft.dasheng.app.service.ItemClassService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 产品分类表控制器
 *
 * @author cheng
 * @Date 2021-08-11 15:37:57
 */
@RestController
@RequestMapping("/itemClass")
@Api(tags = "产品分类表")
public class ItemClassController extends BaseController {

    @Autowired
    private ItemClassService itemClassService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-08-11
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody ItemClassParam itemClassParam) {
        this.itemClassService.add(itemClassParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-08-11
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody ItemClassParam itemClassParam) {

        this.itemClassService.update(itemClassParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author cheng
     * @Date 2021-08-11
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody ItemClassParam itemClassParam)  {
        this.itemClassService.delete(itemClassParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author cheng
     * @Date 2021-08-11
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody ItemClassParam itemClassParam) {
        ItemClass detail = this.itemClassService.getById(itemClassParam.getClassId());
        ItemClassResult result = new ItemClassResult();
        ToolUtil.copyProperties(detail, result);

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author cheng
     * @Date 2021-08-11
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) ItemClassParam itemClassParam) {
        if(ToolUtil.isEmpty(itemClassParam)){
            itemClassParam = new ItemClassParam();
        }
//        return this.itemClassService.findPageBySpec(itemClassParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.itemClassService.findPageBySpec(itemClassParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.itemClassService.findPageBySpec(itemClassParam, dataScope);
        }
    }




}


