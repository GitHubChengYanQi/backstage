package cn.atsoft.dasheng.goods.texture.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.texture.entity.RestTextrue;
import cn.atsoft.dasheng.goods.texture.model.params.RestTextrueParam;
import cn.atsoft.dasheng.goods.texture.model.result.RestTextrueResult;
import cn.atsoft.dasheng.goods.texture.service.RestTextrueService;
import cn.atsoft.dasheng.goods.texture.wrapper.RestTextureSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 材质控制器
 *
 * @author 1
 * @Date 2021-07-14 14:56:44
 */
@RestController
@RequestMapping("/textrue/{version}")
@ApiVersion("2.0")
@Api(tags = "材质管理")
public class RestTextrueController extends BaseController {

    @Autowired
    private RestTextrueService restTextrueService;

    /**
     * 新增接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestTextrueParam restTextrueParam) {
        Long add = this.restTextrueService.add(restTextrueParam);
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
    public ResponseData update(@RequestBody RestTextrueParam restTextrueParam) {

        this.restTextrueService.update(restTextrueParam);
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
    public ResponseData delete(@RequestBody RestTextrueParam restTextrueParam) {
        this.restTextrueService.delete(restTextrueParam);
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
    public ResponseData detail(@RequestBody RestTextrueParam restTextrueParam) {
        RestTextrue detail = this.restTextrueService.getById(restTextrueParam.getMaterialId());
        RestTextrueResult result = new RestTextrueResult();
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
    public PageInfo list(@RequestBody(required = false) RestTextrueParam restTextrueParam) {
        if (ToolUtil.isEmpty(restTextrueParam)) {
            restTextrueParam = new RestTextrueParam();
        }
//        return this.materialService.findPageBySpec(materialParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.restTextrueService.findPageBySpec(restTextrueParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.restTextrueService.findPageBySpec(restTextrueParam, dataScope);
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
        QueryWrapper<RestTextrue> materialQueryWrapper = new QueryWrapper<>();
        materialQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.restTextrueService.listMaps(materialQueryWrapper);

        RestTextureSelectWrapper factory = new RestTextureSelectWrapper(list);

        List<Map<String, Object>> result = factory.wrap();

        return ResponseData.success(result);
    }


}


