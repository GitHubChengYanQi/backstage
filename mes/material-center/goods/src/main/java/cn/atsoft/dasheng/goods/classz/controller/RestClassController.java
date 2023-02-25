package cn.atsoft.dasheng.goods.classz.controller;

import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.category.wrapper.RestCategorySelectWrapper;
import cn.atsoft.dasheng.goods.classz.entity.RestAttribute;
import cn.atsoft.dasheng.goods.classz.entity.RestAttributeValues;
import cn.atsoft.dasheng.goods.classz.entity.RestClass;
import cn.atsoft.dasheng.goods.classz.model.RavAndRabByRestClass;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeParam;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeValuesParam;
import cn.atsoft.dasheng.goods.classz.model.params.RestClassParam;
import cn.atsoft.dasheng.goods.classz.model.result.RestClassResult;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeService;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeValuesService;
import cn.atsoft.dasheng.goods.classz.service.RestClassService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.util.BuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/class/{version}")
@ApiVersion("2.0")
@Api(tags = "类目管理")
public class RestClassController extends BaseController {

    @Autowired
    private RestClassService restClassService;
    @Autowired
    private RestAttributeService restAttributeService;
    @Autowired
    private RestAttributeValuesService restAttributeValuesService;

    /**
     * 新增接口
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestClassParam restClassParam) {
        this.restClassService.add(restClassParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改类目", key = "name", dict = RestClassParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody RestClassParam restClassParam) {

        this.restClassService.update(restClassParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除类目", key = "name", dict = RestClassParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody RestClassParam restClassParam) {
        this.restClassService.delete(restClassParam);
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
    public ResponseData detail(@RequestBody RestClassParam categoryParam) {
        RestClass detail = this.restClassService.getById(categoryParam.getClassId());
        RestClassResult result = new RestClassResult();
        List<RavAndRabByRestClass> categoryRequests = new ArrayList<>();
        List<RestAttributeParam> itemAttributeParamList = new ArrayList<>();
        if (ToolUtil.isNotEmpty(detail)) {

            List<RestAttribute> itemAttributes = restAttributeService.lambdaQuery()
                    .in(RestAttribute::getCategoryId, detail.getCategoryId()).orderByDesc(RestAttribute::getSort)
                    .list();

            if (ToolUtil.isNotEmpty(itemAttributes)) {
                List<Long> attId = new ArrayList<>();
                for (RestAttribute itemAttribute : itemAttributes) {
                    attId.add(itemAttribute.getAttributeId());
                }
                List<RestAttributeValues> attributeValues = restAttributeValuesService.lambdaQuery()
                        .in(RestAttributeValues::getAttributeId, attId)
                        .list();

                for (RestAttribute itemAttribute : itemAttributes) {
                    RavAndRabByRestClass categoryRequest = new RavAndRabByRestClass();
                    categoryRequest.setAttribute(itemAttribute);
                    List<RestAttributeValues> attributeValuesResults = new ArrayList<>();

                    RestAttributeParam itemAttributeParam = new RestAttributeParam();
                    ToolUtil.copyProperties(itemAttribute, itemAttributeParam);

                    List<RestAttributeValuesParam> attributeValuesParams = new ArrayList<>();
                    for (RestAttributeValues attributeValue : attributeValues) {
                        if (itemAttribute.getAttributeId().equals(attributeValue.getAttributeId())) {
                            attributeValuesResults.add(attributeValue);
                            RestAttributeValuesParam attributeValuesParam = new RestAttributeValuesParam();
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
        result.setRavAndRabByRestClass(categoryRequests);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<RestClassResult> list(@RequestBody(required = false) RestClassParam restClassParam) {
        if (ToolUtil.isEmpty(restClassParam)) {
            restClassParam = new RestClassParam();
        }
        if (ToolUtil.isNotEmpty(restClassParam.getPidValue())) {
            List<String> pidValue = restClassParam.getPidValue();
            restClassParam.setPid(Long.valueOf(pidValue.get(pidValue.size() - 1)));
        }
        return this.restClassService.findPageBySpec(restClassParam);
    }

    /**
     * 选择列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<RestClass> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.restClassService.listMaps(categoryQueryWrapper);

        RestCategorySelectWrapper factory = new RestCategorySelectWrapper(list);
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
    public ResponseData treeView() {
        QueryWrapper<RestClass> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.in("display", 1);
        categoryQueryWrapper.orderByDesc("sort");
        List<Map<String, Object>> list = this.restClassService.listMaps(categoryQueryWrapper);

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


