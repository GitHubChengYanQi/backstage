package cn.atsoft.dasheng.orCode.controller;

import cn.atsoft.dasheng.app.entity.Material;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.service.MaterialService;
import cn.atsoft.dasheng.app.service.StorehouseService;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.entity.SpuClassification;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.model.result.SpuClassificationResult;
import cn.atsoft.dasheng.erp.model.result.SpuResult;
import cn.atsoft.dasheng.erp.model.result.StorehousePositionsResult;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.erp.service.SpuClassificationService;
import cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.erp.service.StorehousePositionsService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.model.result.OrCodeResult;
import cn.atsoft.dasheng.orCode.model.result.SpuRequest;
import cn.atsoft.dasheng.orCode.model.result.StoreHousePositionsRequest;
import cn.atsoft.dasheng.orCode.model.result.StoreHouseRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.core.base.controller.BaseController;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.hutool.core.convert.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 二维码控制器
 *
 * @author song
 * @Date 2021-10-29 10:23:27
 */
@RestController
@RequestMapping("/orCode")
@Api(tags = "二维码")
public class OrCodeController extends BaseController {

    @Autowired
    private OrCodeService orCodeService;
    @Autowired
    private OrCodeBindService orCodeBindService;
    @Autowired
    private SpuService spuService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private StorehousePositionsService storehousePositionsService;
    @Autowired
    private SpuClassificationService spuClassificationService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private SkuService skuService;

    /**
     * 新增接口
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增")
    public ResponseData addItem(@RequestBody OrCodeParam orCodeParam) {
        this.orCodeService.add(orCodeParam);
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
    public ResponseData update(@RequestBody OrCodeParam orCodeParam) {

        this.orCodeService.update(orCodeParam);
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
    public ResponseData delete(@RequestBody OrCodeParam orCodeParam) {
        this.orCodeService.delete(orCodeParam);
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
    public ResponseData<OrCodeResult> detail(@RequestBody OrCodeParam orCodeParam) {
        OrCode detail = this.orCodeService.getById(orCodeParam.getOrCodeId());
        OrCodeResult result = new OrCodeResult();
        ToolUtil.copyProperties(detail, result);
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
    public PageInfo<OrCodeResult> list(@RequestBody(required = false) OrCodeParam orCodeParam) {
        if (ToolUtil.isEmpty(orCodeParam)) {
            orCodeParam = new OrCodeParam();
        }
        return this.orCodeService.findPageBySpec(orCodeParam);
    }


    /**
     * 返回二维码
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/backCode", method = RequestMethod.GET)
    @ApiOperation("二维码")
    @Transactional
    public ResponseData backCode(@RequestParam String type, Long id) {
        OrCodeBind one = orCodeBindService.query().in("source", type).in("form_id", id).one();
        if (ToolUtil.isNotEmpty(one)) {
            return ResponseData.success(one.getOrCodeId());
        } else {
            OrCodeParam orCodeParam = new OrCodeParam();
            orCodeParam.setType(type);
            Long aLong = orCodeService.add(orCodeParam);
            OrCodeBindParam orCodeBindParam = new OrCodeBindParam();
            orCodeBindParam.setSource(type);
            orCodeBindParam.setFormId(id);
            orCodeBindParam.setOrCodeId(aLong);
            orCodeBindService.add(orCodeBindParam);
            return ResponseData.success(aLong);
        }
    }

    /**
     * 通过二维码返回真是数据
     *
     * @author song
     * @Date 2021-10-29
     */
    @RequestMapping(value = "/backObject", method = RequestMethod.GET)
    @Transactional
    public ResponseData backObject(@RequestParam Long id) {
        OrCodeBind codeBind = orCodeBindService.query().in("qr_code_id", id).one();
        if (ToolUtil.isEmpty(codeBind)) {
            throw new ServiceException(500, "当前二维码不合法");
        } else {
            String source = codeBind.getSource();
            switch (source) {
                case "spu":
                    Spu spu = spuService.query().eq("spu_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(spu)) {
                        throw new ServiceException(500, "当前物料不存在");
                    }
                    SpuResult spuResult = new SpuResult();
                    ToolUtil.copyProperties(spu, spuResult);
                    spuFormat(spuResult);
                    SpuRequest spuRequest = new SpuRequest();
                    spuRequest.setType("spu");
                    spuRequest.setResult(spuResult);
                    return ResponseData.success(spuRequest);

                case "storehouse":
                    Storehouse storehouse = storehouseService.query().eq("storehouse_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(storehouse)) {
                        throw new ServiceException(500, "当前仓库不存在");
                    }
                    StorehouseResult storehouseResult = new StorehouseResult();
                    ToolUtil.copyProperties(storehouse, storehouseResult);

                    StoreHouseRequest storeHouseRequest = new StoreHouseRequest();
                    storeHouseRequest.setType("storehouse");
                    storeHouseRequest.setResult(storehouseResult);
                    return ResponseData.success(storeHouseRequest);

                case "storehousePositions":
                    StorehousePositions storehousePositions = storehousePositionsService.query().in("storehouse_positions_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(storehousePositions)) {
                        throw new ServiceException(500, "当前库位不存在");
                    }
                    StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
                    ToolUtil.copyProperties(storehousePositions, storehousePositionsResult);
                    storehousePositionsFormat(storehousePositionsResult);
                    StoreHousePositionsRequest storeHousePositionsRequest = new StoreHousePositionsRequest();
                    storeHousePositionsRequest.setType("storehousePositions");
                    storeHousePositionsRequest.setResult(storehousePositionsResult);
                    return ResponseData.success(storeHousePositionsRequest);
            }
        }
        return ResponseData.success();
    }

    public void spuFormat(SpuResult spuResult) {
        SpuClassification spuClassification = spuResult.getSpuClassificationId() == null ? new SpuClassification() : spuClassificationService
                .query().in("spu_classification_id", spuResult.getSpuClassificationId()).one();
        Material material = materialService.query().in("material_id", spuResult.getMaterialId()).one();
        if (ToolUtil.isNotEmpty(spuClassification)) {
            SpuClassificationResult spuClassificationResult = new SpuClassificationResult();
            ToolUtil.copyProperties(spuClassification, spuClassificationResult);
            spuResult.setSpuClassificationResult(spuClassificationResult);
        }
        if (ToolUtil.isNotEmpty(material)) {
            spuResult.setMaterial(material);
        }
    }

    public void storehousePositionsFormat(StorehousePositionsResult storehousePositionsResult) {
        Storehouse storehouse = storehouseService.query().eq("storehouse_id", storehousePositionsResult.getStorehouseId()).one();
        Sku sku = skuService.query().in("sku_id", storehousePositionsResult.getSkuId()).one();

        if (ToolUtil.isNotEmpty(storehouse)) {
            StorehouseResult storehouseResult = new StorehouseResult();
            ToolUtil.copyProperties(storehouse, storehouseResult);
            storehousePositionsResult.setStorehouseResult(storehouseResult);
        }
        if (ToolUtil.isNotEmpty(sku)) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku, skuResult);
            storehousePositionsResult.setSkuResult(skuResult);
        }
    }
}


