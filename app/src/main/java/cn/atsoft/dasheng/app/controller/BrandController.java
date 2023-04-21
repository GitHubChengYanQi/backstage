package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.model.params.BrandParam;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BrandService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.app.wrapper.BrandSelectWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 品牌表控制器
 *
 * @author 1
 * @Date 2021-07-14 14:56:44
 */
@RestController
@RequestMapping("/brand")
@Api(tags = "品牌表")
public class BrandController extends BaseController {

    @Autowired
    private BrandService brandService;

    /**
     * 新增接口
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody BrandParam brandParam) {
        Long add = this.brandService.add(brandParam);
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
    public ResponseData update(@RequestBody BrandParam brandParam) {

        this.brandService.update(brandParam);
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
    public ResponseData delete(@RequestBody BrandParam brandParam) {
        this.brandService.delete(brandParam);
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
    public ResponseData detail(@RequestBody BrandParam brandParam) {
        Brand detail = this.brandService.getById(brandParam.getBrandId());
        BrandResult result = new BrandResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        brandService.format(new ArrayList<BrandResult>(){{
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
    public PageInfo list(@RequestBody(required = false) BrandParam brandParam) {
        if (ToolUtil.isEmpty(brandParam)) {
            brandParam = new BrandParam();
        }

//        if (LoginContextHolder.getContext().isAdmin()) {
            return this.brandService.findPageBySpec(brandParam, null);
//        } else {
//            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
//            brandService.findPageBySpec(brandParam, dataScope);
//            return this.brandService.findPageBySpec(brandParam, dataScope);
//        }
    }


    @RequestMapping(value = "/pureList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo pureList(@RequestBody(required = false) BrandParam brandParam) {
        if (ToolUtil.isEmpty(brandParam)) {
            brandParam = new BrandParam();
        }
        return this.brandService.pureList(brandParam, null);
    }


    /**
     * 选择列表
     *
     * @author 1
     * @Date 2021-07-14
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(@RequestBody(required = false) BrandParam brandParam) {
        QueryWrapper<Brand> brandQueryWrapper = new QueryWrapper<>();
        brandQueryWrapper.eq("display", 1);
        if (ToolUtil.isNotEmpty(brandParam)){
            if (brandParam.getIds() != null){
                if (brandParam.getIds().size() == 0){
                    return ResponseData.success(new ArrayList<>());
                }
                brandQueryWrapper.in("brand_id",brandParam.getIds());
            }
        }
        List<Map<String, Object>> list = this.brandService.listMaps(brandQueryWrapper);
        BrandSelectWrapper factory = new BrandSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }


}


