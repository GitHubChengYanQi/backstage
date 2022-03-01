package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AttributeValues;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.entity.ItemAttribute;
import cn.atsoft.dasheng.erp.model.params.AttributeValuesParam;
import cn.atsoft.dasheng.erp.model.params.CategoryParam;
import cn.atsoft.dasheng.erp.model.params.CategoryRequest;

import cn.atsoft.dasheng.erp.model.params.ItemAttributeParam;
import cn.atsoft.dasheng.erp.model.result.CategoryResult;
import cn.atsoft.dasheng.erp.service.AttributeValuesService;
import cn.atsoft.dasheng.erp.service.CategoryService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.ItemAttributeService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.erp.wrapper.CategorySelectWrapper;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 物品分类表控制器
 *
 * @author jazz
 * @Date 2021-10-18 14:35:39
 */
@RestController
@RequestMapping("/category")
@Api(tags = "物品分类表")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ItemAttributeService itemAttributeService;
    @Autowired
    private AttributeValuesService attributeValuesService;

    /**
     * 新增接口
     *
     * @author jazz
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
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改类目", key = "name", dict = CategoryParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CategoryParam categoryParam) {

        this.categoryService.update(categoryParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除类目", key = "name", dict = CategoryParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CategoryParam categoryParam) {
        this.categoryService.delete(categoryParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CategoryResult> detail(@RequestBody CategoryParam categoryParam) {
        Category detail = this.categoryService.getById(categoryParam.getCategoryId());
        CategoryResult result = new CategoryResult();
        List<CategoryRequest> categoryRequests = new ArrayList<>();
        List<ItemAttributeParam> itemAttributeParamList = new ArrayList<>();
        if (ToolUtil.isNotEmpty(detail)) {

            List<ItemAttribute> itemAttributes = itemAttributeService.lambdaQuery()
                    .in(ItemAttribute::getCategoryId, detail.getCategoryId()).orderByDesc(ItemAttribute::getSort)
                    .list();

            if (ToolUtil.isNotEmpty(itemAttributes)) {
                List<Long> attId = new ArrayList<>();
                for (ItemAttribute itemAttribute : itemAttributes) {
                    attId.add(itemAttribute.getAttributeId());
                }
                List<AttributeValues> attributeValues = attributeValuesService.lambdaQuery()
                        .in(AttributeValues::getAttributeId, attId)
                        .list();

                for (ItemAttribute itemAttribute : itemAttributes) {
                    CategoryRequest categoryRequest = new CategoryRequest();
                    categoryRequest.setAttribute(itemAttribute);
                    List<AttributeValues> attributeValuesResults = new ArrayList<>();

                    ItemAttributeParam itemAttributeParam = new ItemAttributeParam();
                    ToolUtil.copyProperties(itemAttribute, itemAttributeParam);

                    List<AttributeValuesParam> attributeValuesParams = new ArrayList<>();
                    for (AttributeValues attributeValue : attributeValues) {
                        if (itemAttribute.getAttributeId().equals(attributeValue.getAttributeId())) {
                            attributeValuesResults.add(attributeValue);
                            AttributeValuesParam attributeValuesParam = new AttributeValuesParam();
                            ToolUtil.copyProperties(attributeValue, attributeValuesParam);
                            attributeValuesParams.add(attributeValuesParam);
                        }
                    }
                    itemAttributeParam.setAttributeValuesParams(attributeValuesParams);
                    itemAttributeParamList.add(itemAttributeParam);
                    categoryRequest.setValue(attributeValuesResults);
                    categoryRequests.add(categoryRequest);
                }
            }
        }
        result.setItemAttributeParams(itemAttributeParamList);
        ToolUtil.copyProperties(detail, result);
        result.setCategoryRequests(categoryRequests);
        return ResponseData.success(result);
    }

    /**
     * 批量添加
     *
     * @param categoryParam
     * @return
     */
    @RequestMapping(value = "/addList", method = RequestMethod.POST)
    public ResponseData addList(@RequestBody CategoryParam categoryParam) {
        this.categoryService.addList(categoryParam);
        return ResponseData.success();
    }

    /**
     * 查询列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CategoryResult> list(@RequestBody(required = false) CategoryParam categoryParam) {
        if (ToolUtil.isEmpty(categoryParam)) {
            categoryParam = new CategoryParam();
        }
        if (ToolUtil.isNotEmpty(categoryParam.getPidValue())) {
            List<String> pidValue = categoryParam.getPidValue();
            categoryParam.setPid(Long.valueOf(pidValue.get(pidValue.size() - 1)));
        }
        return this.categoryService.findPageBySpec(categoryParam);
    }

    /**
     * 选择列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect() {
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.categoryService.listMaps(categoryQueryWrapper);

        CategorySelectWrapper factory = new CategorySelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }

    /**
     * tree列表，treeview格式
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/treeView", method = RequestMethod.POST)
    @ApiOperation("Tree数据接口")
    public ResponseData<List<TreeNode>> treeView() {
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.in("display", 1);
        categoryQueryWrapper.orderByDesc("sort");
        List<Map<String, Object>> list = this.categoryService.listMaps(categoryQueryWrapper);

        List<TreeNode> treeViewNodes = new ArrayList<>();

        TreeNode rootTreeNode = new TreeNode();
        rootTreeNode.setKey("0");
        rootTreeNode.setValue("0");
        rootTreeNode.setLabel("顶级");
        rootTreeNode.setTitle("顶级");
        rootTreeNode.setParentId("-1");
        treeViewNodes.add(rootTreeNode);

        for (Map<String, Object> item : list) {
            TreeNode treeNode = new TreeNode();
            treeNode.setParentId(Convert.toStr(item.get("pid")));
            treeNode.setKey(Convert.toStr(item.get("category_id")));
            treeNode.setValue(Convert.toStr(item.get("category_id")));
            treeNode.setTitle(Convert.toStr(item.get("category_name")));
            treeNode.setLabel(Convert.toStr(item.get("category_name")));
            treeViewNodes.add(treeNode);
        }
        //构建树
        DefaultTreeBuildFactory<TreeNode> factory = new DefaultTreeBuildFactory<>();
        factory.setRootParentId("0");
        List<TreeNode> results = factory.doTreeBuild(treeViewNodes);

        //把子节点为空的设为null
        //DeptTreeWrapper.clearNull(results);

        return ResponseData.success(results);
    }


}


