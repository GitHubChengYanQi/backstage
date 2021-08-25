package cn.atsoft.dasheng.common_area.controller;

import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.common_area.entity.CommonArea;
import cn.atsoft.dasheng.common_area.model.params.CommonAreaParam;
import cn.atsoft.dasheng.common_area.model.result.CommonAreaResult;
import cn.atsoft.dasheng.common_area.service.CommonAreaService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.common_area.wrapper.CommonAreaSelectWrapper;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 逐渐取代region表控制器
 *
 * @author
 * @Date 2021-08-24 14:59:54
 */
@RestController
@RequestMapping("/commonArea")
@Api(tags = "逐渐取代region表")
public class CommonAreaController extends BaseController {

    @Autowired
    private CommonAreaService commonAreaService;

    /**
     * 新增接口
     *
     * @author
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody CommonAreaParam commonAreaParam) {
        List<String> pidValue = commonAreaParam.getPidValue();
//        commonAreaParam.setParentid(pidValue.get(pidValue.size()-1));
        this.commonAreaService.add(commonAreaParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody CommonAreaParam commonAreaParam) {

        List<String> pidValue = commonAreaParam.getPidValue();
//        commonAreaParam.setParentid(pidValue.get(pidValue.size()-1));
        this.commonAreaService.update(commonAreaParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody CommonAreaParam commonAreaParam) {
        this.commonAreaService.delete(commonAreaParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<CommonAreaResult> detail(@RequestBody CommonAreaParam commonAreaParam) {
        CommonArea detail = this.commonAreaService.getById(commonAreaParam.getId());
        CommonAreaResult result = new CommonAreaResult();
        ToolUtil.copyProperties(detail, result);

        List<Map<String, Object>> list = this.commonAreaService.listMaps();
        List<String> parentValue = CommonAreaSelectWrapper.fetchParentKey(list, Convert.toStr(detail.getParentid()));
//        result.setParentidValue(parentValue);
        return ResponseData.success(result);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<CommonAreaResult> list(@RequestBody(required = false) CommonAreaParam commonAreaParam) {
        if (ToolUtil.isEmpty(commonAreaParam)) {
            commonAreaParam = new CommonAreaParam();
        }
        if (ToolUtil.isNotEmpty(commonAreaParam.getPidValue())) {
            List<String> pidValue = commonAreaParam.getPidValue();
//            commonAreaParam.setParentid(pidValue.get(pidValue.size()-1));
        }
        return this.commonAreaService.findPageBySpec(commonAreaParam);
    }

    /**
     * 选择列表
     *
     * @author
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect() {
        List<Map<String, Object>> list = this.commonAreaService.listMaps();

        CommonAreaSelectWrapper factory = new CommonAreaSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }

    /**
     * tree列表，treeview格式
     *
     * @author
     * @Date 2021-08-24
     */
    @RequestMapping(value = "/treeView", method = RequestMethod.POST)
    @ApiOperation("Tree数据接口")
    public ResponseData<List<TreeNode>> treeView() {
        List<Map<String, Object>> list = this.commonAreaService.listMaps();

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

    @RequestMapping(value = "/getProvince", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData getProvince() {
        List<CommonAreaResult> province = this.commonAreaService.getProvince();
        return ResponseData.success(province);
    }

    @RequestMapping(value = "/getCity", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData getCity(@RequestParam("id") CommonAreaParam param) {
        List<CommonAreaResult> province = this.commonAreaService.getCity(param);
        return ResponseData.success(province);
    }

    @RequestMapping(value = "/getArea", method = RequestMethod.POST)
    @ApiOperation("删除")
    public ResponseData getArea(@RequestParam("id") CommonAreaParam param) {
        List<CommonAreaResult> province = this.commonAreaService.getArea(param);
        return ResponseData.success(province);
    }
}


