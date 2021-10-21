package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.model.params.PartRequest;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.app.wrapper.PartsSelectWrapper;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 清单控制器
 *
 * @author song
 * @Date 2021-10-21 08:41:22
 */
@RestController
@RequestMapping("/parts")
@Api(tags = "清单")
public class PartsController extends BaseController {

    @Autowired
    private PartsService partsService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-21
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PartRequest partRequest) {

        this.partsService.add(partRequest);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-21
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody PartsParam partsParam) {

        List<String>  pidValue = partsParam.getPidValue();
        partsParam.setPid(Long.valueOf(pidValue.get(pidValue.size()-1)));
        this.partsService.update(partsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-21
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody PartsParam partsParam)  {
        this.partsService.delete(partsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-21
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<PartsResult> detail(@RequestBody PartsParam partsParam) {
        Parts detail = this.partsService.getById(partsParam.getPartsId());
        PartsResult result = new PartsResult();
        ToolUtil.copyProperties(detail, result);

        List<Map<String,Object>> list = this.partsService.listMaps();
        List<String> parentValue = PartsSelectWrapper.fetchParentKey(list, Convert.toStr(detail.getPid()));
        result.setPidValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-21
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<PartsResult> list(@RequestBody(required = false) PartsParam partsParam) {
        if(ToolUtil.isEmpty(partsParam)){
            partsParam = new PartsParam();
        }
        if(ToolUtil.isNotEmpty(partsParam.getPidValue())){
            List<String>  pidValue = partsParam.getPidValue();
            partsParam.setPid(Long.valueOf(pidValue.get(pidValue.size()-1)));
        }
        return this.partsService.findPageBySpec(partsParam);
    }

    /**
    * 选择列表
    *
    * @author song
    * @Date 2021-10-21
    */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String,Object>>> listSelect() {
        List<Map<String,Object>> list = this.partsService.listMaps();

        PartsSelectWrapper factory = new PartsSelectWrapper(list);
        List<Map<String,Object>> result = factory.wrap();
        return ResponseData.success(result);
    }
    /**
     * tree列表，treeview格式
     *
     * @author song
         * @Date 2021-10-21
     */
    @RequestMapping(value = "/treeView", method = RequestMethod.POST)
    @ApiOperation("Tree数据接口")
    public ResponseData<List<TreeNode>> treeView() {
        List<Map<String,Object>> list = this.partsService.listMaps();

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
            treeNode.setKey(Convert.toStr(item.get("parts_id")));
            treeNode.setValue(Convert.toStr(item.get("parts_id")));
            treeNode.setTitle(Convert.toStr(item.get("parts_id")));
            treeNode.setLabel(Convert.toStr(item.get("parts_id")));
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


