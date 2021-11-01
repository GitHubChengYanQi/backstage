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
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Category;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.SpuClassification;
import cn.atsoft.dasheng.erp.entity.StorehousePositions;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.model.result.CategoryResult;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.model.response.ResponseData;
import cn.atsoft.dasheng.orCode.entity.OrCode;
import cn.atsoft.dasheng.orCode.entity.OrCodeBind;
import cn.atsoft.dasheng.orCode.mapper.OrCodeMapper;
import cn.atsoft.dasheng.orCode.model.params.OrCodeBindParam;
import cn.atsoft.dasheng.orCode.model.params.OrCodeParam;
import cn.atsoft.dasheng.orCode.model.result.OrCodeResult;
import cn.atsoft.dasheng.orCode.service.OrCodeBindService;
import cn.atsoft.dasheng.orCode.service.OrCodeService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    @Transactional
    public Long add(OrCodeParam param) {
        OrCode entity = getEntity(param);
        this.save(entity);
        return entity.getOrCodeId();
    }

    @Override
    public void delete(OrCodeParam param) {
//        this.removeById(getKey(param));
        throw new ServiceException(500, "不可以删除");
    }

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

    @Override
    public List<Long> backBatchCode(List<Long> ids, String source) {
        List<Long> codeIds = new ArrayList<>();
        List<Long> formIds = new ArrayList<>();
        List<OrCodeBind> orCodeBinds = new ArrayList<>();
        OrCodeParam orCodeParam = null;
        List<OrCodeBind> codeBinds = ids.size() == 0 ? new ArrayList<>() : orCodeBindService.query().in("form_id", ids).in("source", source).list();

        if (ToolUtil.isNotEmpty(codeBinds)) {
            for (OrCodeBind codeBind : codeBinds) {
                codeIds.add(codeBind.getOrCodeId());
                formIds.add(codeBind.getFormId());
            }
        }
        if (formIds.size() != 0) {
            for (Long formId : formIds) {
                for (Long id : ids) {
                    if (formId != id) {
                        orCodeParam = new OrCodeParam();
                        Long aLong = this.add(orCodeParam);
                        codeIds.add(aLong);
                        OrCodeBind orCodeBind = new OrCodeBind();
                        orCodeBind.setSource(source);
                        orCodeBind.setFormId(id);
                        orCodeBind.setOrCodeId(aLong);
                        orCodeBinds.add(orCodeBind);
                    }
                }
            }
        } else {
            for (Long id : ids) {
                orCodeParam = new OrCodeParam();
                Long aLong = this.add(orCodeParam);
                codeIds.add(aLong);
                OrCodeBind orCodeBind = new OrCodeBind();
                orCodeBind.setSource(source);
                orCodeBind.setFormId(id);
                orCodeBind.setOrCodeId(aLong);
                orCodeBinds.add(orCodeBind);

            }
        }

        orCodeBindService.saveBatch(orCodeBinds);

        return codeIds;
    }

    @Override
    public Long backCode(Long id, String source) {
        OrCodeBind one = orCodeBindService.query().in("form_id", id).in("source", source).one();
        if (ToolUtil.isNotEmpty(one)) {
            return one.getOrCodeId();
        } else {
            OrCodeParam orCodeParam = new OrCodeParam();
            Long aLong = this.add(orCodeParam);
            OrCodeBindParam orCodeBindParam = new OrCodeBindParam();
            orCodeBindParam.setSource(source);
            orCodeBindParam.setFormId(id);
            orCodeBindParam.setOrCodeId(aLong);
            orCodeBindService.add(orCodeBindParam);
            return aLong;
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
