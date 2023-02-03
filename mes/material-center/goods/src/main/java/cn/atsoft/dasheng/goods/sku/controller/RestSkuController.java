package cn.atsoft.dasheng.goods.sku.controller;


import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.service.RestCategoryService;
import cn.atsoft.dasheng.goods.sku.entity.RestSku;
import cn.atsoft.dasheng.goods.sku.model.params.RestSkuParam;
import cn.atsoft.dasheng.goods.sku.model.result.RestSkuResult;
import cn.atsoft.dasheng.goods.sku.service.RestSkuService;
import cn.atsoft.dasheng.goods.sku.wrapper.RestSkuSelectWrapper;
import cn.atsoft.dasheng.goods.spu.entity.RestSpu;
import cn.atsoft.dasheng.goods.spu.service.RestSpuService;
import cn.atsoft.dasheng.goods.unit.entity.RestUnit;
import cn.atsoft.dasheng.goods.unit.service.RestUnitService;
import cn.atsoft.dasheng.model.response.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("/sku/{version}")
@ApiVersion("2.0")
@Api(tags = "sku 管理")
public class RestSkuController extends BaseController {

    @Autowired
    private RestSkuService skuService;

    @Autowired
    private RestSpuService spuService;

    @Autowired
    private RestUnitService unitService;
//
//    @Autowired
//    private UserService userService; //管理员
//
//    @Autowired
//    private QualityPlanService qualityPlanService; //质检方案

    @Autowired
    private RestCategoryService spuClassificationService;


//    @Autowired
//    private PrintTemplateService printTemplateService; //编辑模板
//
//    @Autowired
//    private DictService dictService; //基础字典
//
//    @Autowired
//    private GeneralFormDataService generalFormDataService;
//
//    @Autowired
//    private InstockLogDetailService instockLogDetailService;


    /**
     * 直接物料 新增接口
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody RestSkuParam restSkuParam) {
        restSkuParam.setAddMethod(1);
//        restSkuParam.setSkuId(null);
        restSkuParam.setCreateTime(null);
        restSkuParam.setCreateUser(null);
        restSkuParam.setUpdateTime(null);
        restSkuParam.setUpdateUser(null);
        Map<String, RestSku> skuMap = this.skuService.add(restSkuParam);

//
//        if (ToolUtil.isNotEmpty(restSkuParam.getGeneralFormDataParams()) || restSkuParam.getGeneralFormDataParams().size() > 0) {
//            for (GeneralFormDataParam generalFormDataParam : restSkuParam.getGeneralFormDataParams()) {
//                generalFormDataParam.setTableName("goods_sku");
//            }
//            generalFormDataService.addBatch(restSkuParam.getGeneralFormDataParams());
//        }

        if (ToolUtil.isNotEmpty(skuMap.get("success"))) {
            return ResponseData.success(skuMap.get("success").getSkuId());
        } else {
            RestSku error = skuMap.get("error");
            RestSkuResult skuResult = new RestSkuResult();
            ToolUtil.copyProperties(error, skuResult);
            skuService.format(new ArrayList<RestSkuResult>() {{
                add(skuResult);
            }});
//            return ResponseData.error(BizExceptionEnum.USER_CHECK.getCode(), BizExceptionEnum.USER_CHECK.getMessage(), skuResult);
        }
        return ResponseData.success(skuMap.get("success").getSkuId());
    }


//    /**
//     * 直接物料 新增接口
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    @RequestMapping(value = "/batchAdd", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData addBatch(@RequestBody BatchSkuParam batchSkuParam) {
//        for (RestSkuParam restSkuParam : batchSkuParam.getSkuParams()) {
//            restSkuParam.setAddMethod(1);
//            restSkuParam.setSkuId(null);
//        }
//        this.skuService.batchAddSku(batchSkuParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 间接物料 新增接口
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    @RequestMapping(value = "/indirectAdd", method = RequestMethod.POST)
//    @ApiOperation("新增")
//    public ResponseData indirectAdd(@RequestBody RestSkuParam restSkuParam) {
//        restSkuParam.setAddMethod(2);
//        this.skuService.directAdd(restSkuParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 编辑接口
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    @RequestMapping(value = "/edit", method = RequestMethod.POST)
//    @BussinessLog(value = "修改sku", key = "name", dict = RestSkuParam.class)
//    @ApiOperation("编辑")
//    public ResponseData update(@RequestBody RestSkuParam restSkuParam) {
//        if (ToolUtil.isNotEmpty(restSkuParam.getGeneralFormDataParams()) && restSkuParam.getGeneralFormDataParams().size() > 0) {
//            for (GeneralFormDataParam generalFormDataParam : restSkuParam.getGeneralFormDataParams()) {
//                generalFormDataParam.setTableName("goods_sku");
//            }
//            generalFormDataService.addBatch(restSkuParam.getGeneralFormDataParams());
//        }
//        this.skuService.update(restSkuParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 编辑接口
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    @RequestMapping(value = "/editEnclosure", method = RequestMethod.POST)
//    @BussinessLog(value = "修改/上传 sku附件", key = "name", dict = RestSkuParam.class)
//    @ApiOperation("编辑")
//    @Permission
//    public ResponseData editEnclosure(@RequestBody(required = false) RestSkuParam restSkuParam) {
//        this.skuService.editEnclosure(restSkuParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 合并sku接口
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    @RequestMapping(value = "/mirageSku", method = RequestMethod.POST)
//    @BussinessLog(value = "合并sku", key = "name", dict = RestSkuParam.class)
//    @ApiOperation("编辑")
//    @Permission(name = "合并物料")
//    public ResponseData mirageSku(@RequestBody RestSkuParam restSkuParam) {
//        if (ToolUtil.isNotEmpty(restSkuParam.getGeneralFormDataParams()) || restSkuParam.getGeneralFormDataParams().size() > 0) {
//            for (GeneralFormDataParam generalFormDataParam : restSkuParam.getGeneralFormDataParams()) {
//                generalFormDataParam.setTableName("goods_sku");
//            }
//            generalFormDataService.addBatch(restSkuParam.getGeneralFormDataParams());
//        }
//        this.skuService.mirageSku(restSkuParam);
//        return ResponseData.success();
//    }
//
//    /**
//     * 删除接口
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    @RequestMapping(value = "/delete", method = RequestMethod.POST)
//    @BussinessLog(value = "删除sku", key = "name", dict = RestSkuParam.class)
//    @ApiOperation("删除")
//    public ResponseData delete(@RequestBody RestSkuParam restSkuParam) {
//        this.skuService.delete(restSkuParam);
//        return ResponseData.success();
//    }
//
//    @RequestMapping(value = "/addSkuFromSpu", method = RequestMethod.POST)
////    @BussinessLog(value = "删除sku", key = "name", dict = SkuParam.class)
//    @ApiOperation("删除")
//    public ResponseData skuParam(@RequestBody PartsParam param) {
//        return ResponseData.success(this.skuService.addSkuFromSpu(param));
//    }
//
//    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
//    @BussinessLog(value = "批量删除sku", key = "name", dict = RestSkuParam.class)
//    @ApiOperation("删除")
//    public ResponseData deleteBatch(@RequestBody RestSkuParam restSkuParam) {
//        this.skuService.deleteBatch(restSkuParam);
//        return ResponseData.success();
//    }
//
//
//    @RequestMapping(value = "/skuMessage", method = RequestMethod.POST)
//    public ResponseData skuMessage(@RequestBody RestSkuParam restSkuParam) {
//        String skuMessage = this.skuService.skuMessage(restSkuParam.getSkuId());
//        return ResponseData.success(skuMessage);
//    }
//
//
//    @RequestMapping(value = "/skuBindList", method = RequestMethod.POST)
//    public ResponseData skuBindList(@RequestBody SkuBindParam skuBindParam) {
//        List<SkuBind> skuBinds = this.skuService.skuBindList(skuBindParam);
//        return ResponseData.success(skuBinds);
//    }
//
    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-10-18
     */
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData detail(@RequestBody RestSkuParam restSkuParam) {
//
//        RestSkuResult sku = skuService.getSku(restSkuParam.getSkuId());
//        if (ToolUtil.isNotEmpty(sku.getSpuId())) {
//            RestSpu spu = spuService.getById(sku.getSpuId());
//            if (ToolUtil.isNotEmpty(spu.getUnitId())) {
//                RestUnit unit = unitService.getById(spu.getUnitId());
//                sku.setUnit(unit);
//            }
//            if (ToolUtil.isNotEmpty(spu.getSpuClassificationId())) {
//                RestCategory spuClassification1 = spuClassificationService.getById(spu.getSpuClassificationId());
//                sku.setSpuClass(spuClassification1.getSpuClassificationId());
//            }
//        }
//        if (ToolUtil.isNotEmpty(sku.getQualityPlanId())) {
//            QualityPlan plan = qualityPlanService.getById(sku.getQualityPlanId());
//            sku.setQualityPlan(plan);
//        }
//        User user = userService.getById(sku.getCreateUser());
//        if (ToolUtil.isNotEmpty(user)) {
//            sku.setCreateUserName(user.getName());
//        }
//
//        Dict dict = dictService.query().eq("code", "editSku").one();
//        boolean editSkuFlag = ToolUtil.isNotEmpty(dict) && dict.getStatus().equals("ENABLE");
//        sku.setEditSkuFlag(editSkuFlag);
//        return ResponseData.success(sku);
//
//    }
//
//    /**
//     * 查看详情接口
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    @ApiVersion("1.1")
//    @RequestMapping(value = "/{v}/detail", method = RequestMethod.POST)
//    @ApiOperation("详情")
//    public ResponseData detailV1(@RequestBody RestSkuParam restSkuParam) {
//
//        RestSkuResult sku = skuService.getSku(restSkuParam.getSkuId());
//        if (ToolUtil.isNotEmpty(sku.getSpuId())) {
//            RestSpu spu = spuService.getById(sku.getSpuId());
//            if (ToolUtil.isNotEmpty(spu.getUnitId())) {
//                RestUnit unit = unitService.getById(spu.getUnitId());
//                sku.setUnit(unit);
//            }
//            if (ToolUtil.isNotEmpty(spu.getSpuClassificationId())) {
////                SpuClassification spuClassification = spuClassificationService.getById(spu.getSpuClassificationId());
////                sku.setSpuClassification(spuClassification);  //产品
////
////                if (ToolUtil.isNotEmpty(spuClassification.getPid())) {
//                //分类
//                RestCategory spuClassification1 = spuClassificationService.getById(spu.getSpuClassificationId());
//                sku.setSpuClass(spuClassification1.getSpuClassificationId());
////                    sku.setSkuClass(spuClassification1);
////                }
//
//            }
//        }
//        if (ToolUtil.isNotEmpty(sku.getQualityPlanId())) {
//            QualityPlan plan = qualityPlanService.getById(sku.getQualityPlanId());
//            sku.setQualityPlan(plan);
//        }
//        User user = userService.getById(sku.getCreateUser());
//        if (ToolUtil.isNotEmpty(user)) {
//            sku.setCreateUserName(user.getName());
//        }
//
//
//        return ResponseData.success(sku);
//
//    }
//
//
//    /**
//     * 查询列表
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    @RequestMapping(value = "/list", method = RequestMethod.POST)
//    @ApiOperation("直接物料列表")
//    public PageInfo list(@RequestBody(required = false) RestSkuParam restSkuParam) {
//        if (ToolUtil.isEmpty(restSkuParam)) {
//            restSkuParam = new RestSkuParam();
//        }
//        if (ToolUtil.isNotEmpty(restSkuParam.getSkuName())) {
//            QueryLog queryLog = new QueryLog();
//            queryLog.setFormType("sku");
//            queryLog.setRecord(skuParam.getSkuName());
//        }
//
//        //ServiceMapper中区别于普通list因为排序方式不相同
//
//        return this.skuService.changePageBySpec(restSkuParam);
//    }
//
//    /**
//     * 根据md5
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    @RequestMapping(value = "/skuByMd5", method = RequestMethod.POST)
//    @ApiOperation("直接物料列表")
//    public ResponseData skuByMd5(@RequestBody(required = false) RestSkuParam restSkuParam) {
//        if (ToolUtil.isEmpty(restSkuParam)) {
//            restSkuParam = new RestSkuParam();
//        }
//        return ResponseData.success(this.skuService.getSkuByMd5(restSkuParam));
//    }
//
//    /**
//     * 查询列表
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    @RequestMapping(value = "/changeSkuPageList", method = RequestMethod.POST)
//    @ApiOperation("直接选择物料列表")
//    public PageInfo changeList(@RequestBody(required = false) RestSkuParam restSkuParam) {
//        if (ToolUtil.isEmpty(restSkuParam)) {
//            restSkuParam = new RestSkuParam();
//        }
//        return this.skuService.findPageBySpec(restSkuParam);
//    }
//
//    /**
//     * 查询列表
//     *
//     * @author
//     * @Date 2021-10-18
//     */
//    @RequestMapping(value = "/indirectList", method = RequestMethod.POST)
//    @ApiOperation("间接物料列表")
//    public PageInfo indirectList(@RequestBody(required = false) RestSkuParam restSkuParam) {
//        if (ToolUtil.isEmpty(restSkuParam)) {
//            restSkuParam = new RestSkuParam();
//        }
//        restSkuParam.setAddMethod(2);
//        return this.skuService.findPageBySpec(restSkuParam);
//    }
//
//
//    @RequestMapping(value = "/Alllist", method = RequestMethod.POST)
//    public ResponseData Alllist() {
//        List<RestSkuResult> skuResults = this.skuService.AllSku();
//        return ResponseData.success(skuResults);
//    }
//
//    /**
//     * 选择列表
//     *
//     * @author jazz
//     * @Date 2021-10-18
//     */
//    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
//    @ApiOperation("Select数据接口")
//    public ResponseData listSelect() {
//        List<Map<String, Object>> list = this.skuService.listMaps();
//        RestSkuSelectWrapper factory = new RestSkuSelectWrapper(list);
//        List<Map<String, Object>> result = factory.wrap();
//        return ResponseData.success(result);
//    }
//
//    /**
//     * 选择列表
//     *
//     * @author jazz
//     * @Date 2021-10-18
//     */
//    @RequestMapping(value = "/resultSkuByIds", method = RequestMethod.POST)
//    @ApiOperation("Select数据接口")
//    public ResponseData resultSkuByIds(@RequestBody(required = false) RestSkuParam restSkuParam) {
//        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(restSkuParam.getSkuIds());
//        return ResponseData.success(skuSimpleResults);
//    }
//
//    @RequestMapping(value = "/getSkuDrawing", method = RequestMethod.GET)
//    @ApiOperation("Select数据接口")
////    @Permission
//    public ResponseData getSkuDrawing(@RequestParam Long skuId) {
//        RestSku byId = this.skuService.getById(skuId);
//        if (ToolUtil.isNotEmpty(byId.getDrawing())) {
//            return ResponseData.success(skuService.strToMediaResults(byId.getDrawing()));
//
//        }
//        return ResponseData.success(new ArrayList<>());
//    }
//
//    @RequestMapping(value = "/getSkuFile", method = RequestMethod.GET)
//    @ApiOperation("Select数据接口")
////    @Permission
//    public ResponseData getSkuFile(@RequestParam Long skuId) {
//        RestSku byId = this.skuService.getById(skuId);
//        if (ToolUtil.isNotEmpty(byId.getFileId())) {
//            return ResponseData.success(skuService.strToMediaResults(byId.getFileId()));
//
//        }
//        return ResponseData.success(new ArrayList<>());
//    }

//    @RequestMapping(value = "/printSkuTemplate", method = RequestMethod.GET)
//    @ApiOperation("打印信息")
////    @Permission
//    public ResponseData printSkuTemplate(@RequestParam Long skuId) {
//        if (ToolUtil.isNotEmpty(skuId)) {
//            return ResponseData.success(printTemplateService.replaceInkindTemplate(skuId));
//        }
//        return ResponseData.success("");
//    }


//    @RequestMapping(value = "/skuLogDetail", method = RequestMethod.GET)
//    @ApiOperation("物料操作记录")
//    public ResponseData skuLogDetail(@RequestParam("skuId") Long skuId) {
//        List<SkuLogDetail> logDetailResults = instockLogDetailService.skuLogDetail(skuId);
//        return ResponseData.success(logDetailResults);
//    }

}


