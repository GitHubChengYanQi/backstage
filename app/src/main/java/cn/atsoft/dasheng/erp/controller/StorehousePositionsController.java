package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.BusinessTrack;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.service.BusinessTrackService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.datascope.DataScope;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsBindParam;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsDeptBindParam;
import cn.atsoft.dasheng.erp.model.params.StorehousePositionsParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsBindResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.pojo.PositionLoop;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsBindService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsDeptBindService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.erp.wrapper.StorehousePositionsSelectWrapper;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;

import java.util.*;
import java.util.stream.Collectors;


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
    private StorehousePositionsDeptBindService storehousePositionsDeptBindService;

    @Autowired
    private StorehousePositionsBindService storehousePositionsBindService;

    @Autowired
    private SkuService skuService;
    @Autowired
    private OrCodeBindService orCodeBindService;


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody StorehousePositionsParam storehousePositionsParam) {
        Long storehousePositionId = this.storehousePositionsService.add(storehousePositionsParam);
        if (ToolUtil.isNotEmpty(storehousePositionsParam.getSkuIds())) {
            storehousePositionsBindService.bindBatch(new StorehousePositionsBindParam(){{
                setSkuIds(storehousePositionsParam.getSkuIds());
                setPositionId(storehousePositionId);
            }});
        }
        if (ToolUtil.isNotEmpty(storehousePositionsParam.getDeptIds())) {
            List<StorehousePositionsDeptBindParam> collect = storehousePositionsParam.getDeptIds().stream().map(deptId -> new StorehousePositionsDeptBindParam() {{
                setDeptId(deptId);
                setStorehousePositionsId(storehousePositionId);
            }}).collect(Collectors.toList());
            //部门绑定
            storehousePositionsDeptBindService.addBatch(collect);

        }
        return ResponseData.success();
    }


    @RequestMapping(value = "/selectBySku", method = RequestMethod.POST)
    public ResponseData selectBySku(@RequestBody StorehousePositionsParam storehousePositionsParam) {
        Object select = this.storehousePositionsService.selectBySku(storehousePositionsParam);
        return ResponseData.success(select);
    }

    @RequestMapping(value = "/selectAll", method = RequestMethod.POST)
    public ResponseData selectAll(@RequestBody StorehousePositionsParam storehousePositionsParam) {
        Object select = this.storehousePositionsService.selectBySku(storehousePositionsParam.getSkuId());
        return ResponseData.success(select);
    }

    @RequestMapping(value = "/selectByBrand", method = RequestMethod.POST)
    public ResponseData selectByBrand(@RequestBody StorehousePositionsParam storehousePositionsParam) {
        List<BrandResult> resultList = this.storehousePositionsService.selectByBrand(
                storehousePositionsParam.getSkuId(),
                storehousePositionsParam.getBrandId(),
                storehousePositionsParam.getStorehouseId(),
                storehousePositionsParam.getStorehousePositionsId()
        );
        return ResponseData.success(resultList);
    }


    @RequestMapping(value = "/treeViewBySku", method = RequestMethod.POST)
    public ResponseData treeViewBySku(@RequestBody StorehousePositionsParam storehousePositionsParam) {
        List<PositionLoop> positionLoops = this.storehousePositionsService.treeViewBySku(storehousePositionsParam.getSkuIds());
        return ResponseData.success(positionLoops);
    }

    @RequestMapping(value = "/treeViewByName", method = RequestMethod.POST)
    public ResponseData treeViewByName(@RequestBody(required = false) StorehousePositionsParam storehousePositionsParam) {
        if (ToolUtil.isEmpty(storehousePositionsParam)) {
            storehousePositionsParam = new StorehousePositionsParam();
        }
        List<PositionLoop> positionLoops = this.storehousePositionsService.treeViewByName(storehousePositionsParam.getName(), storehousePositionsParam.getStorehouseId());
        if (ToolUtil.isNotEmpty(storehousePositionsParam.getSkuId())) {
            storehousePositionsService.positionFormat(positionLoops, storehousePositionsParam.getSkuId());
        }
        return ResponseData.success(positionLoops);
    }


    /**
     * 库位二维码打印
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/positionsResultById", method = RequestMethod.GET)
    @ApiOperation("新增")
    public ResponseData positionsResultById(@Param("id") Long id) {
        StorehousePositionsResult positionsResult = this.storehousePositionsService.positionsResultById(id);
        return ResponseData.success(positionsResult);
    }

    /**
     * 编辑接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改仓库库位表", key = "name", dict = StorehousePositionsParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody StorehousePositionsParam storehousePositionsParam) {
        this.storehousePositionsService.update(storehousePositionsParam);
        if (storehousePositionsParam.getSkuIds() != null) {
            storehousePositionsBindService.remove(new QueryWrapper<StorehousePositionsBind>().eq("storehouse_positions_id",storehousePositionsParam.getStorehousePositionsId()));
            storehousePositionsBindService.bindBatch(new StorehousePositionsBindParam(){{
                setSkuIds(storehousePositionsParam.getSkuIds());
                setPositionId(storehousePositionsParam.getStorehousePositionsId());
            }});
        }
        if(storehousePositionsParam.getDeptIds() != null){
            storehousePositionsDeptBindService.remove(new QueryWrapper<StorehousePositionsDeptBind>().eq("storehouse_positions_id",storehousePositionsParam.getStorehousePositionsId()));
                List<StorehousePositionsDeptBindParam> collect = storehousePositionsParam.getDeptIds().stream().map(deptId -> new StorehousePositionsDeptBindParam() {{
                    setDeptId(deptId);
                    setStorehousePositionsId(storehousePositionsParam.getStorehousePositionsId());
                }}).collect(Collectors.toList());
                //部门绑定
                storehousePositionsDeptBindService.addBatch(collect);

        }

        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除仓库库位表", key = "name", dict = StorehousePositionsParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody StorehousePositionsParam storehousePositionsParam) {
        this.storehousePositionsService.delete(storehousePositionsParam);
        return ResponseData.success();
    }
    /**
     * 删除接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    @BussinessLog(value = "删除仓库库位表", key = "name", dict = StorehousePositionsParam.class)
    @ApiOperation("删除")
    @Transactional
    public ResponseData deleteBatch(@RequestBody StorehousePositionsParam storehousePositionsParam) {

        this.storehousePositionsService.deleteBatch(storehousePositionsParam);
        return ResponseData.success();
    }


    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResponseData detail(@RequestParam Long id) {
        this.storehousePositionsService.detail(id);
        return ResponseData.success();
    }

    @RequestMapping(value = "/getSupperBySkuId", method = RequestMethod.GET)
    public ResponseData getSupperBySkuId(@RequestParam Long id) {
        List<StorehousePositionsResult> supperBySkuId = this.storehousePositionsService.getSupperBySkuId(id);
        return ResponseData.success(supperBySkuId);
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody StorehousePositionsParam storehousePositionsParam) {
        StorehousePositions detail = this.storehousePositionsService.getById(storehousePositionsParam.getStorehousePositionsId());
        StorehousePositionsResult result = new StorehousePositionsResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }
        List<StorehousePositionsBind> binds = storehousePositionsBindService.query().eq("position_id", detail.getStorehousePositionsId()).list();
        List<Long> skuIds = new ArrayList<>();
        for (StorehousePositionsBind bind : binds) {
            skuIds.add(bind.getSkuId());
        }
        List<SkuSimpleResult> results = skuService.simpleFormatSkuResult(skuIds);
        result.setSkuResults(results);
        List<Map<String, Object>> list = this.storehousePositionsService.listMaps();
        List<String> parentValue = StorehousePositionsSelectWrapper.fetchParentKey(list, Convert.toStr(detail.getPid()));
        result.setPidValue(parentValue);
        if (ToolUtil.isNotEmpty(detail.getPid())){
            StorehousePositions parent = this.storehousePositionsService.getById(detail.getPid());
            if (ToolUtil.isNotEmpty(parent)){
                result.setParent(BeanUtil.copyProperties(parent, StorehousePositionsResult.class));
            }
        }
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
    public List<StorehousePositionsResult> list(@RequestBody(required = false) StorehousePositionsParam storehousePositionsParam) {
        if (ToolUtil.isEmpty(storehousePositionsParam)) {
            storehousePositionsParam = new StorehousePositionsParam();
        }
        if (LoginContextHolder.getContext().isAdmin()) {
            return this.storehousePositionsService.findListBySpec(storehousePositionsParam, null);
        } else {
            DataScope dataScope = new DataScope(LoginContextHolder.getContext().getDeptDataScope(),LoginContextHolder.getContext().getTenantId());
            return this.storehousePositionsService.findListBySpec(storehousePositionsParam, dataScope);
        }

    }

    /**
     * 选择列表
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {


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
    public ResponseData treeView(@RequestParam(required = false) Long ids, String name) {


        QueryWrapper<StorehousePositions> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("display", 1);
        queryWrapper.orderByDesc("sort");
        queryWrapper.eq("tenant_id", LoginContextHolder.getContext().getTenantId());
        if (ToolUtil.isNotEmpty(ids)) {
            queryWrapper.in("storehouse_id", ids);
        }
        if (ToolUtil.isNotEmpty(name)) {  //模糊查询
            queryWrapper.like("name", name);
        }

        List<Map<String, Object>> list = this.storehousePositionsService.listMaps(queryWrapper);
        //从list中取出 storehouse_positions_id Long 类型集合
        List<Long> storehousePositionsIds = list.stream().map(item -> Convert.toLong(item.get("storehouse_positions_id"))).collect(Collectors.toList());

        List<StorehousePositionsBind> storehousePositionsBinds =storehousePositionsIds.size() == 0 ? new ArrayList<>() : storehousePositionsBindService.lambdaQuery().in(StorehousePositionsBind::getPositionId, storehousePositionsIds).eq(StorehousePositionsBind::getDisplay, 1).list();
        List<StorehousePositionsBindResult> storehousePositionsBindResults = BeanUtil.copyToList(storehousePositionsBinds, StorehousePositionsBindResult.class);
        storehousePositionsBindService.format(storehousePositionsBindResults);

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
            treeNode.setKey(Convert.toStr(item.get("storehouse_positions_id")));
            treeNode.setValue(Convert.toStr(item.get("storehouse_positions_id")));
            treeNode.setTitle(Convert.toStr(item.get("name")));
            treeNode.setLabel(Convert.toStr(item.get("name")));
            treeNode.setSort(Convert.toStr(item.get("sort")));
            treeNode.setNote(Convert.toStr(item.get("note")));
            treeViewNodes.add(treeNode);
        }
        for (TreeNode treeViewNode : treeViewNodes) {
            List<SkuList> objects = new ArrayList<>();
            for (StorehousePositionsBindResult storehousePositionsBindResult : storehousePositionsBindResults) {
                if (Convert.toStr(storehousePositionsBindResult.getPositionId()).equals(treeViewNode.getKey()) && ToolUtil.isNotEmpty(storehousePositionsBindResult.getSkuResult())) {
                    objects.add(storehousePositionsBindResult.getSkuResult());
                }
            }
            treeViewNode.setObject(objects);
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