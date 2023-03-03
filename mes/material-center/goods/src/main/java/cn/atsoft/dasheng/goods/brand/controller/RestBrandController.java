package cn.atsoft.dasheng.goods.brand.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.brand.entity.RestBrand;
import cn.atsoft.dasheng.goods.brand.model.params.RestBrandParam;
import cn.atsoft.dasheng.goods.brand.model.result.RestBrandResult;
import cn.atsoft.dasheng.goods.brand.service.RestBrandService;
import cn.atsoft.dasheng.goods.brand.wrapper.RestBrandSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand/{version}")
@ApiVersion("2.0")
@Api(tags = "品牌管理")
public class RestBrandController extends BaseController {
    @Autowired
    private RestBrandService restBrandService;

    /**
     * 新增接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestBrandParam restBrandParam) {
        Long add = this.restBrandService.add(restBrandParam);
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
    public ResponseData update(@RequestBody RestBrandParam restBrandParam) {

        this.restBrandService.update(restBrandParam);
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
    public ResponseData delete(@RequestBody RestBrandParam restBrandParam) {
        this.restBrandService.delete(restBrandParam);
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
    public ResponseData detail(@RequestBody RestBrandParam restBrandParam) {
        RestBrand detail = this.restBrandService.getById(restBrandParam.getBrandId());
        RestBrandResult result = new RestBrandResult();
        ToolUtil.copyProperties(detail, result);
        restBrandService.format(new ArrayList<RestBrandResult>() {{
            add(result);
        }});
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
    public PageInfo list(@RequestBody(required = false) RestBrandParam restBrandParam) {
        if (ToolUtil.isEmpty(restBrandParam)) {
            restBrandParam = new RestBrandParam();
        }

//        if (LoginContextHolder.getContext().isAdmin()) {
        return this.restBrandService.findPageBySpec(restBrandParam, null);
//        } else {
//            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
//            brandService.findPageBySpec(brandParam, dataScope);
//            return this.brandService.findPageBySpec(brandParam, dataScope);
//        }
    }


    @RequestMapping(value = "/pureList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo pureList(@RequestBody(required = false) RestBrandParam restBrandParam) {
        if (ToolUtil.isEmpty(restBrandParam)) {
            restBrandParam = new RestBrandParam();
        }
        return this.restBrandService.pureList(restBrandParam, null);
    }


    /**
     * 选择列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(@RequestBody(required = false) RestBrandParam restBrandParam) {
        QueryWrapper<RestBrand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.eq("display", 1);
        if (ToolUtil.isNotEmpty(restBrandParam)) {
            if (restBrandParam.getIds() != null) {
                if (restBrandParam.getIds().size() == 0) {
                    return ResponseData.success(new ArrayList<>());
                }
                brandQueryWrapper.in("brand_id", restBrandParam.getIds());
            }
        }
        List<Map<String, Object>> list = this.restBrandService.listMaps(brandQueryWrapper);
        RestBrandSelectWrapper factory = new RestBrandSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}

