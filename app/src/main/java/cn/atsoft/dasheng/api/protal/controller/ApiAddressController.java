package cn.atsoft.dasheng.api.protal.controller;

import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.commonArea.model.params.CommonAreaParam;
import cn.atsoft.dasheng.commonArea.model.result.CommonAreaResult;
import cn.atsoft.dasheng.commonArea.service.CommonAreaService;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ApiAddressController {

    @Autowired
    private CommonAreaService commonAreaService;

    @RequestMapping(value = "/getProvince", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData getProvince() {
        List<CommonAreaResult> province = this.commonAreaService.getProvince();
        return ResponseData.success(province);
    }

    @RequestMapping(value = "/getCity", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData getCity(@RequestBody CommonAreaParam param) {
        List<CommonAreaResult> province = this.commonAreaService.getCity(param);
        return ResponseData.success(province);
    }

    @RequestMapping(value = "/getArea", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData getArea(@RequestBody CommonAreaParam param) {
        List<CommonAreaResult> province = this.commonAreaService.getArea(param);
        return ResponseData.success(province);
    }

    @RequestMapping(value = "/treeView", method = RequestMethod.POST)
    public ResponseData treeView() {
        List<Map<String,Object>> list = this.commonAreaService.listMaps();

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
            treeNode.setParentId(Convert.toStr(item.get("parentid")));
            treeNode.setKey(Convert.toStr(item.get("id")));
            treeNode.setValue(Convert.toStr(item.get("id")));
            treeNode.setTitle(Convert.toStr(item.get("title")));
            treeNode.setLabel(Convert.toStr(item.get("title")));
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


}
