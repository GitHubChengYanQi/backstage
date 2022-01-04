package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.QualityPlanDetailParam;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.params.SpuParam;
import cn.atsoft.dasheng.erp.model.result.AttributeValuesResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.wrapper.SkuSelectWrapper;
import cn.atsoft.dasheng.erp.wrapper.SpuSelectWrapper;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * sku表控制器
 *
 * @author 
 * @Date 2021-10-18 14:14:21
 */
@RestController
@RequestMapping("/sku")
@Api(tags = "sku表")
public class SkuController extends BaseController {
    @Autowired
    private ItemAttributeService itemAttributeService;
    @Autowired
    private AttributeValuesService attributeValuesService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private SpuService spuService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private UserService userService;
    @Autowired
    private QualityPlanService qualityPlanService;
    @Autowired
    private SpuClassificationService spuClassificationService;

    /**
     * 新增接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SkuParam skuParam) {
        this.skuService.add(skuParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @BussinessLog(value = "修改sku", key = "name", dict = SkuParam.class)
    @ApiOperation("编辑")
    public ResponseData update(@RequestBody SkuParam skuParam) {

        this.skuService.update(skuParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @BussinessLog(value = "删除sku", key = "name", dict = SkuParam.class)
    @ApiOperation("删除")
    public ResponseData delete(@RequestBody SkuParam skuParam)  {
        this.skuService.delete(skuParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    @BussinessLog(value = "批量删除sku", key = "name", dict = SkuParam.class)
    @ApiOperation("删除")
    public ResponseData deleteBatch(@RequestBody SkuParam skuParam)  {
        this.skuService.deleteBatch(skuParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData<SkuResult> detail(@RequestBody SkuParam skuParam) {
        Sku detail = this.skuService.getById(skuParam.getSkuId());
        SkuResult result = new SkuResult();
        ToolUtil.copyProperties(detail, result);
        SkuResult sku = skuService.getSku(detail.getSkuId());
        if (ToolUtil.isNotEmpty(detail.getSpuId())) {
            Spu spu = spuService.getById(detail.getSpuId());
            if (ToolUtil.isNotEmpty(spu)&&ToolUtil.isNotEmpty(spu.getUnitId())){
                Unit unit = unitService.getById(spu.getUnitId());
                sku.setUnit(unit);
            }
            if (ToolUtil.isNotEmpty(spu.getSpuClassificationId())) {
                SpuClassification spuClassification = spuClassificationService.getById(spu.getSpuClassificationId());
                sku.setSpuClassification(spuClassification);
            }
        }
        if (ToolUtil.isNotEmpty(detail.getQualityPlanId())) {
            QualityPlan plan = qualityPlanService.getById(detail.getQualityPlanId());
            sku.setQualityPlan(plan);
        }

        User user = userService.getById(detail.getCreateUser());
        sku.setCreateUserName(user.getName());
        return ResponseData.success(sku);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("列表")
    public PageInfo<SkuResult> list(@RequestBody(required = false) SkuParam skuParam) {
        if(ToolUtil.isEmpty(skuParam)){
            skuParam = new SkuParam();
        }
        return this.skuService.findPageBySpec(skuParam);
    }

    /**
     * 选择列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData<List<Map<String, Object>>> listSelect() {
        List<Map<String, Object>> list = this.skuService.listMaps();
        SkuSelectWrapper factory = new SkuSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }




}


