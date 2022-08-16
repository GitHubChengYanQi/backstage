package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.SysDept;
import cn.atsoft.dasheng.app.model.params.SysDeptParam;
import cn.atsoft.dasheng.app.model.result.SysDeptResult;
import cn.atsoft.dasheng.app.service.SysDeptService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.rest.wrapper.DictTypeWrapper;
import cn.atsoft.dasheng.sys.modular.system.service.DictTypeService;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.app.wrapper.SysDeptSelectWrapper;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 部门表控制器
 *
 * @author 
 * @Date 2020-12-22 09:28:13
 */
@RestController
@RequestMapping("/sysDept")
@Api(tags = "部门表")
public class SysDeptController extends BaseController {

    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2020-12-22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SysDeptParam sysDeptParam) {
        List<String>  pidValue = sysDeptParam.getPidValue();
        sysDeptParam.setPid(Long.valueOf(pidValue.get(pidValue.size()-1)));
        this.sysDeptService.add(sysDeptParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2020-12-22
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SysDeptParam sysDeptParam) {

        List<String>  pidValue = sysDeptParam.getPidValue();
        sysDeptParam.setPid(Long.valueOf(pidValue.get(pidValue.size()-1)));
        this.sysDeptService.update(sysDeptParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2020-12-22
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SysDeptParam sysDeptParam)  {
        this.sysDeptService.delete(sysDeptParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2020-12-22
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody SysDeptParam sysDeptParam) {
        SysDept detail = this.sysDeptService.getById(sysDeptParam.getDeptId());
        SysDeptResult result = new SysDeptResult();
        ToolUtil.copyProperties(detail, result);

        List<Map<String,Object>> list = this.sysDeptService.listMaps();
        List<String> parentValue = SysDeptSelectWrapper.fetchParentKey(list, Convert.toStr(detail.getPid()));
        result.setPidValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2020-12-22
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SysDeptResult> list(@RequestBody(required = false) SysDeptParam sysDeptParam) {
        if(ToolUtil.isEmpty(sysDeptParam)){
            sysDeptParam = new SysDeptParam();
        }
        if(ToolUtil.isNotEmpty(sysDeptParam.getPidValue())){
            List<String>  pidValue = sysDeptParam.getPidValue();
            sysDeptParam.setPid(Long.valueOf(pidValue.get(pidValue.size()-1)));
        }
//        return this.sysDeptService.findPageBySpec(sysDeptParam);
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.sysDeptService.findPageBySpec(sysDeptParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope());
            return this.sysDeptService.findPageBySpec(sysDeptParam, dataScope);
        }
    }

    /**
    * 选择列表
    *
    * @author 
    * @Date 2020-12-22
    */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        List<Map<String,Object>> list = this.sysDeptService.listMaps();

        SysDeptSelectWrapper factory = new SysDeptSelectWrapper(list);
        List<Map<String,Object>> result = factory.wrap();
        return ResponseData.success(result);
    }
    /**
     * tree列表，treeview格式
     *
     * @author 
         * @Date 2020-12-22
     */
    @RequestMapping(value = "/treeView", method = RequestMethod.POST)
    @ApiOperation("Tree数据接口")
    public ResponseData treeView() {
        List<Map<String,Object>> list = this.sysDeptService.listMaps();

        if(ToolUtil.isEmpty(list)){
            return ResponseData.success();
        }
        List<TreeNode>  treeViewNodes = new ArrayList<>();

        TreeNode rootTreeNode = new TreeNode();
        rootTreeNode.setKey("0");
        rootTreeNode.setValue("0");
        rootTreeNode.setLabel("顶级");
        rootTreeNode.setTitle("顶级");
        rootTreeNode.setParentId("-1");
        treeViewNodes.add(rootTreeNode);

        for(Map<String, Object> item:list){
            TreeNode treeNode = new TreeNode();
            treeNode.setParentId(Convert.toStr(item.get("pid")));
            treeNode.setKey(Convert.toStr(item.get("dept_id")));
            treeNode.setValue(Convert.toStr(item.get("dept_id")));
            treeNode.setTitle(Convert.toStr(item.get("full_name")));
            treeNode.setLabel(Convert.toStr(item.get("full_name")));
            treeViewNodes.add(treeNode);
        }
        //构建树
        DefaultTreeBuildFactory<TreeNode> factory = new DefaultTreeBuildFactory<>();
        factory.setRootParentId("-1");
        List<TreeNode> results = factory.doTreeBuild(treeViewNodes);

        //把子节点为空的设为null
        //DeptTreeWrapper.clearNull(results);

        return ResponseData.success(results);
    }


    @Autowired
    private DictTypeService dictTypeService;

    @RequestMapping(value = "/dictTypeListSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData dictTypeListSelect() {
        List<Map<String, Object>> list = this.dictTypeService.listMaps();
        DictTypeWrapper dictTypeWrapper = new DictTypeWrapper(list);
        List<Map<String, Object>> result = dictTypeWrapper.wrap();
        return ResponseData.success(result);
    }



}


