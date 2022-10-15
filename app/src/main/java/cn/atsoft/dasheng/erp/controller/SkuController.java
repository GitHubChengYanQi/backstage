package cn.atsoft.dasheng.erp.controller;

import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.service.UnitService;
import cn.atsoft.dasheng.appBase.model.result.MediaResult;
import cn.atsoft.dasheng.base.auth.annotion.Permission;
import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.config.api.version.ApiVersion;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.BatchSkuParam;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.pojo.SkuBind;
import cn.atsoft.dasheng.erp.pojo.SkuBindParam;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.wrapper.SkuSelectWrapper;
import cn.atsoft.dasheng.generalForm.model.params.GeneralFormDataParam;
import cn.atsoft.dasheng.generalForm.service.GeneralFormDataService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ErrorResponseData;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.model.response.SuccessResponseData;
import cn.atsoft.dasheng.printTemplate.service.PrintTemplateService;
import cn.atsoft.dasheng.query.entity.QueryLog;
import cn.atsoft.dasheng.query.service.QueryLogService;
import cn.atsoft.dasheng.sys.core.exception.enums.BizExceptionEnum;
import cn.atsoft.dasheng.sys.modular.rest.service.RestUserService;
import cn.atsoft.dasheng.sys.modular.system.entity.Dict;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.DictService;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    @Autowired
    private QueryLogService queryLogService;
    @Autowired
    private PrintTemplateService printTemplateService;
    @Autowired
    private DictService dictService;
    @Autowired
    private GeneralFormDataService generalFormDataService;


    /**
     * 直接物料 新增接口
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody SkuParam skuParam) {
        skuParam.setAddMethod(1);
        skuParam.setSkuId(null);
        skuParam.setCreateTime(null);
        skuParam.setCreateUser(null);
        skuParam.setUpdateTime(null);
        skuParam.setUpdateUser(null);
        Map<String, Sku> skuMap = this.skuService.add(skuParam);


        if (ToolUtil.isNotEmpty(skuParam.getGeneralFormDataParams()) || skuParam.getGeneralFormDataParams().size() > 0) {
            for (GeneralFormDataParam generalFormDataParam : skuParam.getGeneralFormDataParams()) {
                generalFormDataParam.setTableName("goods_sku");
            }
            generalFormDataService.addBatch(skuParam.getGeneralFormDataParams());
        }

        if (ToolUtil.isNotEmpty(skuMap.get("success"))) {
            return ResponseData.success(skuMap.get("success").getSkuId());
        } else {
            Sku error = skuMap.get("error");
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(error, skuResult);
            skuService.format(new ArrayList<SkuResult>() {{
                add(skuResult);
            }});
            return ResponseData.error(BizExceptionEnum.USER_CHECK.getCode(), BizExceptionEnum.USER_CHECK.getMessage(), skuResult);
        }
    }


    /**
     * 直接物料 新增接口
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/batchAdd", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addBatch(@RequestBody BatchSkuParam batchSkuParam) {
        for (SkuParam skuParam : batchSkuParam.getSkuParams()) {
            skuParam.setAddMethod(1);
            skuParam.setSkuId(null);
        }
        this.skuService.batchAddSku(batchSkuParam);
        return ResponseData.success();
    }

    /**
     * 间接物料 新增接口
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/indirectAdd", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData indirectAdd(@RequestBody SkuParam skuParam) {
        skuParam.setAddMethod(2);
        this.skuService.directAdd(skuParam);
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
        if (ToolUtil.isNotEmpty(skuParam.getGeneralFormDataParams()) || skuParam.getGeneralFormDataParams().size() > 0) {
            for (GeneralFormDataParam generalFormDataParam : skuParam.getGeneralFormDataParams()) {
                generalFormDataParam.setTableName("goods_sku");
            }
            generalFormDataService.addBatch(skuParam.getGeneralFormDataParams());
        }
        this.skuService.update(skuParam);
        return ResponseData.success();
    }
    /**
     * 编辑接口
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/editEnclosure", method = RequestMethod.POST)
    @BussinessLog(value = "修改/上传 sku附件", key = "name", dict = SkuParam.class)
    @ApiOperation("编辑")
    @Permission
    public ResponseData editEnclosure(@RequestBody(required = false) SkuParam skuParam) {
        this.skuService.editEnclosure(skuParam);
        return ResponseData.success();
    }

    /**
     * 合并sku接口
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/mirageSku", method = RequestMethod.POST)
    @BussinessLog(value = "合并sku", key = "name", dict = SkuParam.class)
    @ApiOperation("编辑")
    @Permission(name = "合并物料")
    public ResponseData mirageSku(@RequestBody SkuParam skuParam) {
        if (ToolUtil.isNotEmpty(skuParam.getGeneralFormDataParams()) || skuParam.getGeneralFormDataParams().size() > 0) {
            for (GeneralFormDataParam generalFormDataParam : skuParam.getGeneralFormDataParams()) {
                generalFormDataParam.setTableName("goods_sku");
            }
            generalFormDataService.addBatch(skuParam.getGeneralFormDataParams());
        }
        this.skuService.mirageSku(skuParam);
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
    public ResponseData delete(@RequestBody SkuParam skuParam) {
        this.skuService.delete(skuParam);
        return ResponseData.success();
    }

    @RequestMapping(value = "/addSkuFromSpu", method = RequestMethod.POST)
//    @BussinessLog(value = "删除sku", key = "name", dict = SkuParam.class)
    @ApiOperation("删除")
    public ResponseData skuParam(@RequestBody PartsParam param) {
        return ResponseData.success(this.skuService.addSkuFromSpu(param));
    }

    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    @BussinessLog(value = "批量删除sku", key = "name", dict = SkuParam.class)
    @ApiOperation("删除")
    public ResponseData deleteBatch(@RequestBody SkuParam skuParam) {
        this.skuService.deleteBatch(skuParam);
        return ResponseData.success();
    }


    @RequestMapping(value = "/skuMessage", method = RequestMethod.POST)
    public ResponseData skuMessage(@RequestBody SkuParam skuParam) {
        String skuMessage = this.skuService.skuMessage(skuParam.getSkuId());
        return ResponseData.success(skuMessage);
    }


    @RequestMapping(value = "/skuBindList", method = RequestMethod.POST)
    public ResponseData skuBindList(@RequestBody SkuBindParam skuBindParam) {
        List<SkuBind> skuBinds = this.skuService.skuBindList(skuBindParam);
        return ResponseData.success(skuBinds);
    }

    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detail(@RequestBody SkuParam skuParam) {

        SkuResult sku = skuService.getSku(skuParam.getSkuId());
        if (ToolUtil.isNotEmpty(sku.getSpuId())) {
            Spu spu = spuService.getById(sku.getSpuId());
            if (ToolUtil.isNotEmpty(spu.getUnitId())) {
                Unit unit = unitService.getById(spu.getUnitId());
                sku.setUnit(unit);
            }
            if (ToolUtil.isNotEmpty(spu.getSpuClassificationId())) {
//                SpuClassification spuClassification = spuClassificationService.getById(spu.getSpuClassificationId());
//                sku.setSpuClassification(spuClassification);  //产品
//
//                if (ToolUtil.isNotEmpty(spuClassification.getPid())) {
                //分类
                SpuClassification spuClassification1 = spuClassificationService.getById(spu.getSpuClassificationId());
                sku.setSpuClass(spuClassification1.getSpuClassificationId());
//                    sku.setSkuClass(spuClassification1);
//                }

            }
        }
        if (ToolUtil.isNotEmpty(sku.getQualityPlanId())) {
            QualityPlan plan = qualityPlanService.getById(sku.getQualityPlanId());
            sku.setQualityPlan(plan);
        }
        User user = userService.getById(sku.getCreateUser());
        if (ToolUtil.isNotEmpty(user)) {
            sku.setCreateUserName(user.getName());
        }

        Dict dict = dictService.query().eq("code", "editSku").one();
        boolean editSkuFlag = ToolUtil.isNotEmpty(dict) && dict.getStatus().equals("ENABLE");
        sku.setEditSkuFlag(editSkuFlag);
        return ResponseData.success(sku);

    }
    /**
     * 查看详情接口
     *
     * @author
     * @Date 2021-10-18
     */
    @ApiVersion("1.1")
    @RequestMapping(value = "/{v}/detail", method = RequestMethod.POST)
    @ApiOperation("详情")
    public ResponseData detailV1(@RequestBody SkuParam skuParam) {

        SkuResult sku = skuService.getSku(skuParam.getSkuId());
        if (ToolUtil.isNotEmpty(sku.getSpuId())) {
            Spu spu = spuService.getById(sku.getSpuId());
            if (ToolUtil.isNotEmpty(spu.getUnitId())) {
                Unit unit = unitService.getById(spu.getUnitId());
                sku.setUnit(unit);
            }
            if (ToolUtil.isNotEmpty(spu.getSpuClassificationId())) {
//                SpuClassification spuClassification = spuClassificationService.getById(spu.getSpuClassificationId());
//                sku.setSpuClassification(spuClassification);  //产品
//
//                if (ToolUtil.isNotEmpty(spuClassification.getPid())) {
                //分类
                SpuClassification spuClassification1 = spuClassificationService.getById(spu.getSpuClassificationId());
                sku.setSpuClass(spuClassification1.getSpuClassificationId());
//                    sku.setSkuClass(spuClassification1);
//                }

            }
        }
        if (ToolUtil.isNotEmpty(sku.getQualityPlanId())) {
            QualityPlan plan = qualityPlanService.getById(sku.getQualityPlanId());
            sku.setQualityPlan(plan);
        }
        User user = userService.getById(sku.getCreateUser());
        if (ToolUtil.isNotEmpty(user)) {
            sku.setCreateUserName(user.getName());
        }


        return ResponseData.success(sku);

    }


    /**
     * 查询列表
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("直接物料列表")
    public PageInfo list(@RequestBody(required = false) SkuParam skuParam) {
        if (ToolUtil.isEmpty(skuParam)) {
            skuParam = new SkuParam();
        }
        if (ToolUtil.isNotEmpty(skuParam.getSkuName())) {
            QueryLog queryLog = new QueryLog();
            queryLog.setFormType("sku");
            queryLog.setRecord(skuParam.getSkuName());
        }

        //ServiceMapper中区别于普通list因为排序方式不相同

        return this.skuService.changePageBySpec(skuParam);
    }

    /**
     * 根据md5
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/skuByMd5", method = RequestMethod.POST)
    @ApiOperation("直接物料列表")
    public ResponseData skuByMd5(@RequestBody(required = false) SkuParam skuParam) {
        if (ToolUtil.isEmpty(skuParam)) {
            skuParam = new SkuParam();
        }
        return ResponseData.success(this.skuService.getSkuByMd5(skuParam));
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/changeSkuPageList", method = RequestMethod.POST)
    @ApiOperation("直接选择物料列表")
    public PageInfo changeList(@RequestBody(required = false) SkuParam skuParam) {
        if (ToolUtil.isEmpty(skuParam)) {
            skuParam = new SkuParam();
        }
        return this.skuService.findPageBySpec(skuParam);
    }

    /**
     * 查询列表
     *
     * @author
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/indirectList", method = RequestMethod.POST)
    @ApiOperation("间接物料列表")
    public PageInfo indirectList(@RequestBody(required = false) SkuParam skuParam) {
        if (ToolUtil.isEmpty(skuParam)) {
            skuParam = new SkuParam();
        }
        skuParam.setAddMethod(2);
        return this.skuService.findPageBySpec(skuParam);
    }


    @RequestMapping(value = "/Alllist", method = RequestMethod.POST)
    public ResponseData Alllist() {
        List<SkuResult> skuResults = this.skuService.AllSku();
        return ResponseData.success(skuResults);
    }

    /**
     * 选择列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/listSelect", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData listSelect() {
        List<Map<String, Object>> list = this.skuService.listMaps();
        SkuSelectWrapper factory = new SkuSelectWrapper(list);
        List<Map<String, Object>> result = factory.wrap();
        return ResponseData.success(result);
    }

    /**
     * 选择列表
     *
     * @author jazz
     * @Date 2021-10-18
     */
    @RequestMapping(value = "/resultSkuByIds", method = RequestMethod.POST)
    @ApiOperation("Select数据接口")
    public ResponseData resultSkuByIds(@RequestBody(required = false) SkuParam skuParam) {
        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuParam.getSkuIds());
        return ResponseData.success(skuSimpleResults);
    }

    @RequestMapping(value = "/getSkuDrawing", method = RequestMethod.GET)
    @ApiOperation("Select数据接口")
//    @Permission
    public ResponseData getSkuDrawing(@RequestParam Long skuId) {
        Sku byId = this.skuService.getById(skuId);
        if (ToolUtil.isNotEmpty(byId.getDrawing())) {
            return ResponseData.success(skuService.strToMediaResults(byId.getDrawing()));

        }
        return ResponseData.success(new ArrayList<>());
    }
    @RequestMapping(value = "/getSkuFile", method = RequestMethod.GET)
    @ApiOperation("Select数据接口")
//    @Permission
    public ResponseData getSkuFile(@RequestParam Long skuId) {
        Sku byId = this.skuService.getById(skuId);
        if (ToolUtil.isNotEmpty(byId.getFileId())) {
            return ResponseData.success(skuService.strToMediaResults(byId.getFileId()));

        }
        return ResponseData.success(new ArrayList<>());
    }
    @RequestMapping(value = "/printSkuTemplate", method = RequestMethod.GET)
    @ApiOperation("打印信息")
//    @Permission
    public ResponseData printSkuTemplate(@RequestParam Long skuId) {
        if (ToolUtil.isNotEmpty(skuId)) {
            return ResponseData.success(printTemplateService.replaceInkindTemplate(skuId));
        }
        return ResponseData.success("");
    }


}


