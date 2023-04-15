package cn.atsoft.dasheng.app.controller;

import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.model.result.Item;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.model.result.PartsResult;
import cn.atsoft.dasheng.app.service.PartsService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.atsoft.dasheng.app.wrapper.PartsSelectWrapper;
import cn.atsoft.dasheng.base.pojo.node.TreeNode;
import cn.atsoft.dasheng.core.treebuild.DefaultTreeBuildFactory;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    @Autowired
    private ErpPartsDetailService erpPartsDetailService;

    @Autowired
    private SkuService skuService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-21
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody PartsParam partsParam) {
        if (ToolUtil.isNotEmpty(partsParam) && ToolUtil.isEmpty(partsParam.getSkuId())) {
            if (ToolUtil.isNotEmpty(partsParam.getItem().getSkuId())) {
                Sku sku = skuService.getById(partsParam.getItem().getSkuId());
                if (ToolUtil.isNotEmpty(sku) && ToolUtil.isNotEmpty(sku.getSpuId())) {
                    partsParam.setSpuId(sku.getSpuId());
                    partsParam.setSkuId(sku.getSkuId());
                }
            } else {
                if (ToolUtil.isNotEmpty(partsParam.getItem().getSpuId())) {
                    partsParam.setSpuId(partsParam.getItem().getSpuId());
                }
            }
        }
        Parts parts = this.partsService.add(partsParam);
        return ResponseData.success(parts);
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
        Parts parts = this.partsService.getById(partsParam.getPartsId());
        if (parts.getStatus() == 99) {
            this.partsService.updateAdd(partsParam);
        } else {
            this.partsService.update(partsParam);
        }
        return ResponseData.success();
    }


    @RequestMapping(value = "/release", method = RequestMethod.POST)
    public ResponseData release(@RequestBody @Valid PartsParam partsParam) {
        this.partsService.release(partsParam.getPartsId());
        return ResponseData.success();
    }

    /**
     * 最顶级物料
     *
     * @return
     */
    @RequestMapping(value = "/startAnalyse", method = RequestMethod.GET)
    public ResponseData startAnalyse() {
        this.partsService.startAnalyse();
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
    public ResponseData delete(@RequestBody PartsParam partsParam) {
        this.partsService.delete(partsParam);
        return ResponseData.success();
    }

    /**
     * bom
     *
     * @author song
     * @Date 2021-10-21
     */
    @RequestMapping(value = "/getBOM", method = RequestMethod.GET)
    public ResponseData getBOM(@Param("partId") Long partId, String type) {
        PartsResult bom = this.partsService.getBOM(partId);
        return ResponseData.success(bom);
    }


    @RequestMapping(value = "/getSkuIdsByBom", method = RequestMethod.GET)
    public ResponseData getSkuIdsByBom(@Param("skuId") Long skuId) {
        List<Long> skuIdsByBom = this.partsService.getSkuIdsByBom(skuId);
        return ResponseData.success(skuIdsByBom);
    }

    /**
     * 查看详情接口
     *
     * @author song
     * @Date 2021-10-21
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody PartsParam partsParam) {
        Parts detail = this.partsService.getById(partsParam.getPartsId());
        PartsResult result = new PartsResult();
        if (ToolUtil.isNotEmpty(detail)) {
            ToolUtil.copyProperties(detail, result);
        }

        QueryWrapper<ErpPartsDetail> erpPartsDetailQueryWrapper = new QueryWrapper<>();
        erpPartsDetailQueryWrapper.eq("parts_id", detail.getPartsId());
        erpPartsDetailQueryWrapper.eq("display", 1);
        List<ErpPartsDetail> erpPartsDetails = erpPartsDetailService.list(erpPartsDetailQueryWrapper);
        List<ErpPartsDetailResult> erpPartsDetailParams = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (ErpPartsDetail erpPartsDetail : erpPartsDetails) {
            ErpPartsDetailResult erpPartsDetailResult = new ErpPartsDetailResult();
            ToolUtil.copyProperties(erpPartsDetail, erpPartsDetailResult);
            erpPartsDetailResult.setAutoOutstock(erpPartsDetail.getAutoOutstock());
            erpPartsDetailResult.setPartsAttributes(erpPartsDetail.getAttribute());
            erpPartsDetailParams.add(erpPartsDetailResult);
            skuIds.add(erpPartsDetail.getSkuId());
        }
        List<Long> childrenIds = erpPartsDetails.stream().map(ErpPartsDetail::getVersionBomId).collect(Collectors.toList());
        List<Parts> parts = childrenIds.size() == 0 ? new ArrayList<>() : partsService.listByIds(childrenIds);


        Item item = new Item();
        if (ToolUtil.isNotEmpty(detail.getSkuId())) {
            item.setSkuId(detail.getSkuId());
        }
        result.setItem(item);

        List<SkuResult> skuResults = skuService.formatSkuResult(skuIds);

        if (skuResults.size() > 0) {
            result.setSkuResult(skuResults.get(0));
        }
        for (ErpPartsDetailResult erpPartsDetailParam : erpPartsDetailParams) {
            for (SkuResult skuResult : skuResults) {
                if (skuResult.getSkuId().equals(erpPartsDetailParam.getSkuId())) {
                    erpPartsDetailParam.setSkuResult(skuResult);
                    erpPartsDetailParam.setCoding(skuResult.getStandard());
                    break;
                }
            }
            for (Parts part : parts) {
                if (ToolUtil.isNotEmpty(erpPartsDetailParam.getVersionBomId()) && erpPartsDetailParam.getVersionBomId().equals(part.getPartsId())){
                    erpPartsDetailParam.setVersion(part.getName());
                    break;
                }
            }
        }
        result.setParts(erpPartsDetailParams);


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
    public PageInfo list(@RequestBody(required = false) PartsParam partsParam) {
        if (ToolUtil.isEmpty(partsParam)) {
            partsParam = new PartsParam();
        }
        if (ToolUtil.isNotEmpty(partsParam.getPidValue())) {
            List<String> pidValue = partsParam.getPidValue();
            partsParam.setPid(Long.valueOf(pidValue.get(pidValue.size() - 1)));
        }
        return this.partsService.findPageBySpec(partsParam);
    }

    @RequestMapping(value = "/getdetails", method = RequestMethod.POST)
    public ResponseData getdetails(@RequestBody PartsParam param) {
        List<PartsResult> results = this.partsService.getdetails(param.getPartIds());
        return ResponseData.success(results);
    }

    @RequestMapping(value = "/getByBomId", method = RequestMethod.POST)
    public ResponseData getByBomId(@RequestBody PartsParam param) {

        return ResponseData.success(this.partsService.getByBomId(param.getPartsId(), param.getNumber()));
    }

    /**
     * 查询列表
     *
     * @author song
     * @Date 2021-10-21
     */
    @RequestMapping(value = "/oldList", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo oldList(@RequestBody(required = false) PartsParam partsParam) {
        if (ToolUtil.isEmpty(partsParam)) {
            partsParam = new PartsParam();
        }
        if (ToolUtil.isNotEmpty(partsParam.getPidValue())) {
            List<String> pidValue = partsParam.getPidValue();
            partsParam.setPid(Long.valueOf(pidValue.get(pidValue.size() - 1)));
        }
        return this.partsService.oldFindPageBySpec(partsParam);
    }

    /**
     * 选择列表
     *
     * @author song
     * @Date 2021-10-21
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect(@RequestBody(required = false) PartsParam partsParam) {
        QueryWrapper<Parts> partsQueryWrapper = new QueryWrapper<>();
        partsQueryWrapper.eq("display", 1);
        if (ToolUtil.isNotEmpty(partsParam)) {
            if (ToolUtil.isNotEmpty(partsParam.getType())) {
                partsQueryWrapper.eq("type", partsParam.getType());
            }
            if (ToolUtil.isNotEmpty(partsParam.getStatus())) {
                partsQueryWrapper.eq("status", partsParam.getStatus());
            }
        }
        List<Map<String, Object>> list = this.partsService.listMaps(partsQueryWrapper);
        PartsSelectWrapper factory = new PartsSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
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
    public ResponseData treeView() {
        List<Map<String, Object>> list = this.partsService.listMaps();

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

    /**
     * 返回詳情結合
     *
     * @author song
     * @Date 2021-10-26
     */
    @RequestMapping(value = "/backDetails", method = RequestMethod.GET)
    @ApiOperation("返回子表集合")
    public ResponseData backDetails(@RequestParam Long id, Long partsId, String type) {
        List<ErpPartsDetailResult> detailResults = this.partsService.backDetails(id, partsId, type);
        return ResponseData.success(detailResults);
    }

    /**
     * 返回历史u详情数据
     *
     * @param id
     * @param partsId
     * @return
     */
    @RequestMapping(value = "/oldBackDetails", method = RequestMethod.GET)
    @ApiOperation("返回子表集合")
    public ResponseData oldBackDetails(@RequestParam Long id, Long partsId) {
        List<ErpPartsDetailResult> detailResults = this.partsService.oldBackDetails(id, partsId);
        return ResponseData.success(detailResults);
    }

    @RequestMapping(value = "getTree", method = RequestMethod.GET)
    @ApiOperation("返回子表集合")
    public ResponseData getTree(@RequestParam Long id) {
        List<PartsResult> treeParts = this.partsService.getTreeParts(id);
        return ResponseData.success(treeParts);
    }


    @RequestMapping(value = "getParent", method = RequestMethod.GET)
    @ApiOperation("返回当前父级集合")
    public ResponseData getParent(@RequestParam Long id) {
        List<PartsResult> treeParts = this.partsService.getParent(id);
        return ResponseData.success(treeParts);
    }

    /**
     * 查看物料历史BOM
     */
    @RequestMapping(value = "/bomsByskuId", method = RequestMethod.GET)
    @ApiOperation("点击查询")
    public PageInfo bomsBySkuId(@RequestParam Long skuId) {
        return this.partsService.findPageBySkuId(skuId);
    }

}


