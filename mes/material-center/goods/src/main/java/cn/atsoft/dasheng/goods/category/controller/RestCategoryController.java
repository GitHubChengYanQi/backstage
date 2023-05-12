package cn.atsoft.dasheng.goods.category.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.RestFormStyle;
import cn.atsoft.dasheng.form.service.RestFormStyleService;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.model.params.RestCategoryParam;
import cn.atsoft.dasheng.goods.category.model.result.RestCategoryResult;
import cn.atsoft.dasheng.goods.category.service.RestCategoryService;
import cn.atsoft.dasheng.goods.category.wrapper.RestCategorySelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
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
import java.util.stream.Collectors;


/**
 * SPU分类控制器
 *
 * @author song
 * @Date 2021-10-25 17:52:03
 */
@RestController
@RequestMapping("/category/{version}")
@ApiVersion("2.0")
@Api(tags = "SPU分类管理")
public class RestCategoryController extends BaseController {

    @Autowired
    private RestCategoryService restCategoryService;

    @Autowired
    private RestFormStyleService formStyleService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestCategoryParam restCategoryParam) {
        this.restCategoryService.add(restCategoryParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改spu分类", key = "name", dict = RestCategoryParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RestCategoryParam restCategoryParam) {

        this.restCategoryService.update(restCategoryParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除spu分类", key = "name", dict = RestCategoryParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RestCategoryParam restCategoryParam) {
        this.restCategoryService.delete(restCategoryParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody RestCategoryParam restCategoryParam) {
        RestCategory detail = this.restCategoryService.getById(restCategoryParam.getSpuClassificationId());
        RestCategoryResult result = new RestCategoryResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        if (ToolUtil.isNotEmpty(result.getFormStyleId())) {
            RestFormStyle formStyle = formStyleService.getById(result.getFormStyleId());
            result.setTypeSetting(formStyle.getTypeSetting());
        }

        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RestCategoryResult> list(@RequestBody(required = false) RestCategoryParam restCategoryParam) {
        if (ToolUtil.isEmpty(restCategoryParam)) {
            restCategoryParam = new RestCategoryParam();
        }
        return this.restCategoryService.findPageBySpec(restCategoryParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(@RequestBody(required = false) RestCategoryParam restCategoryParam) {
        QueryWrapper<RestCategory> queryWrapper = new QueryWrapper<>();

        if (ToolUtil.isNotEmpty(restCategoryParam) && ToolUtil.isNotEmpty(restCategoryParam.getSpuClassificationId())) {
            queryWrapper.eq("pid", restCategoryParam.getSpuClassificationId());
        }
        queryWrapper.eq("display", 1);

        List<Map<String, Object>> list = this.restCategoryService.listMaps(queryWrapper);
        RestCategorySelectWrapper spuClassificationSelectWrapper = new RestCategorySelectWrapper(list);
        List<Map<String, Object>> result = spuClassificationSelectWrapper.wrap();
        return ResponseData.success(result);
    }

    @RequestMapping(value = "/treeView", method = RequestMethod.POST)
    @ApiOperation("SelectTree数据接口")
    public ResponseData treeView(@RequestBody(required = false) RestCategoryParam restCategoryParam) {
        QueryWrapper<RestCategory> spuClassificationQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(restCategoryParam)) {
            if (ToolUtil.isNotEmpty(restCategoryParam.getSpuClassificationId())) {
                spuClassificationQueryWrapper.eq("spu_classification_id", restCategoryParam.getSpuClassificationId());
            }
        }

        spuClassificationQueryWrapper.eq("display", 1);
        spuClassificationQueryWrapper.orderByDesc("sort");

        List<Map<String, Object>> list = this.restCategoryService.listMaps(spuClassificationQueryWrapper);

        if (ToolUtil.isEmpty(list)) {
            return ResponseData.success();
        }
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
            treeNode.setKey(Convert.toStr(item.get("spu_classification_id")));
            treeNode.setValue(Convert.toStr(item.get("spu_classification_id")));
            treeNode.setLabel(Convert.toStr(item.get("name")));
            treeNode.setTitle(Convert.toStr(item.get("name")));
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
    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    @ApiOperation("排序接口")
    public void updateSort(@RequestBody RestCategoryParam restCategoryParam) {
        List<Long> categoryIds = restCategoryParam.getSortParam().stream().map(RestCategoryParam.Sort::getSpuClassificationId).distinct().collect(Collectors.toList());
        List<RestCategory> restCategories = this.restCategoryService.listByIds(categoryIds);
        //将restCategoryParam.getSortParam()的spuClassificationId与restCategories中的spuClassificationId 对比 去掉将restCategoryParam.getSortParam中的 restCategories.getSortParam()中没有的数据
        restCategoryParam.getSortParam().removeIf(sort -> restCategories.stream().noneMatch(restCategory -> restCategory.getSpuClassificationId().equals(sort.getSpuClassificationId())));
        //将restCategoryParam.getSortParam()中重复的spuClassificationId去掉
        restCategoryParam.getSortParam().removeIf(sort -> restCategoryParam.getSortParam().stream().filter(sort1 -> sort1.getSpuClassificationId().equals(sort.getSpuClassificationId())).count() > 1);
        //将restCategoryParam.getSortParam()中的数据copy到restCategories中
        List<RestCategory> restCategorys = BeanUtil.copyToList(restCategoryParam.getSortParam(), RestCategory.class);
        this.restCategoryService.updateBatchById(restCategorys);
    }
}


