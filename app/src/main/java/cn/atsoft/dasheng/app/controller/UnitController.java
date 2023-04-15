package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.entity.Template;
import cn.atsoft.dasheng.app.wrapper.TemplateSelectWrapper;
import cn.atsoft.dasheng.app.wrapper.UnitSelectWrapper;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.params.UnitParam;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.app.service.UnitService;
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
 * 单位表控制器
 *
 * @author cheng
 * @Date 2021-08-11 15:37:57
 */
@RestController
@RequestMapping("/unit")
@Api(tags = "单位表")
public class UnitController extends BaseController {

    @Autowired
    private UnitService unitService;

    /**
     * 新增接口
     *
     * @author cheng
     * @Date 2021-08-11
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody UnitParam unitParam) {

        return ResponseData.success(this.unitService.add(unitParam));
    }

    /**
     * 编辑接口
     *
     * @author cheng
     * @Date 2021-08-11
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody UnitParam unitParam) {

        this.unitService.update(unitParam);
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
    public ResponseData delete(@RequestBody UnitParam unitParam)  {
        this.unitService.delete(unitParam);
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
    public ResponseData detail(@RequestBody UnitParam unitParam) {
        Unit detail = this.unitService.getById(unitParam.getUnitId());
        UnitResult result = new UnitResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

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
    public PageInfo list(@RequestBody(required = false) UnitParam unitParam) {
        if(ToolUtil.isEmpty(unitParam)){
            unitParam = new UnitParam();
        }
//        return this.unitService.findPageBySpec(unitParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.unitService.findPageBySpec(unitParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.unitService.findPageBySpec(unitParam, dataScope);
        }
    }

    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    public ResponseData listSelect() {
        List<Map<String, Object>> list = this.unitService.listMaps();
        UnitSelectWrapper unitSelectWrapper = new UnitSelectWrapper(list);
        List<Map<String, Object>> result = unitSelectWrapper.wrap();
        return ResponseData.success(result);
    }


}


