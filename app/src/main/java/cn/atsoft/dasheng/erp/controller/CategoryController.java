package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.model.params.CategoryParam;
import cn.atsoft.dasheng.erp.model.result.CategoryResult;
import cn.atsoft.dasheng.erp.service.CategoryService;
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
 * 物品分类表控制器
 *
 * @author 
 * @Date 2021-10-18 10:54:16
 */
@RestController
@RequestMapping("/category")
@Api(tags = "物品分类表")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CategoryParam categoryParam) {
        this.categoryService.add(categoryParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CategoryParam categoryParam) {

        this.categoryService.update(categoryParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CategoryParam categoryParam)  {
        this.categoryService.delete(categoryParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CategoryResult> detail(@RequestBody CategoryParam categoryParam) {
        Category detail = this.categoryService.getById(categoryParam.getCategoryId());
        CategoryResult result = new CategoryResult();
        ToolUtil.copyProperties(detail, result);

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CategoryResult> list(@RequestBody(required = false) CategoryParam categoryParam) {
        if(ToolUtil.isEmpty(categoryParam)){
            categoryParam = new CategoryParam();
        }
        return this.categoryService.findPageBySpec(categoryParam);
    }




}


