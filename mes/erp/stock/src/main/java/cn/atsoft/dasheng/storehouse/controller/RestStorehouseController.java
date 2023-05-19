package cn.atsoft.dasheng.storehouse.controller;

import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.model.result.RestCategoryResult;
import cn.atsoft.dasheng.goods.category.service.RestCategoryService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.storehouse.entity.RestStorehouse;
import cn.atsoft.dasheng.storehouse.entity.StorehouseSpuClassBind;
import cn.atsoft.dasheng.storehouse.model.params.RestStorehouseParam;
import cn.atsoft.dasheng.storehouse.model.result.RestStorehouseResult;
import cn.atsoft.dasheng.storehouse.service.RestStorehouseService;
import cn.atsoft.dasheng.storehouse.service.StorehouseSpuClassBindService;
import cn.atsoft.dasheng.storehouse.wrapper.RestStorehouseSelectWrapper;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 地点表控制器
 *
 * @author
 * @Date 2021-07-15 11:13:02
 */
@RestController
@RequestMapping("/storehouse/{version}")
@Api(tags = "仓库")
@ApiVersion("2.0")
public class RestStorehouseController extends BaseController {

    @Autowired
    private RestStorehouseService storehouseService;
    @Autowired
    private StorehouseSpuClassBindService spuClassBindService;
    @Autowired
    private RestCategoryService categoryService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestStorehouseParam storehouseParam) {
        Long add = this.storehouseService.add(storehouseParam);
        if (ToolUtil.isNotEmpty(storehouseParam.getSpuClassIds())){
            List<StorehouseSpuClassBind> bind = new ArrayList<>();
            for (Long spuClassId : storehouseParam.getSpuClassIds()) {
                bind.add(new StorehouseSpuClassBind(){{
                    setStorehouseId(add);
                    setSpuClassId(spuClassId);
                }});
            }
            spuClassBindService.saveBatch(bind);
        }
        if (storehouseParam.getCapacity() == null && storehouseParam.getMeasure() == null) {
            return ResponseData.success(add);
        } else {
            if (storehouseParam.getCapacity() > 0 && storehouseParam.getMeasure() > 0) {
                return ResponseData.success(add);
            } else {
                return ResponseData.error("请输入正确值");
            }
        }

    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RestStorehouseParam storehouseParam) {

        this.storehouseService.updateRestStorehouse(storehouseParam);

        spuClassBindService.remove(new QueryWrapper<StorehouseSpuClassBind>().eq("storehouse_id", storehouseParam.getStorehouseId()));


        if (ToolUtil.isNotEmpty(storehouseParam.getSpuClassIds())){
            List<StorehouseSpuClassBind> bind = new ArrayList<>();
            for (Long spuClassId : storehouseParam.getSpuClassIds()) {
                bind.add(new StorehouseSpuClassBind(){{
                    setStorehouseId(storehouseParam.getStorehouseId());
                    setSpuClassId(spuClassId);
                }});
            }
            spuClassBindService.saveBatch(bind);
        }
        if (storehouseParam.getCapacity() == null && storehouseParam.getMeasure() == null) {
            return ResponseData.success();
        } else {
            if (storehouseParam.getCapacity() > 0 && storehouseParam.getMeasure() > 0) {
                return ResponseData.success();
            } else {
                return ResponseData.error("请输入正确值");
            }
        }
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-07-15
     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody RestStorehouseParam storehouseParam) {
//        this.storehouseService.delete(storehouseParam);
//        return ResponseData.success();
//    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody RestStorehouseParam storehouseParam) {
        RestStorehouse detail = this.storehouseService.getById(storehouseParam.getStorehouseId());
        RestStorehouseResult result = new RestStorehouseResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        List<StorehouseSpuClassBind> list = spuClassBindService.lambdaQuery().eq(StorehouseSpuClassBind::getStorehouseId, storehouseParam.getStorehouseId()).list();
        if (ToolUtil.isNotEmpty(list)) {
            List<Long> spuClassId = list.stream().map(StorehouseSpuClassBind::getSpuClassId).collect(Collectors.toList());
            List<RestCategory> restCategories = spuClassId.size() == 0 ? new ArrayList<>() : categoryService.listByIds(spuClassId);
            List<RestCategoryResult> restCategoryResults = BeanUtil.copyToList(restCategories, RestCategoryResult.class);
            result.setSpuClassResults(restCategoryResults);
        }

//        result.setValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    @Permission
    public PageInfo list(@RequestBody(required = false) RestStorehouseParam storehouseParam) {
        if (ToolUtil.isEmpty(storehouseParam)) {
            storehouseParam = new RestStorehouseParam();
        }
//        return this.storehouseService.findPageBySpec(storehouseParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.storehouseService.findPageBySpec(storehouseParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(), LoginContextHolder.getContext().getTenantId());
            return this.storehouseService.findPageBySpec(storehouseParam, dataScope);
        }
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    @ApiOperation("列表")
    public ResponseData sort(@RequestBody(required = false) RestStorehouseParam storehouseParam) {
        if (ToolUtil.isEmpty(storehouseParam) || ToolUtil.isEmpty(storehouseParam.getSortList())) {
            throw new ServiceException(500, "参数错误");
        }
        this.storehouseService.sort(storehouseParam.getSortList());
        return ResponseData.success();

    }

    /**
     * 选择列表
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(@RequestBody(required = false) RestStorehouseParam storehouseParam) {
        QueryWrapper<RestStorehouse> storehouseQueryWrapper = new QueryWrapper<>();
        storehouseQueryWrapper.in("display", 1);
        if (ToolUtil.isNotEmpty(storehouseParam) && ToolUtil.isNotEmpty(storehouseParam.getName())) {
            storehouseQueryWrapper.like("name", storehouseParam.getName());
        }
        List<Map<String, Object>> list = this.storehouseService.listMaps(storehouseQueryWrapper);
        RestStorehouseSelectWrapper factory = new RestStorehouseSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }

    /**
     * 选择列表
     *
     * @author
     * @Date 2021-07-15
     */
    @RequestMapping(value = "/tree", method = RequestMethod.POST)
    @ApiOperation("树形结构")
    public ResponseData tree() {

        List<RestStorehouseResult> restStorehouseTree = this.storehouseService.getRestStorehouseTree();
        //restStorehouseTree 按照sort 倒叙排序

        return ResponseData.success(restStorehouseTree);
    }


}


