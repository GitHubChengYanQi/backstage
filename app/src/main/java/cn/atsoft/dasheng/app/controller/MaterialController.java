package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Material;
import cn.atsoft.dasheng.app.model.params.MaterialParam;
import cn.atsoft.dasheng.app.model.result.MaterialResult;
import cn.atsoft.dasheng.app.service.MaterialService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.app.wrapper.MaterialSelectWrapper;

import java.util.List;
import java.util.Map;


/**
 * 材质控制器
 *
 * @author 1
 * @Date 2021-07-14 14:56:44
 */
@RestController
@RequestMapping("/material")
@Api(tags = "材质")
public class MaterialController extends BaseController {

    @Autowired
    private MaterialService materialService;

    /**
     * 新增接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody MaterialParam materialParam) {
        Long add = this.materialService.add(materialParam);
        return ResponseData.success(add);
    }

    /**
     * 编辑接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody MaterialParam materialParam) {

        this.materialService.update(materialParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody MaterialParam materialParam)  {
        this.materialService.delete(materialParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody MaterialParam materialParam) {
        Material detail = this.materialService.getById(materialParam.getMaterialId());
        MaterialResult result = new MaterialResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
//
//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) MaterialParam materialParam) {
        if(ToolUtil.isEmpty(materialParam)){
            materialParam = new MaterialParam();
        }
//        return this.materialService.findPageBySpec(materialParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.materialService.findPageBySpec(materialParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.materialService.findPageBySpec(materialParam, dataScope);
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
    public ResponseData listSelect() {
        QueryWrapper<Material> materialQueryWrapper = new QueryWrapper<>();
        materialQueryWrapper.in("display",1);
        List<Map<String,Object>> list = this.materialService.listMaps(materialQueryWrapper);

        MaterialSelectWrapper factory = new MaterialSelectWrapper(list);

        List<Map<String,Object>> result = factory.wrap();

        return ResponseData.success(result);
    }


}


