package cn.atsoft.dasheng.orCode.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.InstockParam;
import cn.atsoft.dasheng.app.model.result.*;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.InkindParam;
import cn.atsoft.dasheng.erp.model.params.InstockListParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.model.result.CategoryResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.form.entity.FormDataValue;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.mapper.OrCodeMapper;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.model.result.*;
import cn.atsoft.dasheng.orCode.model.result.InstockRequest;
import cn.atsoft.dasheng.orCode.model.result.StockRequest;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.model.result.UserResult;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 二维码 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-29
 */
@Service
public class OrCodeServiceImpl extends ServiceImpl<OrCodeMapper, OrCode> implements OrCodeService {
    @Autowired
    private SpuClassificationService spuClassificationService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StorehouseService storehouseService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private StorehousePositionsService storehousePositionsService;
    @Autowired
    private OrCodeBindService orCodeBindService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private InstockListService instockListService;
    @Autowired
    private InstockOrderService instockOrderService;
    @Autowired
    private StockService stockService;
    @Autowired
    private UserService userService;
    @Autowired
    private InstockService instockService;
    @Autowired
    private OutstockService outstockService;
    @Autowired
    private OutstockListingService outstockListingService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private QualityTaskService qualityTaskService;


    @Override
    @Transactional
    public Long add(OrCodeParam param) {
        OrCode entity = getEntity(param);
        this.save(entity);
        return entity.getOrCodeId();
    }

    @BussinessLog
    @Override
    public void delete(OrCodeParam param) {
//        this.removeById(getKey(param));
        throw new ServiceException(500, "不可以删除");
    }

    @BussinessLog
    @Override
    public void update(OrCodeParam param) {
//        OrCode oldEntity = getOldEntity(param);
//        OrCode newEntity = getEntity(param);
//        ToolUtil.copyProperties(newEntity, oldEntity);
//        this.updateById(newEntity);
        throw new ServiceException(500, "不可以修改");
    }

    @Override
    public OrCodeResult findBySpec(OrCodeParam param) {
        return null;
    }

    @Override
    public List<OrCodeResult> findListBySpec(OrCodeParam param) {
        return null;
    }

    @Override
    public PageInfo<OrCodeResult> findPageBySpec(OrCodeParam param) {
        Page<OrCodeResult> pageContext = getPageContext();
        IPage<OrCodeResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    public void format(List<OrCodeResult> data) {
        for (OrCodeResult datum : data) {
            Object obj = orcodeBackObj(datum.getOrCodeId());
            datum.setObject(obj);
        }

    }


    @Override
    public void spuFormat(SpuResult spuResult) {
        //查询分类
        SpuClassification spuClassification = spuResult.getSpuClassificationId() == null ? new SpuClassification() : spuClassificationService
                .query().in("spu_classification_id", spuResult.getSpuClassificationId()).one();
        Material material = materialService.query().in("material_id", spuResult.getMaterialId()).one();
        //查询单位
        Unit unit = spuResult.getUnitId() == null ? new Unit() : unitService.query().eq("unit_id", spuResult.getUnitId()).one();
        //返回类目
        Category category = spuResult.getCategoryId() == null ? new Category() : categoryService.query().in("category_id", spuResult.getCategoryId()).one();
        if (ToolUtil.isNotEmpty(category)) {
            CategoryResult categoryResult = new CategoryResult();
            ToolUtil.copyProperties(category, categoryResult);
            spuResult.setCategoryResult(categoryResult);
        }

        if (ToolUtil.isNotEmpty(unit)) {
            UnitResult unitResult = new UnitResult();
            ToolUtil.copyProperties(unit, unitResult);
            spuResult.setUnitResult(unitResult);
        }

        if (ToolUtil.isNotEmpty(spuClassification)) {
            SpuClassificationResult spuClassificationResult = new SpuClassificationResult();
            ToolUtil.copyProperties(spuClassification, spuClassificationResult);
            spuResult.setSpuClassificationResult(spuClassificationResult);
        }
        if (ToolUtil.isNotEmpty(material)) {
            spuResult.setMaterial(material);
        }
    }

    @Override
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

    @Override
    public void stockFormat(StockResult stockResult) {
        Long storehouseId = stockResult.getStorehouseId();
        Long brandId = stockResult.getBrandId();
        Long skuId = stockResult.getSkuId();

        if (ToolUtil.isNotEmpty(storehouseId)) {
            Storehouse storehouse = storehouseService.query().eq("storehouse_id", storehouseId).one();
            if (ToolUtil.isNotEmpty(storehouse)) {
                StorehouseResult storehouseResult = new StorehouseResult();
                ToolUtil.copyProperties(storehouse, storehouseResult);
                stockResult.setStorehouseResult(storehouseResult);
            }
        }

        if (ToolUtil.isNotEmpty(brandId)) {
            Brand brand = brandService.query().eq("brand_id", brandId).one();
            if (ToolUtil.isNotEmpty(brand)) {
                BrandResult brandResult = new BrandResult();
                ToolUtil.copyProperties(brand, brandResult);
                stockResult.setBrandResult(brandResult);
            }
        }
        if (ToolUtil.isNotEmpty(skuId)) {
            List<BackSku> backSkus = skuService.backSku(skuId);
            SpuResult spuResult = skuService.backSpu(skuId);
            stockResult.setBackSkus(backSkus);
            stockResult.setSpuResult(spuResult);
        }

    }

    @Override
    public void storehouseFormat(StorehouseResult storehouseResult) {
        Long storehouseId = storehouseResult.getStorehouseId();
        if (ToolUtil.isNotEmpty(storehouseId)) {
            List<StorehousePositions> positions = storehousePositionsService.query().in("storehouse_id", storehouseId).list();
            List<StorehousePositionsResult> list = new ArrayList<>();
            for (StorehousePositions position : positions) {
                StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
                ToolUtil.copyProperties(position, storehousePositionsResult);
                list.add(storehousePositionsResult);
            }
            storehouseResult.setStorehousePositionsResults(list);
        }
    }

    /**
     * 批量绑定
     *
     * @param ids
     * @param source
     * @return
     */
    @Override
    @Transactional
    public List<Long> backBatchCode(List<Long> ids, String source) {
        //判断重复
        long count = ids.stream().distinct().count();
        if (ids.size() > count) {
            throw new ServiceException(500, "不可以填写重复数据");
        }
        if (ToolUtil.isEmpty(source)) {
            throw new ServiceException(500, "请传入绑定类型");
        }
        List<Long> codeIds = new ArrayList<>();
        Map<Long, Long> map = new HashMap<>();
        List<OrCodeBind> orCodeBinds = new ArrayList<>();
        OrCodeParam orCodeParam = null;
        List<OrCodeBind> codeBinds = ids.size() == 0 ? new ArrayList<>() : orCodeBindService.query().in("form_id", ids).in("source", source).list();
        //已有绑定
        if (ToolUtil.isNotEmpty(codeBinds)) {
            for (OrCodeBind codeBind : codeBinds) {
                map.put(codeBind.getFormId(), codeBind.getOrCodeId());
            }
        }
        for (Long id : ids) {
            Long aLong = map.get(id);
            if (ToolUtil.isNotEmpty(aLong)) {
                codeIds.add(aLong);
            } else {
                orCodeParam = new OrCodeParam();
                orCodeParam.setType(source);
                Long codeId = this.add(orCodeParam);
                codeIds.add(codeId);
                OrCodeBind orCodeBind = new OrCodeBind();
                orCodeBind.setSource(source);
                orCodeBind.setOrCodeId(codeId);
                orCodeBind.setFormId(id);
                orCodeBinds.add(orCodeBind);
            }
        }

        orCodeBindService.saveBatch(orCodeBinds);

        return codeIds;
    }


    /**
     * 单个绑定
     *
     * @param
     * @param
     * @return
     */
    @Override
    @Transactional
    public Long backCode(BackCodeRequest codeRequest) {

        if (ToolUtil.isEmpty(codeRequest.getSource())) {
            throw new ServiceException(500, "请传入绑定类型");
        }
        OrCode code = this.query().eq("qr_code_id", codeRequest.getCodeId()).one();
        if (ToolUtil.isNotEmpty(code)) {
            code.setType(codeRequest.getSource());
            code.setState(1);
            QueryWrapper<OrCode> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("qr_code_id", code.getOrCodeId());
            this.update(code, queryWrapper);
        }
        switch (codeRequest.getSource()) {
            case "sku":
                OrCodeBindParam orCodeBindParam = new OrCodeBindParam();
                //添加绑定表
                orCodeBindParam.setSource(codeRequest.getSource());
                orCodeBindParam.setFormId(codeRequest.getId());
                if (ToolUtil.isEmpty(codeRequest.getCodeId())) {
                    OrCodeParam orCodeParam = new OrCodeParam();
                    orCodeParam.setType(codeRequest.getSource());
                    Long aLong = this.add(orCodeParam);
                    orCodeBindParam.setOrCodeId(aLong);
                    orCodeBindService.add(orCodeBindParam);
                    return aLong;
                } else {
                    OrCode qrCodeId = this.query().in("qr_code_id", codeRequest.getCodeId()).one();
                    if (ToolUtil.isEmpty(qrCodeId)) {
                        throw new ServiceException(500, "二维码不存在");
                    }
                    orCodeBindParam.setOrCodeId(codeRequest.getCodeId());
                    orCodeBindService.add(orCodeBindParam);
                    return codeRequest.getCodeId();
                }
            case "item":
                OrCode orCode = this.query().eq("qr_code_id", codeRequest.getCodeId()).one();
                if (ToolUtil.isEmpty(orCode)) {
                    throw new ServiceException(500, "二维码不合法");
                }
                //判断相同物料绑定
                Integer count = inkindService.query()
                        .eq("sku_id", codeRequest.getId()).eq("brand_id", codeRequest.getBrandId()).eq("instock_order_id", codeRequest.getInstockOrderId())
                        .eq("type", 0).eq("source", "入库").count();
                if (count > 0) {
                    throw new ServiceException(500, "物料已经绑定");
                }
                //判断相同二维码绑定
                OrCodeBind orCodeBind = orCodeBindService.query().in("qr_code_id", codeRequest.getCodeId()).one();
                if (ToolUtil.isNotEmpty(orCodeBind)) {
                    throw new ServiceException(500, "二维码已绑定");
                }
                InkindParam inkindParam = new InkindParam();
                inkindParam.setSkuId(codeRequest.getId());
                inkindParam.setType("0");
                inkindParam.setNumber(codeRequest.getNumber());
                inkindParam.setCostPrice(codeRequest.getCostPrice());
                inkindParam.setSellingPrice(codeRequest.getSellingPrice());
                inkindParam.setBrandId(codeRequest.getBrandId());
                inkindParam.setSourceId(codeRequest.getTaskDetailId());
                inkindParam.setSource(codeRequest.getInkindType());
                Long aLong = inkindService.add(inkindParam);
                OrCodeBindParam bindParam = new OrCodeBindParam();
                bindParam.setOrCodeId(codeRequest.getCodeId());
                bindParam.setFormId(aLong);
                bindParam.setSource(codeRequest.getSource());
                orCodeBindService.add(bindParam);
                return codeRequest.getCodeId();

            default:
                OrCodeBind one = orCodeBindService.query().in("form_id", codeRequest.getId()).in("source", codeRequest.getSource()).one();
                if (ToolUtil.isNotEmpty(one)) {
                    return one.getOrCodeId();
                } else {
                    OrCodeParam orCodeParam = new OrCodeParam();
                    orCodeParam.setState(1);
                    orCodeParam.setType(codeRequest.getSource());
                    Long Long = this.add(orCodeParam);
                    OrCodeBindParam BindParam = new OrCodeBindParam();
                    BindParam.setSource(codeRequest.getSource());
                    BindParam.setFormId(codeRequest.getId());
                    BindParam.setOrCodeId(Long);
                    orCodeBindService.add(BindParam);
                    return Long;
                }

        }

    }

    @Override
    public Boolean isNotBind(InKindRequest inKindRequest) {
        if (ToolUtil.isNotEmpty(inKindRequest.getCodeId())) {
            OrCodeBind orCodeBind = orCodeBindService.query().eq("qr_code_id", inKindRequest.getCodeId()).one();
            if (ToolUtil.isNotEmpty(orCodeBind)) {
                return true;
            }
        }
//        if (ToolUtil.isNotEmpty(inKindRequest.getSkuId())) {
//            Inkind inkind = inkindService.query().eq("sku_id", inKindRequest.getSkuId()).eq("type", inKindRequest.getType())
//                    .eq("spu_id", inKindRequest.getSpuId()).one();
//            if (ToolUtil.isEmpty(inkind)) {
//                return false;
//            }
//        }
        return false;
    }

    //判断是否入库
    @Override
    public Boolean judgeBind(InKindRequest inKindRequest) {
        OrCodeBind orCodeBind = orCodeBindService.query().eq("qr_code_id", inKindRequest.getCodeId()).one();
        if (ToolUtil.isNotEmpty(orCodeBind) && orCodeBind.getSource().equals("item")) {
            Inkind one = inkindService.query().eq("inkind_id", orCodeBind.getFormId()).eq("sku_id", inKindRequest.getId())
                    .eq("brand_id", inKindRequest.getBrandId())
                    .one();
            if (ToolUtil.isEmpty(one)) {
                return false;
            }
            if (one.getType().equals("0")) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * 扫码入库
     *
     * @param inKindRequest
     * @return
     */
    @Override
    @Transactional
    public Long instockByCode(InKindRequest inKindRequest) {
        OrCodeBind orCodeBind = orCodeBindService.query().eq("qr_code_id", inKindRequest.getCodeId()).eq("source", inKindRequest.getType()).one();
        InstockList instockList = null;
        Long number = 0L;
        if (ToolUtil.isNotEmpty(orCodeBind)) {
            Inkind one = inkindService.query().eq("inkind_id", orCodeBind.getFormId()).one();
            number = one.getNumber();
            if (one.getType().equals("1")) {
                throw new ServiceException(500, "已入库");
            }
            one.setType("1");
            Inkind inkind = new Inkind();
            inkind.setType("1");
            QueryWrapper<Inkind> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("inkind_id", one.getInkindId());
            inkindService.update(inkind, queryWrapper);
            inKindRequest.getInstockListParam().setNum(one.getNumber());
            if (ToolUtil.isNotEmpty(inKindRequest.getInstockListParam())) {
                instockList = instockListService.query().eq("instock_list_id", inKindRequest.getInstockListParam().getInstockListId()).one();
                if (ToolUtil.isNotEmpty(instockList)) {
                    if ((instockList.getNumber() - one.getNumber()) == 0) {
                        try {
                            instockListService.update(inKindRequest.getInstockListParam());
                        } catch (Exception e) {
                            return 0L;
                        }
                        return 0L;
                    }
                }
                try {
                    instockListService.update(inKindRequest.getInstockListParam());
                } catch (Exception e) {
                    return 0L;
                }
            }
        }
        if (ToolUtil.isEmpty(instockList)) {
            return 0L;
        } else {
            return instockList.getNumber() - number;
        }
    }

    @Override
    public void batchAdd(OrCodeParam param) {
        if (param.getAddSize() > 1000) {
            throw new ServiceException(500, "最大只可以生成1000个二维码");
        }
        List<OrCode> orCodes = new ArrayList<>();
        for (Integer i = 0; i < param.getAddSize(); i++) {
            OrCode orCode = new OrCode();
            orCode.setState(0);
            orCodes.add(orCode);
        }
        this.saveBatch(orCodes);
    }


    private Serializable getKey(OrCodeParam param) {
        return param.getOrCodeId();
    }

    private Page<OrCodeResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private OrCode getOldEntity(OrCodeParam param) {
        return this.getById(getKey(param));
    }

    private OrCode getEntity(OrCodeParam param) {
        OrCode entity = new OrCode();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public Object orcodeBackObj(Long id) {
        OrCodeBind codeBind = orCodeBindService.query().in("qr_code_id", id).one();
        if (ToolUtil.isEmpty(codeBind)) {
            return null;
        } else {
            String source = codeBind.getSource();
            switch (source) {
                case "storehouse":
                    Storehouse storehouse = storehouseService.query().eq("storehouse_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(storehouse)) {
                        return null;
                    }
                    StorehouseResult storehouseResult = new StorehouseResult();
                    ToolUtil.copyProperties(storehouse, storehouseResult);
                    try {
                        storehouseFormat(storehouseResult);
                    } catch (Exception e) {
                    }
                    StoreHouseRequest storeHouseRequest = new StoreHouseRequest();
                    storeHouseRequest.setType("storehouse");
                    storeHouseRequest.setResult(storehouseResult);
                    return storeHouseRequest;

                case "storehousePositions":
                    StorehousePositions storehousePositions = storehousePositionsService.query().in("storehouse_positions_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(storehousePositions)) {
                        return null;
                    }
                    StorehousePositionsResult storehousePositionsResult = new StorehousePositionsResult();
                    ToolUtil.copyProperties(storehousePositions, storehousePositionsResult);
                    try {
                        storehousePositionsFormat(storehousePositionsResult);
                    } catch (Exception e) {
                    }
                    StoreHousePositionsRequest storeHousePositionsRequest = new StoreHousePositionsRequest();
                    storeHousePositionsRequest.setType("storehousePositions");
                    storeHousePositionsRequest.setResult(storehousePositionsResult);
                    return storeHousePositionsRequest;

                case "stock":
                    Stock stock = stockService.query().eq("stock_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(stock)) {
                        return null;
                    }
                    StockResult stockResult = new StockResult();
                    ToolUtil.copyProperties(stock, stockResult);
                    try {
                        stockFormat(stockResult);
                    } catch (Exception e) {
                    }
                    StockRequest stockRequest = new StockRequest();
                    stockRequest.setType("storehouse");
                    stockRequest.setResult(stockResult);
                    return stockRequest;

                case "instock":
                    InstockOrder instockOrder = instockOrderService.query().eq("instock_order_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(instockOrder)) {
                        return null;
                    }
                    InstockOrderResult instockOrderResult = new InstockOrderResult();
                    ToolUtil.copyProperties(instockOrder, instockOrderResult);
                    Storehouse storehouseDetail = storehouseService.getById(instockOrder.getStoreHouseId());
                    if (ToolUtil.isNotEmpty(storehouseDetail)) {
                        StorehouseResult storehouseResult1 = new StorehouseResult();
                        ToolUtil.copyProperties(storehouseDetail, storehouseResult1);
                        instockOrderResult.setStorehouseResult(storehouseResult1);
                    }
                    User user = userService.getById(instockOrder.getUserId());
                    if (ToolUtil.isNotEmpty(user)) {
                        UserResult userResult = new UserResult();
                        ToolUtil.copyProperties(user, userResult);
                        instockOrderResult.setUserResult(userResult);
                    }
                    InstockListParam instockListParam = new InstockListParam();
                    instockListParam.setInstockOrderId(instockOrder.getInstockOrderId());
                    PageInfo<InstockListResult> instockListResultPageInfo = instockListService.findPageBySpec(instockListParam);
                    List<InstockListResult> instockListResults = instockListResultPageInfo.getData();

                    instockOrderResult.setInstockListResults(instockListResults);

                    InstockParam instockParam = new InstockParam();
                    instockParam.setInstockOrderId(instockOrder.getInstockOrderId());
                    PageInfo<InstockResult> instockResultPageInfo = instockService.findPageBySpec(instockParam, null);
                    List<InstockResult> instockResults = instockResultPageInfo.getData();

                    instockOrderResult.setInstockResults(instockResults);

                    cn.atsoft.dasheng.orCode.model.result.InstockRequest instockRequest = new InstockRequest();
                    instockRequest.setType("instock");
                    instockRequest.setResult(instockOrderResult);
                    return instockRequest;

                case "item":
                    Inkind inkind = inkindService.query().eq("inkind_id", codeBind.getFormId()).one();
                    if (ToolUtil.isEmpty(inkind)) {
                        return null;
                    }
                    List<BackSku> backSkus = skuService.backSku(inkind.getSkuId());
                    OrcodeBackItem orcodeBackItem = new OrcodeBackItem();
                    Sku sku = skuService.query().eq("sku_id", inkind.getSkuId()).one();
                    SpuResult backSpu = skuService.backSpu(inkind.getSkuId());
                    if (ToolUtil.isNotEmpty(sku)) {
                        orcodeBackItem.setSkuName(sku.getSkuName());
                    }
                    orcodeBackItem.setBackSkus(backSkus);
                    orcodeBackItem.setBackSpu(backSpu);
                    ItemRequest itemRequest = new ItemRequest();
                    itemRequest.setType("item");
                    itemRequest.setOrcodeBackItem(orcodeBackItem);
                    itemRequest.setInKindNumber(inkind.getNumber());
                    return itemRequest;
                case "quality":
                    QualityTask qualityTask = qualityTaskService.query().eq("quality_task_id", codeBind.getFormId()).one();
                    QualityTaskResult qualityTaskResult = new QualityTaskResult();
                    ToolUtil.copyProperties(qualityTask, qualityTaskResult);
                    QualityRequest qualityRequest = new QualityRequest();
                    qualityRequest.setType("quality");
                    qualityRequest.setResult(qualityTaskResult);
                    return qualityRequest;
            }
        }
        return null;
    }

    /**
     * 扫码判断物品
     *
     * @param inKindRequest
     * @return
     */
    @Override
    public Object backInkindByCode(InKindRequest inKindRequest) {
        Long codeId = inKindRequest.getCodeId();
        OrCodeBind codeBind = orCodeBindService.query().eq("qr_code_id", codeId).one();
        if (ToolUtil.isEmpty(codeBind)) {
            throw new ServiceException(500, "请扫正确二维码");
        }
        BackObject object = new BackObject();
        switch (codeBind.getSource()) {
            case "item":
                InkindResult inkindResult = inkindService.backInKindgetById(codeBind.getFormId());
                if (inkindResult.getNumber() == 0) {
                    throw new ServiceException(500, "已出库，请勿重复扫描");
                }
                if (ToolUtil.isEmpty(inkindResult)) {
                    throw new ServiceException(500, "没有此物料");
                }


                if (inkindResult.getSkuId().equals(inKindRequest.getId()) && inkindResult.getBrandId().equals(inKindRequest.getBrandId())) {
                    if (ToolUtil.isNotEmpty(inkindResult.getStorehousePositionsId())) {
                        StorehousePositions storehousePositions = storehousePositionsService.query()
                                .eq("storehouse_positions_id", inkindResult.getStorehousePositionsId()).one();
                        object.setPositions(storehousePositions);
                    }
                    object.setInkind(inkindResult);
                    return object;
                }

        }
        throw new ServiceException(500, "请扫正确物料二维码");
    }

    //扫码出库
    @Override
    @Transactional
    public Long outStockByCode(InKindRequest inKindRequest) {
        //修改库存详情
        StockDetails stockDetails = stockDetailsService.query().eq("storehouse_id", inKindRequest.getStorehouse()).eq("qr_code_id", inKindRequest.getCodeId()).one();
        if (stockDetails.getNumber() == 0) {
            throw new ServiceException(500, "数量不足");
        }
        long l = stockDetails.getNumber() - inKindRequest.getNumber();
        stockDetails.setNumber(l);
        QueryWrapper<StockDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stock_item_id", stockDetails.getStockItemId());
        stockDetailsService.update(stockDetails, queryWrapper);

        //修改出库清单
        OutstockListing outstockListing = outstockListingService.query().eq("outstock_listing_id", inKindRequest.getOutstockListingId()).one();
        Long ListingNumber = outstockListing.getNumber();
        if (ListingNumber == 0) {
            throw new ServiceException(500, "数量不足");
        }
        if (ListingNumber < inKindRequest.getNumber()) {
            throw new ServiceException(500, "数量不符");
        }

        long listNumber = outstockListing.getNumber() - inKindRequest.getNumber();
        outstockListing.setNumber(listNumber);
        QueryWrapper<OutstockListing> listingQueryWrapper = new QueryWrapper<>();
        listingQueryWrapper.eq("outstock_listing_id", inKindRequest.getOutstockListingId());
        outstockListingService.update(outstockListing, listingQueryWrapper);

        //修改库存
        Stock stock = stockService.query().eq("stock_id", stockDetails.getStockId()).one();
        if (stock.getInventory() == 0) {
            throw new ServiceException(500, "数量不足");
        }
        if (stock.getInventory() < inKindRequest.getNumber()) {
            throw new ServiceException(500, "数量不符");
        }
        long newNumber = stock.getInventory() - inKindRequest.getNumber();
        stock.setInventory(newNumber);
        QueryWrapper<Stock> stockQueryWrapper = new QueryWrapper<>();
        stockQueryWrapper.eq("stock_id", stock.getStockId());
        stockService.update(stock, stockQueryWrapper);

        //修改实物
        OrCodeBind orCodeBind = orCodeBindService.query().eq("qr_code_id", inKindRequest.getCodeId()).one();
        Inkind inkind = inkindService.query().eq("inkind_id", orCodeBind.getFormId()).one();
        if (inkind.getNumber() == 0) {
            throw new ServiceException(500, "数量不足");
        }
        if (inkind.getNumber() < inKindRequest.getNumber()) {
            throw new ServiceException(500, "数量不符");
        }
        long inkindNumber = inkind.getNumber() - inKindRequest.getNumber();
        inkind.setNumber(inkindNumber);
        QueryWrapper<Inkind> inkindQueryWrapper = new QueryWrapper<>();
        inkindQueryWrapper.eq("inkind_id", inkind.getInkindId());
        inkindService.update(inkind, inkindQueryWrapper);

        //新建出库的实物
        if (inkind.getNumber() > 0) {
            Inkind newinKind = new Inkind();
            newinKind.setNumber(inKindRequest.getNumber());
            newinKind.setSkuId(inkind.getSkuId());
            newinKind.setSource("出库");
            newinKind.setBrandId(inkind.getBrandId());
            inkindService.save(newinKind);
        }
        //增加出库详情
        Outstock outstock = new Outstock();
        outstock.setStorehouseId(inKindRequest.getStorehouse());
        outstock.setBrandId(outstockListing.getBrandId());
        outstock.setOutstockOrderId(inKindRequest.getOutstockOrderId());
        outstock.setStockId(stock.getStockId());
        outstock.setStockItemId(stockDetails.getStockItemId());
        outstock.setSkuId(stockDetails.getSkuId());
        outstock.setNumber(inKindRequest.getNumber());
        outstockService.save(outstock);


        return listNumber;
    }

    @Override
    public Long automaticBinding(BackCodeRequest codeRequest) {
        OrCode orCode = new OrCode();
        orCode.setState(1);
        this.save(orCode);
        Long formId = 0L;
        switch (codeRequest.getSource()) {
            case "item":
                InkindParam inkindParam = new InkindParam();
                inkindParam.setSkuId(codeRequest.getId());
                inkindParam.setType("0");
                inkindParam.setNumber(codeRequest.getNumber());
                inkindParam.setCostPrice(codeRequest.getCostPrice());
                inkindParam.setSellingPrice(codeRequest.getSellingPrice());
                inkindParam.setBrandId(codeRequest.getBrandId());
                inkindParam.setSource(codeRequest.getInkindType());
                inkindParam.setSourceId(codeRequest.getTaskDetailId());
                formId = inkindService.add(inkindParam);
                break;
        }
        OrCodeBindParam bindParam = new OrCodeBindParam();
        bindParam.setOrCodeId(orCode.getOrCodeId());
        bindParam.setFormId(formId);
        bindParam.setSource(codeRequest.getSource());
        orCodeBindService.add(bindParam);
        return orCode.getOrCodeId();
    }

    @Override
    public List<Long> codeIdList(String codeId) {
        List<Long> qrCodeList = this.baseMapper.qrCodeList(codeId);
        return qrCodeList;
    }
}






