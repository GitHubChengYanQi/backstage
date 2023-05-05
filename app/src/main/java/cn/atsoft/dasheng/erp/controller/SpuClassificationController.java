package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;
import cn.atsoft.dasheng.erp.entity.SpuClassification;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.params.SpuClassificationParam;
import cn.atsoft.dasheng.erp.model.result.SpuClassificationResult;
import cn.atsoft.dasheng.erp.service.SpuClassificationService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.wrapper.SpuClassificationSelectWrapper;
import cn.atsoft.dasheng.erp.wrapper.SpuSelectWrapper;
import cn.atsoft.dasheng.form.entity.FormStyle;
import cn.atsoft.dasheng.form.service.FormStyleService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * SPU分类控制器
 *
 * @author song
 * @Date 2021-10-25 17:52:03
 */
@RestController
@RequestMapping("/spuClassification")
@Api(tags = "SPU分类")
public class SpuClassificationController extends BaseController {

    @Autowired
    private SpuClassificationService spuClassificationService;

    @Autowired
    private FormStyleService formStyleService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SpuClassificationParam spuClassificationParam) {

        return ResponseData.success(this.spuClassificationService.add(spuClassificationParam));
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改spu分类", key = "name", dict = SpuClassificationParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SpuClassificationParam spuClassificationParam) {

        this.spuClassificationService.update(spuClassificationParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-25
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除spu分类", key = "name", dict = SpuClassificationParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SpuClassificationParam spuClassificationParam) {
        this.spuClassificationService.delete(spuClassificationParam);
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
    public ResponseData detail(@RequestBody SpuClassificationParam spuClassificationParam) {
        SpuClassification detail = this.spuClassificationService.getById(spuClassificationParam.getSpuClassificationId());
        SpuClassificationResult result = new SpuClassificationResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        if (ToolUtil.isNotEmpty(result.getFormStyleId())) {
            FormStyle formStyle = formStyleService.getById(result.getFormStyleId());
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
    public PageInfo<SpuClassificationResult> list(@RequestBody(required = false) SpuClassificationParam spuClassificationParam) {
        if (ToolUtil.isEmpty(spuClassificationParam)) {
            spuClassificationParam = new SpuClassificationParam();
        }
        return this.spuClassificationService.findPageBySpec(spuClassificationParam);
    }


    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(@RequestBody(required = false) SpuClassificationParam spuClassificationParam) {
        QueryWrapper<SpuClassification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tenant_id", LoginContextHolder.getContext().getTenantId());
        if (ToolUtil.isNotEmpty(spuClassificationParam) && ToolUtil.isNotEmpty(spuClassificationParam.getSpuClassificationId())) {
            queryWrapper.eq("pid", spuClassificationParam.getSpuClassificationId());
        }
        queryWrapper.eq("display", 1);

        List<Map<String, Object>> list = this.spuClassificationService.listMaps(queryWrapper);
        SpuClassificationSelectWrapper spuClassificationSelectWrapper = new SpuClassificationSelectWrapper(list);
        List<Map<String, Object>> result = spuClassificationSelectWrapper.wrap();
        return ResponseData.success(result);
    }

    @RequestMapping(value = "/treeView", method = RequestMethod.POST)
    public ResponseData treeView(@RequestBody(required = false) SpuClassificationParam spuClassificationParam) {
        QueryWrapper<SpuClassification> spuClassificationQueryWrapper = new QueryWrapper<>();
        if (ToolUtil.isNotEmpty(spuClassificationParam)) {
            if (ToolUtil.isNotEmpty(spuClassificationParam.getSpuClassificationId())) {
                spuClassificationQueryWrapper.eq("spu_classification_id", spuClassificationParam.getSpuClassificationId());
            }
        }

        spuClassificationQueryWrapper.eq("display", 1);
        spuClassificationQueryWrapper.orderByDesc("sort");
        spuClassificationQueryWrapper.eq("tenant_id",LoginContextHolder.getContext().getTenantId());

        List<Map<String, Object>> list = this.spuClassificationService.listMaps(spuClassificationQueryWrapper);

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
}


