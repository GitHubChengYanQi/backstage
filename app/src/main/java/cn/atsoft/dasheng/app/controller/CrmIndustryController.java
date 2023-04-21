package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.result.BatchDeleteRequest;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmIndustry;
import cn.atsoft.dasheng.app.model.params.CrmIndustryParam;
import cn.atsoft.dasheng.app.model.result.CrmIndustryResult;
import cn.atsoft.dasheng.app.service.CrmIndustryService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.app.wrapper.CrmIndustrySelectWrapper;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 行业表控制器
 *
 * @author
 * @Date 2021-08-02 08:25:03
 */
@RestController
@RequestMapping("/crmIndustry")
@Api(tags = "行业表")
public class CrmIndustryController extends BaseController {

    @Autowired
    private CrmIndustryService crmIndustryService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody(required = false) CrmIndustryParam crmIndustryParam) {
//        List<String> pidValue = crmIndustryParam.getPidValue();
//        crmIndustryParam.setParent_id(pidValue.get(pidValue.size()-1));
        this.crmIndustryService.add(crmIndustryParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody(required = false) CrmIndustryParam crmIndustryParam) {

//        List<String> pidValue = crmIndustryParam.getPidValue();
//        crmIndustryParam.setParent_id(pidValue.get(pidValue.size()-1));
        this.crmIndustryService.update(crmIndustryParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody(required = false) CrmIndustryParam crmIndustryParam) {
        this.crmIndustryService.delete(crmIndustryParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation("批量删除")
    public ResponseData batchDelete(@RequestBody(required = false) BatchDeleteRequest batchDeleteRequest) {
        this.crmIndustryService.batchDelete(batchDeleteRequest.getIds());
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody(required = false) CrmIndustryParam crmIndustryParam) {
        CrmIndustry detail = this.crmIndustryService.getById(crmIndustryParam.getIndustryId());
        CrmIndustryResult result = new CrmIndustryResult();
if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        List<Map<String, Object>> list = this.crmIndustryService.listMaps();
//        List<String> parentValue = CrmIndustrySelectWrapper.fetchParentKey(list, Convert.toStr(detail.getParent_id()));
//        result.setParent_idValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo list(@RequestBody(required = false) CrmIndustryParam crmIndustryParam) {
        if (ToolUtil.isEmpty(crmIndustryParam)) {
            crmIndustryParam = new CrmIndustryParam();
        }
        if (ToolUtil.isNotEmpty(crmIndustryParam.getPidValue())) {
            List<String> pidValue = crmIndustryParam.getPidValue();
        }
//        return this.crmIndustryService.findPageBySpec(crmIndustryParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.crmIndustryService.findPageBySpec(crmIndustryParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return this.crmIndustryService.findPageBySpec(crmIndustryParam, dataScope);
        }
    }

    /**
     * 选择列表
     *
     * @author
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        QueryWrapper<CrmIndustry> industryQueryWrapper = new QueryWrapper<>();
        industryQueryWrapper.in("display", 1);
        List<Map<String, Object>> list = this.crmIndustryService.listMaps(industryQueryWrapper);
        CrmIndustrySelectWrapper factory = new CrmIndustrySelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }

    /**
     * tree列表，treeview格式
     *
     * @author
     * @Date 2021-08-02
     */
    @RequestMapping(value = "/treeView", method = RequestMethod.POST)
    @ApiOperation("Tree数据接口")
    public ResponseData treeView() {
        List<Map<String, Object>> list = this.crmIndustryService.listMaps(new QueryWrapper<CrmIndustry>() {{
            orderByDesc("sort");
        }});

        if (ToolUtil.isEmpty(list)) {
            return ResponseData.success();
        }
        List<TreeNode> treeViewNodes = new ArrayList<>();

        TreeNode rootTreeNode = new TreeNode();
        rootTreeNode.setKey("0");
        rootTreeNode.setValue("0");
        rootTreeNode.setParentId("-1");
        treeViewNodes.add(rootTreeNode);

        for (Map<String, Object> item : list) {
            TreeNode treeNode = new TreeNode();
            treeNode.setParentId(Convert.toStr(item.get("parent_id")));
            treeNode.setKey(Convert.toStr(item.get("industry_id")));
            treeNode.setValue(Convert.toStr(item.get("industry_id")));
            treeNode.setTitle(Convert.toStr(item.get("industry_name")));
            treeNode.setLabel(Convert.toStr(item.get("industry_name")));
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


