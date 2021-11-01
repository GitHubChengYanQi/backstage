package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsParam;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.erp.wrapper.StorehousePositionsSelectWrapper;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 仓库库位表控制器
 *
 * @author song
 * @Date 2021-10-29 09:22:25
 */
@RestController
@RequestMapping("/storehousePositions")
@Api(tags = "仓库库位表")
public class StorehousePositionsController extends BaseController {

    @Autowired
    private StorehousePositionsService storehousePositionsService;
    @Autowired
    private StorehouseService storehouseService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody StorehousePositionsParam storehousePositionsParam) {
        this.storehousePositionsService.add(storehousePositionsParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody StorehousePositionsParam storehousePositionsParam) {

        this.storehousePositionsService.update(storehousePositionsParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody StorehousePositionsParam storehousePositionsParam) {
        this.storehousePositionsService.delete(storehousePositionsParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<StorehousePositionsResult> detail(@RequestBody StorehousePositionsParam storehousePositionsParam) {
        StorehousePositions detail = this.storehousePositionsService.getById(storehousePositionsParam.getStorehousePositionsId());
        StorehousePositionsResult result = new StorehousePositionsResult();
        ToolUtil.copyProperties(detail, result);

        List<Map<String, Object>> list = this.storehousePositionsService.listMaps();
        List<String> parentValue = StorehousePositionsSelectWrapper.fetchParentKey(list, Convert.toStr(detail.getPid()));
        result.setPidValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<StorehousePositionsResult> list(@RequestBody(required = false) StorehousePositionsParam storehousePositionsParam) {
        if (ToolUtil.isEmpty(storehousePositionsParam)) {
            storehousePositionsParam = new StorehousePositionsParam();
        }
        return this.storehousePositionsService.findPageBySpec(storehousePositionsParam);
    }

    /**
     * 选择列表
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect() {
        List<Map<String, Object>> list = this.storehousePositionsService.listMaps();
        StorehousePositionsSelectWrapper factory = new StorehousePositionsSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }

    /**
     * tree列表，treeview格式
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/treeView", method = RequestMethod.GET)
    @ApiOperation("Tree数据接口")
    public ResponseData<List<TreeNode>> treeView(@RequestParam Long ids) {

        Storehouse storehouse = storehouseService.query().eq("storehouse_id", ids).one();
        if (ToolUtil.isEmpty(storehouse)) {
            throw  new ServiceException(500,"数据不正确");
        }

        QueryWrapper<StorehousePositions> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("storehouse_id", ids);

        List<Map<String, Object>> list = this.storehousePositionsService.listMaps(queryWrapper);

        List<TreeNode> treeViewNodes = new ArrayList<>();

        TreeNode rootTreeNode = new TreeNode();
        rootTreeNode.setKey("0");
        rootTreeNode.setValue("0");
        rootTreeNode.setLabel(storehouse.getName());
        rootTreeNode.setTitle(storehouse.getName());
        rootTreeNode.setParentId("-1");
        treeViewNodes.add(rootTreeNode);

        for (Map<String, Object> item : list) {
            TreeNode treeNode = new TreeNode();
            treeNode.setParentId(Convert.toStr(item.get("pid")));
            treeNode.setKey(Convert.toStr(item.get("storehouse_positions_id")));
            treeNode.setValue(Convert.toStr(item.get("storehouse_positions_id")));
            treeNode.setTitle(Convert.toStr(item.get("name")));
            treeNode.setLabel(Convert.toStr(item.get("name")));
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

