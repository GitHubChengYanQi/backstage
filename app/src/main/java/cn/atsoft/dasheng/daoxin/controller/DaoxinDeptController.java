package cn.atsoft.dasheng.daoxin.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.daoxin.entity.DaoxinDept;
import cn.atsoft.dasheng.daoxin.model.params.DaoxinDeptParam;
import cn.atsoft.dasheng.daoxin.model.result.DaoxinDeptResult;
import cn.atsoft.dasheng.daoxin.service.DaoxinDeptService;
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
 * daoxin部门表控制器
 *
 * @author Captain_Jazz
 * @Date 2022-02-17 15:10:45
 */
@RestController
@RequestMapping("/daoxinDept")
@Api(tags = "daoxin部门表")
public class DaoxinDeptController extends BaseController {

    @Autowired
    private DaoxinDeptService daoxinDeptService;

    /**
     * 新增接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody DaoxinDeptParam daoxinDeptParam) {
        this.daoxinDeptService.add(daoxinDeptParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody DaoxinDeptParam daoxinDeptParam) {

        this.daoxinDeptService.update(daoxinDeptParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody DaoxinDeptParam daoxinDeptParam)  {
        this.daoxinDeptService.delete(daoxinDeptParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody DaoxinDeptParam daoxinDeptParam) {
        DaoxinDept detail = this.daoxinDeptService.getById(daoxinDeptParam.getDeptId());
        DaoxinDeptResult result = new DaoxinDeptResult();
        ToolUtil.copyProperties(detail, result);


        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author Captain_Jazz
     * @Date 2022-02-17
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<DaoxinDeptResult> list(@RequestBody(required = false) DaoxinDeptParam daoxinDeptParam) {
        if(ToolUtil.isEmpty(daoxinDeptParam)){
            daoxinDeptParam = new DaoxinDeptParam();
        }
        return this.daoxinDeptService.findPageBySpec(daoxinDeptParam);
    }




}


