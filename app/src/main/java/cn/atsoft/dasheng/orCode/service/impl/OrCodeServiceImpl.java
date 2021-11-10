package cn.atsoft.dasheng.orCode.service.impl;


import cn.atsoft.dasheng.app.entity.Brand;
import cn.atsoft.dasheng.app.entity.Material;
import cn.atsoft.dasheng.app.entity.Storehouse;
import cn.atsoft.dasheng.app.entity.Unit;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.StockResult;
import cn.atsoft.dasheng.app.model.result.StorehouseResult;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.model.params.InkindParam;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.mapper.OrCodeMapper;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.model.result.BackCodeRequest;
import cn.atsoft.dasheng.orCode.model.result.InKindRequest;
import cn.atsoft.dasheng.orCode.model.result.OrCodeResult;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
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
        return PageFactory.createPageInfo(page);
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
            cn.atsoft.dasheng.erp.model.result.CategoryResult categoryResult = new CategoryResult();
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
                OrCodeBind orCodeBind = orCodeBindService.query().in("qr_code_id", codeRequest.getCodeId()).one();
                if (ToolUtil.isNotEmpty(orCodeBind)) {
                    throw new ServiceException(500, "二维码已绑定");
                }
                InkindParam inkindParam = new InkindParam();
                inkindParam.setSkuId(codeRequest.getId());
                inkindParam.setType("0");
                inkindParam.setCostPrice(codeRequest.getCostPrice());
                inkindParam.setInstockOrderId(codeRequest.getInstockOrderId());
                inkindParam.setSellingPrice(codeRequest.getSellingPrice());
                inkindParam.setBrandId(codeRequest.getBrandId());
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
            Inkind one = inkindService.query().eq("inkind_id", orCodeBind.getFormId()).eq("sku_id", inKindRequest.getId()).one();
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
     */
    @Override
    @Transactional
    public void instockByCode(InKindRequest inKindRequest) {
        OrCodeBind orCodeBind = orCodeBindService.query().eq("qr_code_id", inKindRequest.getCodeId()).eq("source", inKindRequest.getType()).one();
        if (ToolUtil.isNotEmpty(orCodeBind)) {
            Inkind one = inkindService.query().eq("inkind_id", orCodeBind.getFormId()).one();
            if (one.getType().equals("1")) {
                throw new ServiceException(500, "已入库");
            }
            one.setType("1");
            Inkind inkind = new Inkind();
            inkind.setType("1");
            QueryWrapper<Inkind> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("inkind_id", one.getInkindId());
            inkindService.update(inkind, queryWrapper);
            if (ToolUtil.isNotEmpty(inKindRequest.getInstockListParam())) {
                instockListService.update(inKindRequest.getInstockListParam());
            }
        }
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

}
