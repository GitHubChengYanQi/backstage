package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.*;
import cn.atsoft.dasheng.app.model.params.Attribute;
import cn.atsoft.dasheng.app.model.params.PartsParam;
import cn.atsoft.dasheng.app.model.params.Values;
import cn.atsoft.dasheng.app.model.result.BrandResult;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.model.result.UnitResult;
import cn.atsoft.dasheng.app.service.*;
import cn.atsoft.dasheng.appBase.model.result.MediaUrlResult;
import cn.atsoft.dasheng.appBase.service.MediaService;
import cn.atsoft.dasheng.base.log.BussinessLog;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Supply;
import cn.atsoft.dasheng.crm.service.SupplyService;
import cn.atsoft.dasheng.erp.enums.SearchTypeEnum;
import cn.atsoft.dasheng.erp.entity.*;
import cn.atsoft.dasheng.erp.mapper.SkuMapper;
import cn.atsoft.dasheng.erp.model.params.*;
import cn.atsoft.dasheng.erp.model.request.SkuAttributeAndValue;
import cn.atsoft.dasheng.erp.model.result.*;
import cn.atsoft.dasheng.erp.pojo.*;
import cn.atsoft.dasheng.erp.service.*;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.form.entity.ActivitiProcess;
import cn.atsoft.dasheng.form.model.params.ActivitiProcessParam;
import cn.atsoft.dasheng.form.model.params.StepsParam;
import cn.atsoft.dasheng.form.model.result.ActivitiProcessResult;
import cn.atsoft.dasheng.form.model.result.ActivitiStepsResult;
import cn.atsoft.dasheng.form.service.ActivitiProcessService;
import cn.atsoft.dasheng.form.service.RemarksService;
import cn.atsoft.dasheng.form.service.StepsService;
import cn.atsoft.dasheng.message.producer.MessageProducer;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.production.service.ProductionPickListsCartService;
import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.service.PurchaseListingService;
import cn.atsoft.dasheng.query.service.QueryLogService;
import cn.atsoft.dasheng.sys.modular.system.entity.Dict;
import cn.atsoft.dasheng.sys.modular.system.entity.User;
import cn.atsoft.dasheng.sys.modular.system.service.DictService;
import cn.atsoft.dasheng.sys.modular.system.service.UserService;
import cn.atsoft.dasheng.view.entity.SkuBasisView;
import cn.atsoft.dasheng.view.entity.SkuPositionView;
import cn.atsoft.dasheng.view.entity.SkuSupplyView;
import cn.atsoft.dasheng.view.service.SkuBasisViewService;
import cn.atsoft.dasheng.view.service.SkuPositionViewService;
import cn.atsoft.dasheng.view.service.SkuSupplyViewService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * sku表	 服务实现类
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {
    @Autowired
    private SpuService spuService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttributeValuesService attributeValuesService;
    @Autowired
    private ItemAttributeService itemAttributeService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private PartsService partsService;
    @Autowired
    private ErpPartsDetailService partsDetailService;
    @Autowired
    private CodingRulesService codingRulesService;
    @Autowired
    private SpuClassificationService spuClassificationService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private UserService userService;
    @Autowired
    private SkuBrandBindService skuBrandBindService;
    @Autowired
    private StockDetailsService stockDetailsService;
    @Autowired
    private MessageProducer messageProducer;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ActivitiProcessService processService;
    @Autowired
    private StepsService stepsService;
    @Autowired
    private SupplyService supplyService;
    @Autowired
    private PurchaseListingService purchaseListingService;
    @Autowired
    private StorehousePositionsService positionsService;
    @Autowired
    private StorehousePositionsBindService positionsBindService;
    @Autowired
    private SkuBasisViewService skuBasisViewService;
    @Autowired
    private SkuSupplyViewService skuSupplyViewService;
    @Autowired
    private SkuPositionViewService skuPositionViewService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private QueryLogService queryLogService;
    @Autowired
    private ProductionPickListsCartService pickListsCartService;
    @Autowired
    private MaintenanceCycleService maintenanceCycleService;
    @Autowired
    private InkindService inkindService;
    @Autowired
    private RemarksService remarksService;
    @Autowired
    private DictService dictService;


    @Transactional
    @Override
    public Map<String, Sku> add(SkuParam param) {

        Sku result = new Sku();
        Long skuId = null;

        /**
         * type=1 是整机添加
         */
        if (param.getType() == 0) {
            /**
             * 查询分类  添加分类
             */


//            Long spuClassificationId = this.getOrSaveSpuClass(param);
            SpuClassification spuClassification = spuClassificationService.getById(param.getSpuClass());
            Integer parentSpuClassifications = spuClassificationService.query().eq("pid", spuClassification.getSpuClassificationId()).eq("display", 1).count();
            if (parentSpuClassifications > 0) {
                throw new ServiceException(500, "物料必须添加在最底级分类中");
            }
            Long spuClassificationId = spuClassification.getSpuClassificationId();
            /**
             * sku名称（skuName）加型号(spuName)判断防止重复
             */

            Category category = this.getOrSaveCategory(param);
            Long categoryId = category.getCategoryId();
            Spu spu = this.getOrSaveSpu(param, spuClassificationId, categoryId);
            //同分类，同产品下不可有同名物料
            Sku sku = this.throwDuplicate(param, spu.getSpuId());
            if (ToolUtil.isNotEmpty(sku)) {
                {
                    return new HashMap<String, Sku>() {{
                        put("error", sku);
                    }};
                }
            }


            //生成编码
            if (ToolUtil.isEmpty(param.getStandard())) {
                CodingRules codingRules = codingRulesService.query().eq("module", "0").eq("state", 1).one();
                if (ToolUtil.isNotEmpty(codingRules)) {
                    String backCoding = codingRulesService.backCoding(codingRules.getCodingRulesId(), spu.getSpuId());
//                SpuClassification classification = spuClassificationService.query().eq("spu_classification_id", spuClassificationId).one();
                    SpuClassification classification = spuClassificationService.query().eq("spu_classification_id", param.getSpuClass()).one();
                    if (ToolUtil.isNotEmpty(classification) && classification.getDisplay() != 0) {
                        String replace = "";
                        if (ToolUtil.isNotEmpty(classification.getCodingClass())) {
                            replace = backCoding.replace("${skuClass}", classification.getCodingClass());
                        } else {
                            replace = backCoding.replace("${skuClass}", "");
                        }

                        param.setStandard(replace);
                        param.setCoding(replace);

                    }
                } else {
                    throw new ServiceException(500, "当前无此规则");
                }
            }
            /**
             * 判断成品码是否重复
             */
            int count = skuService.count(new QueryWrapper<Sku>() {{
                eq("standard", param.getStandard());
                eq("display", 1);
            }});
            if (count > 0) {
                throw new ServiceException(500, "编码/成品码重复");
            }


            /**
             * 查询产品，添加产品 在上方spu查询
             */
            Long spuId = spu.getSpuId();

            /**
             * 做匹配保存 属性属性值方法
             *
             */
            List<AttributeValues> list = this.addAttributeAndValue(param.getSku(), categoryId);

            Sku entity = getEntity(param);

            list.sort(Comparator.comparing(AttributeValues::getAttributeId));
            String json = JSON.toJSONString(list);

            entity.setSpuId(spuId);
            entity.setSkuValue(json);

            String md5 = SecureUtil.md5(entity.getSkuValue() + entity.getSpuId().toString() + entity.getSkuName() + spuClassificationId);


            entity.setSkuValueMd5(md5);


            this.save(entity);
            skuId = entity.getSkuId();
            ToolUtil.copyProperties(entity, result);
            /**
             * 绑定品牌（多个）
             */
            if (ToolUtil.isNotEmpty(param.getBrandIds())) {
                skuBrandBindService.addBatch(new SkuBrandBindParam() {{
                    setBrandIds(param.getBrandIds());
                    setSkuId(entity.getSkuId());
                }});
            }

        } else if (param.getType() == 1) {
            Long spuId = param.getSpu().getSpuId();
            if (ToolUtil.isEmpty(spuId)) {
                spuId = spuService.lambdaQuery().eq(Spu::getName, param.getSpu().getName()).and(i -> i.eq(Spu::getDisplay, 1)).one().getSpuId();
            }

            if (spuId == null) {
                SpuParam spu = new SpuParam();
                spu.setName(param.getSpu().getName());
                spu.setSpuClassificationId(param.getSpuClassificationId());
                spu.setIsHidden(false);
                spu.setType(0);
                spuId = spuService.add(spu);
            } else {
                param.getSpuAttributes().getSpuRequests().sort(Comparator.comparing(Attribute::getAttributeId));
                List<AttributeValues> list = new ArrayList<>();
                for (Attribute spuRequest : param.getSpuAttributes().getSpuRequests()) {
                    AttributeValues attributeValueResult = new AttributeValues();
                    attributeValueResult.setAttributeId(Long.valueOf(spuRequest.getAttributeId()));
                    for (Values attributeValue : spuRequest.getAttributeValues()) {
                        attributeValueResult.setAttributeValuesId(Long.valueOf(attributeValue.getAttributeValuesId()));
                    }
                    list.add(attributeValueResult);
                }
                Sku entity = getEntity(param);
                entity.setSpuId(spuId);
                String json = JSON.toJSONString(list);
                entity.setSkuValue(json);
//                entity.setSkuValue(spuId + "," + json);
                String md5 = SecureUtil.md5(entity.getSkuValue() + entity.getSpuId().toString() + entity.getSkuName() + param.getSpuClassificationId());
                entity.setSkuValueMd5(md5);
                /**
                 * //原 SKU防重复判断
                 *
                 * Sku sku = skuService.lambdaQuery().eq(Sku::getSkuValueMd5, md5).and(i -> i.eq(Sku::getDisplay, 1)).one();
                 *  if (ToolUtil.isNotEmpty(sku) || ToolUtil.isNotEmpty(spu)) {
                 *    throw new ServiceException(500, "此物料在产品中已存在");
                 *} else {
                 *    this.save(entity);
                 *}
                 *
                 *  ↓为新sku防止重复判断  以名称加型号 做数据库比对判断
                 */
//                List<Spu> spu = spuService.query().eq("name", param.getSpu().getName()).and(i -> i.eq("display", 1)).list();
//                List<Sku> skuName = skuService.query().eq("sku_name", param.getSkuName()).and(i -> i.eq("display", 1)).list();
//                if (ToolUtil.isNotEmpty(spu) && ToolUtil.isNotEmpty(skuName)) {
//                    throw new ServiceException(500, "此物料在产品中已存在");
//                } else {
                this.save(entity);
                if (ToolUtil.isNotEmpty(param.getMaintenancePeriod())) {
                    MaintenanceCycle maintenanceCycle = new MaintenanceCycle();
                    maintenanceCycle.setSkuId(entity.getSkuId());
                    maintenanceCycle.setMaintenancePeriod(param.getMaintenancePeriod());
                    maintenanceCycleService.save(maintenanceCycle);
                }
                skuId = entity.getSkuId();
                ToolUtil.copyProperties(entity, result);
                /**
                 * 绑定品牌（多个）
                 */
                if (ToolUtil.isNotEmpty(param.getBrandIds())) {
                    skuBrandBindService.addBatch(new SkuBrandBindParam() {{
                        setBrandIds(param.getBrandIds());
                        setSkuId(entity.getSkuId());
                    }});
                }
//                }
            }

        }
        if (ToolUtil.isNotEmpty(param.getOldSkuId())) {
            ActivitiProcess activitiProcess = processService.query().eq("form_id", param.getOldSkuId()).eq("type", "ship").eq("display", 1).one();
            if (ToolUtil.isNotEmpty(activitiProcess)) {
                copyProcessRoute(param.getOldSkuId(), skuId);
            } else {
                copySkuBomById(param.getOldSkuId(), skuId);
            }
        }
        return new HashMap<String, Sku>() {{
            put("success", result);
        }};
    }

    private void copySkuBomById(Long oldSkuId, Long newSkuId) {
        Parts parts = partsService.query().eq("sku_id", oldSkuId).eq("display", 1).eq("status", 99).one();
        if (ToolUtil.isNotEmpty(parts)) {
            List<ErpPartsDetail> partsDetails = partsDetailService.query().eq("parts_id", parts.getPartsId()).list();
            Parts newSkuParts = new Parts();
            ToolUtil.copyProperties(parts, newSkuParts);
            newSkuParts.setPartsId(null);
            newSkuParts.setSkuId(newSkuId);
            partsService.save(newSkuParts);
            List<ErpPartsDetail> newSkuPartsDetails = new ArrayList<>();
            for (ErpPartsDetail partsDetail : partsDetails) {
                ErpPartsDetail newSkuPartsDetail = new ErpPartsDetail();
                ToolUtil.copyProperties(partsDetail, newSkuPartsDetail);
                newSkuPartsDetail.setPartsDetailId(null);
                newSkuPartsDetail.setPartsId(newSkuParts.getPartsId());
                newSkuPartsDetails.add(newSkuPartsDetail);
            }
            partsDetailService.saveBatch(newSkuPartsDetails);
        }
    }

    private void copyProcessRoute(Long oldSkuId, Long newSkuId) {
        ActivitiProcess activitiProcess = processService.query().eq("type", "ship").eq("form_id", oldSkuId).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(activitiProcess)) {
            ActivitiStepsResult activitiStepsResult = stepsService.detail(activitiProcess.getProcessId());
            StepsParam param = new StepsParam();
            ToolUtil.copyProperties(activitiStepsResult, param);
            System.out.println(param);
            param.setProcess(new ActivitiProcessParam() {{
                setSkuId(newSkuId);
            }});
            stepsService.add(param);

        }
    }

    @Override
    public void editEnclosure(SkuParam param) {
        if (ToolUtil.isEmpty(param.getSkuId())) {
            throw new ServiceException(500, "请传入物料id");
        }
        Long skuId = param.getSkuId();
        Sku sku = this.getById(skuId);
        String enclosure = ToolUtil.isNotEmpty(param.getEnclosureIds()) ? param.getEnclosure() : null;
        String drawing = ToolUtil.isNotEmpty(param.getDrawing()) ? param.getDrawing() : null;
        String images = ToolUtil.isNotEmpty(param.getImages()) ? param.getImages() : null;
        String filedId = ToolUtil.isNotEmpty(param.getFileId()) ? param.getFileId() : null;
        sku.setDrawing(drawing);
        sku.setImages(images);
        sku.setEnclosure(enclosure);
        sku.setFileId(filedId);
        remarksService.addDynamic("操作更改了物料附件等文件信息");
        this.updateById(sku);
    }


    @Override
    public List<SkuResult> getSkuByMd5(SkuParam param) {
        if (ToolUtil.isEmpty(param.getSpuId())) {
            throw new ServiceException(500, "请传入spuId");
        }
        List<String> attributeName = new ArrayList<>();
        List<String> attributeValueName = new ArrayList<>();
        List<Long> attributeId = new ArrayList<>();
        for (SkuAttributeAndValue skuAttributeAndValue : param.getSku()) {
            attributeName.add(skuAttributeAndValue.getLabel());
            attributeValueName.add(skuAttributeAndValue.getValue());
            if (ToolUtil.isEmpty(skuAttributeAndValue.getLabel()) || skuAttributeAndValue.getLabel().replace(" ", "").length() == 0) {
                throw new ServiceException(500, "规格名称不可为空或空格");
            }
            if (ToolUtil.isEmpty(skuAttributeAndValue.getValue()) || skuAttributeAndValue.getValue().replace(" ", "").length() == 0) {
                throw new ServiceException(500, "规格值不可为空或空格");
            }
        }


        Spu spu = spuService.getById(param.getSpuId());
        Long categoryId = spu.getCategoryId();

        List<ItemAttribute> attributes = itemAttributeService.query().eq("category_id", categoryId).in("attribute", attributeName).eq("display", 1).list();
        for (ItemAttribute attribute : attributes) {
            attributeId.add(attribute.getAttributeId());
        }
        List<AttributeValues> attributeValues = attributeId.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery().in(AttributeValues::getAttributeId, attributeId).and(i -> i.isNotNull(AttributeValues::getAttributeValues)).and(i -> i.eq(AttributeValues::getDisplay, 1)).list();
        List<AttributeValues> list = new ArrayList<>();
        for (SkuAttributeAndValue skuAttributeAndValue : param.getSku()) {

            if (ToolUtil.isNotEmpty(skuAttributeAndValue)) {
                AttributeValues value = new AttributeValues();
                if (ToolUtil.isNotEmpty(skuAttributeAndValue.getLabel()) && attributes.size() > 0 && attributes.stream().anyMatch(attribute -> attribute.getAttribute().equals(skuAttributeAndValue.getLabel()))) {
                    for (ItemAttribute itemAttribute : attributes) {
                        if (skuAttributeAndValue.getLabel().equals(itemAttribute.getAttribute())) {
                            value.setAttributeId(itemAttribute.getAttributeId());
                        }
                    }
                }
                if (ToolUtil.isNotEmpty(skuAttributeAndValue.getValue()) && attributes.size() > 0 && attributeValues.stream().anyMatch(attributeValue -> attributeValue.getAttributeValues().equals(skuAttributeAndValue.getValue()))) {
                    for (AttributeValues attributeValue : attributeValues) {
                        if (skuAttributeAndValue.getValue().equals(attributeValue.getAttributeValues()) && attributeValue.getAttributeId().equals(value.getAttributeId())) {
                            value.setAttributeValuesId(attributeValue.getAttributeValuesId());
                        }
                    }
                }
                list.add(value);
            }
        }


        for (AttributeValues values : list) {
            if (ToolUtil.isEmpty(values.getAttributeId()) || ToolUtil.isEmpty(values.getAttributeValuesId())) {
                return new ArrayList<>();
            }
        }

        list.sort(Comparator.comparing(AttributeValues::getAttributeId));
        String jsonList = JSON.toJSONString(list);
        String md5 = SecureUtil.md5(jsonList + spu.getSpuId().toString() + param.getSkuName() + spu.getSpuClassificationId());


        List<Sku> skuList = this.query().in("sku_value_md5", md5).list();
        List<SkuResult> results = new ArrayList<>();
        for (Sku sku : skuList) {
            SkuResult result = new SkuResult();
            ToolUtil.copyProperties(sku, result);
            results.add(result);
        }
        return results;

    }

    @Override
    @Transactional
    public void directAdd(SkuParam param) {
        Category category = this.getOrSaveCategory(param);
        Long categoryId = category.getCategoryId();

        SpuClassification spuClassification = spuClassificationService.getById(param.getSpuClass());
        Long spuClassificationId = spuClassification.getSpuClassificationId();
        Integer parentSpuClassifications = spuClassificationService.query().eq("pid", spuClassification.getSpuClassificationId()).eq("display", 1).count();
        if (parentSpuClassifications > 0) {
            throw new ServiceException(500, "物料必须添加在最底级分类中");
        }
        /**
         * sku名称（skuName）加型号(spuName)判断防止重复
         */
        Spu spu = this.getOrSaveSpu(param, spuClassificationId, categoryId);
        Long spuId = spu.getSpuId();
        //生成编码
        if (ToolUtil.isEmpty(param.getStandard())) {
            getCoding(param, spu.getSpuId());
        }
        /**
         * 判断成品码是否重复
         */
        int count = skuService.count(new QueryWrapper<Sku>() {{
            eq("standard", param.getStandard());
            eq("display", 1);
        }});
        if (count > 0) {
            throw new ServiceException(500, "编码/成品码重复");
        }


//        List<Sku> skuName = skuService.query().eq("sku_name", param.getSkuName()).and(i -> i.eq("display", 1)).list();
//        if (ToolUtil.isNotEmpty(spu) && ToolUtil.isNotEmpty(skuName)) {
//            throw new ServiceException(500, "此物料在产品中已存在");
//        }
        /**
         * 查询产品，添加产品 在上方spu查询
         */

        List<AttributeValues> list = this.addAttributeAndValue(param.getSku(), categoryId);

        Sku entity = getEntity(param);


        String json = JSON.toJSONString(list);

        entity.setSpuId(spuId);
        entity.setSkuValue(json);

        String md5 = SecureUtil.md5(entity.getSkuValue() + entity.getSpuId().toString() + entity.getSkuName() + spuClassification.getSpuClassificationId());

        entity.setSkuValueMd5(md5);
//        if (ToolUtil.isNotEmpty(codingRules)) {
//            Integer skuCount = skuService.lambdaQuery().eq(Sku::getSkuValueMd5, md5).and(i -> i.eq(Sku::getDisplay, 1)).count();
//            if (skuCount > 0) {
//                throw new ServiceException(500, "该物料已经存在");
//            }
//        }


        this.save(entity);
        if (ToolUtil.isNotEmpty(param.getBrandIds())) {
            skuBrandBindService.addBatch(new SkuBrandBindParam() {{
                setBrandIds(param.getBrandIds());
                setSkuId(entity.getSkuId());
            }});
        }
    }


    @Transactional
    public List<AttributeValues> addAttributeAndValue(List<SkuAttributeAndValue> param, Long categoryId) {
        List<String> attributeName = new ArrayList<>();
        List<String> attributeValueName = new ArrayList<>();
        List<Long> attributeId = new ArrayList<>();
        for (SkuAttributeAndValue skuAttributeAndValue : param) {
            attributeName.add(skuAttributeAndValue.getLabel());
            attributeValueName.add(skuAttributeAndValue.getValue());
            if (ToolUtil.isEmpty(skuAttributeAndValue.getLabel()) || skuAttributeAndValue.getLabel().replace(" ", "").length() == 0) {
                throw new ServiceException(500, "规格名称不可为空或空格");
            }
            if (ToolUtil.isEmpty(skuAttributeAndValue.getValue()) || skuAttributeAndValue.getValue().replace(" ", "").length() == 0) {
                throw new ServiceException(500, "规格值不可为空或空格");
            }
        }
        List<ItemAttribute> attributes = attributeName.size() == 0 ? new ArrayList<>() : itemAttributeService.lambdaQuery().in(ItemAttribute::getAttribute, attributeName).and(i -> i.eq(ItemAttribute::getCategoryId, categoryId)).and(i -> i.isNotNull(ItemAttribute::getAttribute)).and(i -> i.eq(ItemAttribute::getDisplay, 1)).list();
        for (ItemAttribute attribute : attributes) {
            attributeId.add(attribute.getAttributeId());
        }
        List<AttributeValues> attributeValues = attributeId.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery().in(AttributeValues::getAttributeId, attributeId).and(i -> i.isNotNull(AttributeValues::getAttributeValues)).and(i -> i.eq(AttributeValues::getDisplay, 1)).list();
        List<AttributeValues> list = new ArrayList<>();
        for (SkuAttributeAndValue skuAttributeAndValue : param) {
            if (ToolUtil.isNotEmpty(skuAttributeAndValue)) {
                AttributeValues value = new AttributeValues();

                if (ToolUtil.isNotEmpty(skuAttributeAndValue.getLabel()) && attributes.size() > 0 && attributes.stream().anyMatch(attribute -> attribute.getAttribute().equals(skuAttributeAndValue.getLabel()))) {
                    for (ItemAttribute itemAttribute : attributes) {
                        if (skuAttributeAndValue.getLabel().equals(itemAttribute.getAttribute())) {
                            value.setAttributeId(itemAttribute.getAttributeId());
                        }
                    }
                } else {
                    ItemAttribute itemAttributeEntity = new ItemAttribute();
                    itemAttributeEntity.setAttribute(skuAttributeAndValue.getLabel());
                    itemAttributeEntity.setCategoryId(categoryId);
                    itemAttributeService.save(itemAttributeEntity);
                    value.setAttributeId(itemAttributeEntity.getAttributeId());

                }

                if (ToolUtil.isNotEmpty(skuAttributeAndValue.getValue()) && attributes.size() > 0 && attributeValues.stream().anyMatch(attributeValue -> attributeValue.getAttributeValues().equals(skuAttributeAndValue.getValue()) && attributeValue.getAttributeId().equals(value.getAttributeId()))) {
                    for (AttributeValues attributeValue : attributeValues) {
                        if (skuAttributeAndValue.getValue().equals(attributeValue.getAttributeValues()) && attributeValue.getAttributeId().equals(value.getAttributeId())) {
                            value.setAttributeValuesId(attributeValue.getAttributeValuesId());
                        }
                    }
                } else {
                    AttributeValues attributeValuesEntity = new AttributeValues();
                    attributeValuesEntity.setAttributeId(value.getAttributeId());
                    attributeValuesEntity.setAttributeValues(skuAttributeAndValue.getValue());
                    attributeValuesService.save(attributeValuesEntity);
                    value.setAttributeValuesId(attributeValuesEntity.getAttributeValuesId());
                }
                list.add(value);
            }
        }
        list.sort(Comparator.comparing(AttributeValues::getAttributeId));
        return list;
    }

    /**
     * 合并sku
     *
     * @param param
     */
    @Override
    public void mirageSku(SkuParam param) {


        SpuClassification spuClassification = ToolUtil.isEmpty(param.getSpuClass()) ? new SpuClassification() : spuClassificationService.getById(param.getSpuClass());
        Category category = this.getOrSaveCategory(param);
        Long categoryId = category.getCategoryId();
        Long spuClassificationId = spuClassification.getSpuClassificationId();
        Spu orSaveSpu = this.getOrSaveSpu(param, spuClassificationId, categoryId);
        if (ToolUtil.isEmpty(param.getStandard())) {
            getCoding(param, orSaveSpu.getSpuId());
        }
        List<AttributeValues> list = ToolUtil.isEmpty(param.getSku()) ? new ArrayList<>() : this.addAttributeAndValue(param.getSku(), categoryId);


        Sku oldEntity = getOldEntity(param);
        Sku newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);

        newEntity.setSpuId(orSaveSpu.getSpuId());
        String json = JSON.toJSONString(list);
        newEntity.setSkuValue(json);
        String md5 = SecureUtil.md5(newEntity.getSkuValue() + newEntity.getSpuId().toString() + newEntity.getSkuName() + spuClassification.getSpuClassificationId());
        newEntity.setSkuValueMd5(md5);
        this.updateById(newEntity);
//        /**
//         * 更新品牌
//         */
//        if (ToolUtil.isNotEmpty(param.getBrandIds())) {
//            List<SkuBrandBind> binds = skuBrandBindService.query().eq("sku_id", newEntity.getSkuId()).list();
//            for (SkuBrandBind bind : binds) {
//                bind.setDisplay(0);
//            }
//            skuBrandBindService.updateBatchById(binds);
//
//            skuBrandBindService.addBatch(new SkuBrandBindParam() {{
//                setBrandIds(param.getBrandIds());
//                setSkuId(newEntity.getSkuId());
//            }});
//        }
    }

    @Override
    public void delete(SkuParam param) {
        List<Long> id = new ArrayList<>();
        id.add(param.getSkuId());
        param.setId(id);

        this.deleteBatch(param);
    }

    @Transactional
    @Override
    public void deleteBatch(SkuParam param) {
        List<Long> skuIds = param.getId();
        Integer stockSku = skuIds.size() == 0 ? 0 : stockDetailsService.query().in("sku_id", skuIds).count();
        if (stockSku > 0) {
            throw new ServiceException(500, "库存中中有此物品数据,删除终止");

        }
        List<ErpPartsDetail> partsDetailList = skuIds.size() == 0 ? new ArrayList<>() : partsDetailService.query().in("sku_id", skuIds).eq("display", 1).list();
        List<Long> partsIds = new ArrayList<>();
        for (ErpPartsDetail erpPartsDetail : partsDetailList) {
            partsIds.add(erpPartsDetail.getPartsId());
        }
        List<Parts> parts = partsIds.size() == 0 ? new ArrayList<>() : partsService.query().in("parts_id", partsIds).eq("status", 99).eq("display", 1).list();

        List<Parts> partList = skuIds.size() == 0 ? new ArrayList<>() : partsService.lambdaQuery().in(Parts::getSkuId, skuIds).and(i -> i.eq(Parts::getDisplay, 1)).and(i -> i.eq(Parts::getStatus, 99)).list();
        partList.addAll(parts);
        partList = partList.stream().distinct().collect(Collectors.toList());
        if (ToolUtil.isNotEmpty(partList)) {
            throw new ServiceException(500, "清单中有此物品数据,删除终止");
        }
        List<Long> attributeValuesIds = new ArrayList<>();
        List<Long> attributeIds = new ArrayList<>();
        List<Long> spuIds = new ArrayList<>();
        List<Sku> skuList = skuIds.size() == 0 ? new ArrayList<>() : skuService.lambdaQuery().in(Sku::getSkuId, skuIds).and(i -> i.eq(Sku::getDisplay, 1)).list();
        for (Sku sku : skuList) {
            //获取spuId
            spuIds.add(sku.getSpuId());
            //循环设置逻辑删除值
            sku.setDisplay(0);
            JSONArray jsonArray = JSONUtil.parseArray(sku.getSkuValue());
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);//skuValue解析
            for (AttributeValues valuesRequest : valuesRequests) {
                attributeValuesIds.add(valuesRequest.getAttributeValuesId());//获取sku属性值
                attributeIds.add(valuesRequest.getAttributeId());//获取sku属性
            }
        }
        skuService.updateBatchById(skuList);
        List<AttributeValues> attributeValuesList = attributeValuesIds.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery().in(AttributeValues::getAttributeValuesId, attributeValuesIds).and(i -> i.eq(AttributeValues::getDisplay, 1)).list();
        for (AttributeValues attributeValues : attributeValuesList) {
            attributeValues.setDisplay(0);
        }
        attributeValuesService.updateBatchById(attributeValuesList);
        List<AttributeValues> afterDeleteValues = attributeValuesList.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery().in(AttributeValues::getAttributeId, attributeIds).and(i -> i.eq(AttributeValues::getDisplay, 1)).list();
        List<ItemAttribute> itemAttributes = attributeIds.size() == 0 ? new ArrayList<>() : itemAttributeService.lambdaQuery().in(ItemAttribute::getAttributeId, attributeIds).and(i -> i.eq(ItemAttribute::getDisplay, 1)).list();
        for (ItemAttribute itemAttribute : itemAttributes) {
            itemAttribute.setDisplay(0);
            for (AttributeValues afterDeleteValue : afterDeleteValues) {
                if (itemAttribute.getAttributeId().equals(afterDeleteValue.getAttributeId())) {
                    itemAttribute.setDisplay(1);
                }
            }
        }
        itemAttributeService.updateBatchById(itemAttributes);

        //获取分类id
        List<Long> categoryIds = new ArrayList<>();
        List<Spu> spuList = spuIds.size() == 0 ? new ArrayList<>() : spuService.lambdaQuery().in(Spu::getSpuId, spuIds).and(i -> i.eq(Spu::getDisplay, 1)).list();
        List<Sku> afterDeleteSkuList = spuIds.size() == 0 ? new ArrayList<>() : skuService.lambdaQuery().in(Sku::getSpuId, spuIds).and(i -> i.eq(Sku::getDisplay, 1)).list();
        for (Spu spu : spuList) {
            spu.setDisplay(0);
            for (Sku sku : afterDeleteSkuList) {
                if (sku.getSpuId().equals(spu.getSpuId())) {
                    spu.setDisplay(1);
                }
            }
            categoryIds.add(spu.getCategoryId());
        }
        spuService.updateBatchById(spuList);

        List<Category> categoryList = categoryIds.size() == 0 ? new ArrayList<>() : categoryService.lambdaQuery().in(Category::getCategoryId, categoryIds).and(i -> i.eq(Category::getDisplay, 1)).list();
        List<Spu> afterDeleteSpuList = spuIds.size() == 0 ? new ArrayList<>() : spuService.lambdaQuery().in(Spu::getSpuId, spuIds).and(i -> i.eq(Spu::getDisplay, 1)).list();
        for (Category category : categoryList) {
            category.setDisplay(0);
            for (Spu spu : afterDeleteSpuList) {
                if (category.getCategoryId().equals(spu.getCategoryId())) {
                    category.setDisplay(1);
                }
            }
        }
        categoryService.updateBatchById(categoryList);

    }

    @Override
    public void batchAddSku(BatchSkuParam batchSkuParam) {
//        List<Sku> entitys = new ArrayList<>();

        for (SkuParam param : batchSkuParam.getSkuParams()) {
            Long skuId = null;
            Category category = this.getOrSaveCategory(param);
            Long categoryId = category.getCategoryId();

            SpuClassification spuClassification = spuClassificationService.getById(param.getSpuClass());
            Long spuClassificationId = spuClassification.getSpuClassificationId();
            Integer parentSpuClassifications = spuClassificationService.query().eq("pid", spuClassification.getSpuClassificationId()).eq("display", 1).count();
            if (parentSpuClassifications > 0) {
                throw new ServiceException(500, "物料必须添加在最底级分类中");
            }
            /**
             * sku名称（skuName）加型号(spuName)判断防止重复
             */
            Spu spu = this.getOrSaveSpu(param, spuClassificationId, categoryId);
            Long spuId = spu.getSpuId();
            //生成编码
            if (ToolUtil.isEmpty(param.getStandard())) {
                CodingRules codingRules = codingRulesService.query().eq("module", "0").eq("state", 1).one();
                if (ToolUtil.isNotEmpty(codingRules)) {
                    String backCoding = codingRulesService.backCoding(codingRules.getCodingRulesId(), spu.getSpuId());
                    SpuClassification classification = spuClassificationService.query().eq("spu_classification_id", param.getSpuClass()).one();
                    if (ToolUtil.isNotEmpty(classification) && classification.getDisplay() != 0) {
                        String replace = "";
                        if (ToolUtil.isNotEmpty(classification.getCodingClass())) {
                            replace = backCoding.replace("${skuClass}", classification.getCodingClass());
                        } else {
                            replace = backCoding.replace("${skuClass}", "");
                        }

                        param.setStandard(replace);
                        param.setCoding(replace);

                    }
                } else {
                    throw new ServiceException(500, "当前无此规则");
                }
            }
            /**
             * 判断成品码是否重复
             */
            int count = skuService.count(new QueryWrapper<Sku>() {{
                eq("standard", param.getStandard());
                eq("display", 1);
            }});
            if (count > 0) {
                throw new ServiceException(500, "编码/成品码重复");
            }


//        List<Sku> skuName = skuService.query().eq("sku_name", param.getSkuName()).and(i -> i.eq("display", 1)).list();
//        if (ToolUtil.isNotEmpty(spu) && ToolUtil.isNotEmpty(skuName)) {
//            throw new ServiceException(500, "此物料在产品中已存在");
//        }
            /**
             * 查询产品，添加产品 在上方spu查询
             */

            List<AttributeValues> list = this.addAttributeAndValue(param.getSku(), categoryId);

            Sku entity = getEntity(param);


            String json = JSON.toJSONString(list);

            entity.setSpuId(spuId);
            entity.setSkuValue(json);

            String md5 = SecureUtil.md5(entity.getSkuValue() + entity.getSpuId().toString() + entity.getSkuName() + spuClassification.getSpuClassificationId());

            entity.setSkuValueMd5(md5);
            this.save(entity);
            skuId = entity.getSkuId();

            if (ToolUtil.isNotEmpty(param.getBrandIds())) {
                skuBrandBindService.addBatch(new SkuBrandBindParam() {{
                    setBrandIds(param.getBrandIds());
                    setSkuId(entity.getSkuId());
                }});
            }
            if (ToolUtil.isNotEmpty(param.getOldSkuId()) && ToolUtil.isNotEmpty(skuId)) {
                ActivitiProcess activitiProcess = processService.query().eq("form_id", param.getOldSkuId()).eq("type", "ship").eq("display", 1).one();
                if (ToolUtil.isNotEmpty(activitiProcess)) {
                    copyProcessRoute(param.getOldSkuId(), skuId);
                } else {
                    copySkuBomById(param.getOldSkuId(), skuId);
                }
            }
        }

    }

    /**
     * 替换编码
     *
     * @param param
     * @param spuId
     * @return
     */
    private void getCoding(SkuParam param, Long spuId) {
        CodingRules codingRules = codingRulesService.query().eq("module", "0").eq("state", 1).one();
        if (ToolUtil.isNotEmpty(codingRules)) {
            String backCoding = codingRulesService.backCoding(codingRules.getCodingRulesId(), spuId);
//                SpuClassification classification = spuClassificationService.query().eq("spu_classification_id", spuClassificationId).one();
            SpuClassification classification = spuClassificationService.query().eq("spu_classification_id", param.getSpuClass()).one();

        } else {
            throw new ServiceException(500, "当前无此规则");
        }
    }


    @Override
    public List<SkuBind> skuBindList(SkuBindParam skuBindParam) {
        if (ToolUtil.isNotEmpty(skuBindParam.getBomIds())) {   //bom
            List<Long> skuIds = skuBindParam.getSkuIds();
            if (ToolUtil.isEmpty(skuIds)) {
                skuIds = new ArrayList<>();
            }
            for (Long bomId : skuBindParam.getBomIds()) {
                skuIds.addAll(partsService.getSkuIdsByBom(bomId));
            }
            skuBindParam.setSkuIds(skuIds);
        }
        return this.baseMapper.skuBindList(skuBindParam);
    }


    @Override
    @BussinessLog
    @Transactional
    public void update(SkuParam param) {
        Sku oldEntity = getOldEntity(param);

        Category category = this.getOrSaveCategory(param);
        Long categoryId = category.getCategoryId();

        List<AttributeValues> list = this.addAttributeAndValue(param.getSku(), categoryId);

        SpuClassification spuClassification = spuClassificationService.getById(param.getSpuClass());
        Long spuClassificationId = spuClassification.getSpuClassificationId();
        Spu orSaveSpu = this.getOrSaveSpu(param, spuClassificationId, categoryId);

        Long spuId = oldEntity.getSpuId();
        Spu oldSpu = spuService.getById(spuId);


        if (ToolUtil.isEmpty(param.getStandard())) {
            getCoding(param, orSaveSpu.getSpuId());
        }
        Sku newEntity = getEntity(param);
        newEntity.setSpuId(orSaveSpu.getSpuId());
        String json = JSON.toJSONString(list);
        newEntity.setSkuValue(json);
        Dict dict = dictService.query().eq("code", "editSku").one();
        boolean editSkuFlag = ToolUtil.isNotEmpty(dict) && dict.getStatus().equals("ENABLE");
//        String md5 = SecureUtil.md5(newEntity.getSpuId() + newEntity.getSkuValue());
        String md5 = SecureUtil.md5(newEntity.getSkuValue() + newEntity.getSpuId().toString() + newEntity.getSkuName() + spuClassification.getSpuClassificationId());
        if ((
//                !oldEntity.getSkuValueMd5().equals(md5)
                !oldEntity.getSkuName().equals(param.getSkuName())//
                        || !param.getUnitId().equals(orSaveSpu.getUnitId())//
                        || !oldEntity.getBatch().equals(param.getBatch())
                        || !oldEntity.getSpuId().equals(param.getSpuId())
                        || (orSaveSpu.getSpuId().equals(oldSpu.getSpuId()) && !oldSpu.getUnitId().equals(param.getUnitId()))
        ) && editSkuFlag){
            /**
             * 如要变更sku主要信息数据  需要验证物料是否正在被 物料清单所使用   如果被使用则不可更改
             * 如果只是更新 上传附件与图片之类资料完善则不需查询清单中是否被使用
             */
            List<ErpPartsDetail> partsDetailList = partsDetailService.lambdaQuery().eq(ErpPartsDetail::getSkuId, param.getSkuId()).list();
            List<Parts> partList = partsService.lambdaQuery().eq(Parts::getSkuId, param.getSkuId()).and(i -> i.eq(Parts::getDisplay, 1)).list();
            if (ToolUtil.isNotEmpty(partsDetailList) || ToolUtil.isNotEmpty(partList)) {
                throw new ServiceException(500, "清单中有此物品数据,不可修改");

            }
        }
        newEntity.setSkuValueMd5(md5);


        /**
         * 更新品牌
         */
        if (ToolUtil.isNotEmpty(param.getBrandIds())) {
            List<SkuBrandBind> binds = skuBrandBindService.query().eq("sku_id", newEntity.getSkuId()).list();
            for (SkuBrandBind bind : binds) {
                bind.setDisplay(0);
            }
            skuBrandBindService.updateBatchById(binds);

            skuBrandBindService.addBatch(new SkuBrandBindParam() {{
                setBrandIds(param.getBrandIds());
                setSkuId(newEntity.getSkuId());
            }});
        }

        this.updateById(newEntity);
        if (ToolUtil.isNotEmpty(param.getMaintenancePeriod())) {
            MaintenanceCycle maintenanceCycle = maintenanceCycleService.query().eq("sku_id", newEntity.getSkuId()).eq("display", 1).one();
            if (ToolUtil.isNotEmpty(maintenanceCycle)) {
                if (param.getMaintenancePeriod() != maintenanceCycle.getMaintenancePeriod()) {
                    maintenanceCycle.setMaintenancePeriod(param.getMaintenancePeriod());
                    maintenanceCycleService.updateById(maintenanceCycle);
                }
            } else {
                maintenanceCycle = new MaintenanceCycle();
                maintenanceCycle.setSkuId(newEntity.getSkuId());
                maintenanceCycle.setMaintenancePeriod(param.getMaintenancePeriod());
                maintenanceCycleService.save(maintenanceCycle);

            }
        }


    }

    @Override
    public SkuResult findBySpec(SkuParam param) {
        return null;
    }

    @Override
    public List<SkuResult> findListBySpec(SkuParam param) {
//        if (param.getSkuIds() != null) {
//            if (param.getSkuIds().size() == 0) {
//                return null;
//            }
//        }
//        List<Long> spuIds = null;
//        if (ToolUtil.isNotEmpty(param.getSpuClass())) {
//            spuIds = new ArrayList<>();
//            List<SpuClassification> classifications = spuClassificationService.query().eq("pid", param.getSpuClass()).eq("display", 1).list();
//            List<Long> classIds = new ArrayList<>();
//            for (SpuClassification classification : classifications) {
//                classIds.add(classification.getSpuClassificationId());
//            }
//            List<Spu> spuList = classIds.size() == 0 ? new ArrayList<>() : spuService.query().in("spu_classification_id", classIds).eq("display", 1).list();
//            for (Spu spu : spuList) {
//                spuIds.add(spu.getSpuId());
//            }
//            if (ToolUtil.isEmpty(spuList)) {
//                spuIds.add(0L);
//            }
//        } else {
//            spuIds = new ArrayList<>();
//        }
        List<SkuResult> skuResults = this.baseMapper.customList(param);
        format(skuResults);
        return skuResults;
    }

    @Override
    public PageInfo findPageBySpec(SkuParam param) {
        if (param.getSkuIds() != null) {
            if (param.getSkuIds().size() == 0) {
                return null;
            }
        }
        Page<SkuResult> pageContext = getPageContext();
        IPage<SkuResult> page = this.baseMapper.customPageList(new ArrayList<>(), pageContext, param);
        format(page.getRecords());


        return PageFactory.createPageInfo(page);
    }

    @Override
    public PageInfo changePageBySpec(SkuParam param) {
        /**
         * 库位条件查询
         */
        if (ToolUtil.isNotEmpty(param.getStorehousePositionsId())) {
            List<Long> loopPositionIds = positionsService.getLoopPositionIds(param.getStorehousePositionsId());
            param.setStorehousePositionsIds(loopPositionIds);
        }

        /**
         * 异常件查询
         */
        if (ToolUtil.isNotEmpty(param.getStatus()) && param.getStatus() == -1) {
            List<StockDetails> stock = stockDetailsService.getStock();
            List<Long> inkindIds = new ArrayList<>();
            for (StockDetails details : stock) {
                inkindIds.add(details.getInkindId());
            }
            List<Long> skuIds = inkindService.anomalySku(inkindIds);
            if (skuIds.size() == 0) {
                skuIds.add(0L);
            }
            param.setAnomalySkuIds(skuIds);
        }

        /**
         * 查询这个物料的bom
         */
        if (ToolUtil.isNotEmpty(param.getPartsSkuId())) {
            if (ToolUtil.isEmpty(param.getSelectBom())) {
                param.setSelectBom(SelectBomEnum.Present);
            }
            Parts parts = partsService.query().eq("sku_id", param.getPartsSkuId()).eq("status", 99).eq("display", 1).one();
            if (ToolUtil.isEmpty(parts)) {
                return new PageInfo();
            }
            switch (param.getSelectBom()) {
                case All:
                    List<ErpPartsDetailResult> detailResults = partsDetailService.recursiveDetails(parts.getSkuId(), null);  //最末级物料
                    List<Long> skuIdsByBom = new ArrayList<>();
                    for (ErpPartsDetailResult detailResult : detailResults) {
                        skuIdsByBom.add(detailResult.getSkuId());
                    }
//                    List<Long> skuIdsByBom = partsService.getSkuIdsByBom(parts.getSkuId());    //bom 全部物料
                    param.setSkuIds(skuIdsByBom);
                    break;
                case Present:
                    List<ErpPartsDetail> partsDetails = partsDetailService.query().eq("parts_id", parts.getPartsId()).list();
                    List<Long> skuIds = param.getSkuIds();
                    for (ErpPartsDetail partsDetail : partsDetails) {
                        skuIds.add(partsDetail.getSkuId());
                    }
                    break;
            }
        }


        Page<SkuResult> pageContext = getPageContext();
        selectFormat(param);  //查询格式化
        IPage<SkuResult> page = this.baseMapper.changeCustomPageList(new ArrayList<>(), pageContext, param);
        PageInfo pageInfo = PageFactory.createPageInfo(page);


        if (param.getStockView()) {  //是否开启查询

            List<SkuResult> skuResultList = this.baseMapper.allList(new ArrayList<>(), param);  //查询所有结果集


            List<Long> skuIds = new ArrayList<>();
            skuIds.add(0L);
            int countNumber = 0;
            for (SkuResult skuResult : skuResultList) {
                skuIds.add(skuResult.getSkuId());
                countNumber = countNumber + skuResult.getStockNumber();
            }
            Map<String, Integer> countMap = new HashMap<>();
            countMap.put("stockCount", countNumber);
            List<Object> searchObjects = new ArrayList<>();

            /**
             * bom查询
             */
            SearchObject bomSearch = null;
            if (param.getOpenBom()) {
                if (ToolUtil.isNotEmpty(param.getPartsSkuId())) {
                    List<Parts> parts = partsService.query().eq("status", 99).eq("display", 1).list();
                    bomSearch = bomSearch(new ArrayList<Long>() {{
                        for (Parts part : parts) {
                            add(part.getSkuId());
                        }
                    }});
                } else {
                    bomSearch = bomSearch(skuIds);
                }
            }

            /**
             * 分類查詢
             */
            SearchObject classSearch = null;
            if (ToolUtil.isNotEmpty(param.getSpuClassIds())) {
                List<SkuBasisView> basisViews = skuBasisViewService.list();
                classSearch = spuClassSearch(new ArrayList<Long>() {{
                    for (SkuBasisView basisView : basisViews) {
                        add(basisView.getSpuClassificationId());
                    }
                }});
            } else {
                List<SkuBasisView> skuBasisViews = skuIds.size() == 0 ? new ArrayList<>() : skuBasisViewService.query().in("sku_id", skuIds).list();
                List<Long> classIds = new ArrayList<>();
                for (SkuBasisView skuBasisView : skuBasisViews) {
                    classIds.add(skuBasisView.getSpuClassificationId());
                }
                classSearch = spuClassSearch(classIds);
            }

            /**
             *  供应商过滤
             */
            List<Long> brandIds = new ArrayList<>();
            List<Long> customerIds = new ArrayList<>();
            List<SkuSupplyView> skuSupplyViews = skuSupplyViewService.list();
            SearchObject customerSearch = null;
            if (ToolUtil.isNotEmpty(param.getCustomerIds())) {  //不筛选

                for (SkuSupplyView skuSupplyView : skuSupplyViews) {
                    customerIds.add(skuSupplyView.getCustomerId());
                }
                customerSearch = customerSearch(customerIds);
            } else {
                for (SkuSupplyView skuSupplyView : skuSupplyViews) {
                    for (Long skuId : skuIds) {
                        if (skuId.equals(skuSupplyView.getSkuId())) {
                            customerIds.add(skuSupplyView.getCustomerId());
                        }
                    }
                    customerSearch = customerSearch(customerIds);
                }
            }

            /**
             *  品牌过滤
             */
            SearchObject brandSearch = null;
            if (ToolUtil.isNotEmpty(param.getBrandIds())) {
                for (SkuSupplyView skuSupplyView : skuSupplyViews) {
                    brandIds.add(skuSupplyView.getBrandId());
                }
                brandSearch = brandSearch(brandIds);
            } else {
                for (SkuSupplyView skuSupplyView : skuSupplyViews) {
                    for (Long skuId : skuIds) {
                        if (skuId.equals(skuSupplyView.getSkuId())) {
                            brandIds.add(skuSupplyView.getBrandId());
                        }
                    }
                }
                brandSearch = brandSearch(brandIds);
            }


            /**
             *通过当前库位查询物料
             */
            SearchObject positionSearch = null;
            if (param.getOpenPosition()) {
                if (ToolUtil.isNotEmpty(param.getStorehousePositionsIds())) {
                    List<SkuPositionView> skuPositionViews = skuPositionViewService.list();
                    positionSearch = positionSearch(new ArrayList<Long>() {{
                        for (SkuPositionView skuPositionView : skuPositionViews) {
                            add(skuPositionView.getSkuId());
                        }
                    }});
                } else {
                    positionSearch = positionSearch(skuIds);
                }
            }

            searchObjects.add(positionSearch);
            searchObjects.add(customerSearch);
            searchObjects.add(brandSearch);
            searchObjects.add(classSearch);
            searchObjects.add(bomSearch);
            searchObjects.add(statusSearch());
            searchObjects.add(countMap);
            pageInfo.setSearch(searchObjects);
        }

        this.isSupply(param, page.getRecords());
        this.format(page.getRecords());
        /**
         * 是否查询仓库和库位
         */
        positionsService.skuFormat(page.getRecords());

        return pageInfo;
    }

    private void selectFormat(SkuParam param) {
        if (ToolUtil.isNotEmpty(param.getStorehousePositionsIds())) {
            List<Long> positionIds = new ArrayList<>();
            for (Long storehousePositionsId : param.getStorehousePositionsIds()) {
                positionIds.addAll(positionsService.getEndChild(storehousePositionsId));
            }
            param.setStorehousePositionsIds(positionIds);
        }
    }

    /**
     * 状态条件
     */
    private SearchObject statusSearch() {
        SearchObject searchObject = new SearchObject();

        searchObject.setTitle("状态");
        searchObject.setKey("status");
        searchObject.setTypeEnum(SearchTypeEnum.List);
        searchObject.setObjects(new ArrayList<SearchDetail>() {{
            add(new SearchDetail(0L, "特别关注"));
            add(new SearchDetail(1L, "上限预警"));
            add(new SearchDetail(2L, "下线预警"));
            add(new SearchDetail(3L, "入库超期"));
            add(new SearchDetail(4L, "停用"));
        }});
        return searchObject;
    }

    /**
     * 库位条件
     */
    private SearchObject positionSearch(List<Long> skuIds) {
        SearchObject searchObject = new SearchObject();
        List<PositionLoop> positionLoops = positionsService.treeViewBySku(skuIds);
        searchObject.setKey("position");
        searchObject.setTitle("库位");
        searchObject.setObjects(positionLoops);
        searchObject.setTypeEnum(SearchTypeEnum.position);
        return searchObject;
    }

    /**
     * 分类条件
     *
     * @param
     * @return
     */
    private SearchObject spuClassSearch(List<Long> classIds) {
        SearchObject searchObject = new SearchObject();
        List<SearchDetail> results = new ArrayList<>();
        List<SpuClassification> spuClassifications = classIds.size() == 0 ? new ArrayList<>() : spuClassificationService.listByIds(classIds);
        for (SpuClassification spuClassification : spuClassifications) {
            SearchDetail searchDetail = new SearchDetail();
            searchDetail.setKey(spuClassification.getSpuClassificationId());
            searchDetail.setTitle(spuClassification.getName());
            if (results.stream().noneMatch(i -> i.getKey().equals(spuClassification.getSpuClassificationId()))) {
                results.add(searchDetail);
            }
        }

        searchObject.setKey("spuClass");
        searchObject.setTitle("分类");
        searchObject.setTypeEnum(SearchTypeEnum.List);
        searchObject.setObjects(results);
        return searchObject;
    }

    /**
     * 供应商条件
     *
     * @param
     * @return
     */
    private SearchObject customerSearch(List<Long> customerIds) {
        SearchObject searchObject = new SearchObject();
        List<SearchDetail> results = new ArrayList<>();
        if (customerIds.size() > 50) {
            searchObject.setTypeEnum(SearchTypeEnum.overLength);
        } else {
            List<Customer> customerList = customerIds.size() == 0 ? new ArrayList<>() : customerService.listByIds(customerIds);
            for (Customer customerResult : customerList) {
                SearchDetail customerSearch = new SearchDetail();
                customerSearch.setTitle(customerResult.getCustomerName());
                customerSearch.setKey(customerResult.getCustomerId());
                if (results.stream().noneMatch(i -> i.getKey().equals(customerResult.getCustomerId()))) {
                    results.add(customerSearch);
                }
            }
            searchObject.setObjects(results);
            searchObject.setTypeEnum(SearchTypeEnum.List);
        }

        searchObject.setTitle("供应商");
        searchObject.setKey("customer");
        return searchObject;
    }

    /**
     * 品牌条件
     *
     * @param
     * @return
     */
    private SearchObject brandSearch(List<Long> brandIds) {
        SearchObject searchObject = new SearchObject();
        if (brandIds.size() > 50) {     //判断超长
            searchObject.setTypeEnum(SearchTypeEnum.overLength);
        } else {
            List<Brand> brandList = brandIds.size() == 0 ? new ArrayList<>() : brandService.listByIds(brandIds);
            List<SearchDetail> results = new ArrayList<>();

            for (Brand brand : brandList) {
                SearchDetail brandSearch = new SearchDetail();
                brandSearch.setKey(brand.getBrandId());
                brandSearch.setTitle(brand.getBrandName());
                if (results.stream().noneMatch(i -> i.getKey().equals(brand.getBrandId()))) {
                    results.add(brandSearch);
                }
            }
            searchObject.setTypeEnum(SearchTypeEnum.List);
            searchObject.setObjects(results);
        }

        searchObject.setTitle("品牌");
        searchObject.setKey("brand");

        return searchObject;
    }

    /**
     * bom 条件
     *
     * @param
     * @return
     */
    private SearchObject bomSearch(List<Long> skuIds) {
        SearchObject searchObject = new SearchObject();


        List<Parts> parts = skuIds.size() == 0 ? new ArrayList<>() : partsService.query().in("sku_id", skuIds).eq("status", 99).eq("display", 1).list();

        List<Long> partSkuIds = new ArrayList<>();
        for (Parts part : parts) {
            partSkuIds.add(part.getSkuId());
        }

        List<Sku> skus = partSkuIds.size() == 0 ? new ArrayList<>() : this.listByIds(partSkuIds);
        List<SkuResult> skuResults = BeanUtil.copyToList(skus, SkuResult.class, new CopyOptions());
        spuService.skuFormat(skuResults);

        List<SearchDetail> results = new ArrayList<>();
        for (SkuResult skuResult : skuResults) {
            for (Parts part : parts) {
                if (part.getSkuId().equals(skuResult.getSkuId())) {
                    SearchDetail bomSearch = new SearchDetail();
                    bomSearch.setKey(part.getSkuId());
                    bomSearch.setTitle(skuResult.getSpuName() + " " + skuResult.getSkuName());
                    if (results.stream().noneMatch(i -> i.getKey().equals(skuResult.getSkuId()))) {
                        results.add(bomSearch);
                    }
                    break;
                }
            }

        }
        searchObject.setKey("bom");
        searchObject.setTypeEnum(SearchTypeEnum.bom);
        searchObject.setTitle("物料清单");
        searchObject.setObjects(results);
        return searchObject;
    }

    public void isSupply(SkuParam skuParam, List<SkuResult> results) {
        List<Long> skuIds = new ArrayList<>();
        for (SkuResult result : results) {
            skuIds.add(result.getSkuId());
        }
        List<Supply> supplies = skuIds.size() == 0 ? new ArrayList<>() : supplyService.query().in("sku_id", skuIds).list();
        for (SkuResult result : results) {
            result.setInSupply(false);
            for (Supply supply : supplies) {
                if (result.getSkuId().equals(supply.getSkuId()) && supply.getCustomerId().equals(skuParam.getCustomerId())) {
                    result.setInSupply(true);
                }
            }
        }

    }

    @Override
    public List<SkuResult> AllSku() {
        List<SkuResult> skuResults = this.baseMapper.customList(new SkuParam());
        format(skuResults);
        return skuResults;
    }

    private void getSpu(List<SkuResult> skuResults) {
        List<Long> spuIds = new ArrayList<>();
        for (SkuResult skuResult : skuResults) {
            spuIds.add(skuResult.getSpuId());
        }
        List<Spu> spus = spuIds.size() == 0 ? new ArrayList<>() : spuService.query().in("spu_id", spuIds).eq("display", 1).list();
        List<SpuResult> spuResults = BeanUtil.copyToList(spus, SpuResult.class, new CopyOptions());

        for (SkuResult skuResult : skuResults) {
            for (SpuResult spuResult : spuResults) {
                if (skuResult.getSpuId().equals(spuResult.getSpuId())) {
                    skuResult.setSpuResult(spuResult);
                    break;
                }
            }
        }


    }


    @Override
    public void format(List<SkuResult> param) {

        List<Long> spuIds = new ArrayList<>();
        List<Long> attributeIds = new ArrayList<>();
        List<Long> userIds = new ArrayList<>();
        List<Long> skuIds = new ArrayList<>();
        for (SkuResult skuResult : param) {
            skuIds.add(skuResult.getSkuId());
            spuIds.add(skuResult.getSpuId());
            userIds.add(skuResult.getCreateUser());
            JSONArray jsonArray = JSONUtil.parseArray(skuResult.getSkuValue());
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            for (AttributeValues valuesRequest : valuesRequests) {
                attributeIds.add(valuesRequest.getAttributeId());
            }
        }
        List<MaintenanceCycle> maintenanceCycles = skuIds.size() == 0 ? new ArrayList<>() : maintenanceCycleService.query().in("sku_id", skuIds).eq("display", 1).list();

        List<ItemAttribute> itemAttributes = itemAttributeService.lambdaQuery().list();
        List<AttributeValues> attributeValues = attributeIds.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery()
                .in(AttributeValues::getAttributeId, attributeIds)
                .list();
        List<Spu> spus = spuIds.size() == 0 ? new ArrayList<>() : spuService.query().in("spu_id", spuIds).list();
        List<Long> unitIds = new ArrayList<>();
        List<Long> spuClassId = new ArrayList<>();
        Map<Long, UnitResult> unitMaps = new HashMap<>();
        Map<Long, SpuClassificationResult> spuClassificationMap = new HashMap<>();
        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
        for (Spu spu : spus) {
            if (ToolUtil.isNotEmpty(spu.getUnitId())) {
                unitIds.add(spu.getUnitId());
                spuClassId.add(spu.getSpuClassificationId());
            }
        }

        List<Unit> units = unitIds.size() == 0 ? new ArrayList<>() : unitService.query().in("unit_id", unitIds).eq("display", 1).list();
        List<SpuClassification> spuClassifications = spuClassId.size() == 0 ? new ArrayList<>() : spuClassificationService.query().in("spu_classification_id", spuClassId).list();

        for (Spu spu : spus) {
            if (ToolUtil.isNotEmpty(units)) {
                for (Unit unit : units) {
                    if (spu.getUnitId() != null && spu.getUnitId().equals(unit.getUnitId())) {
                        UnitResult unitResult = new UnitResult();
                        ToolUtil.copyProperties(unit, unitResult);
                        unitMaps.put(spu.getSpuId(), unitResult);
                    }
                }
                for (SpuClassification spuClassification : spuClassifications) {
                    if (spu.getSpuClassificationId() != null && spu.getSpuClassificationId().equals(spuClassification.getSpuClassificationId())) {
                        SpuClassificationResult classification = new SpuClassificationResult();
                        ToolUtil.copyProperties(spuClassification, classification);
                        spuClassificationMap.put(spu.getSpuId(), classification);
                    }
                }
            }
        }

        /**
         * 查询品牌
         */
        List<SkuBrandBind> skuBrandBinds = skuIds.size() == 0 ? new ArrayList<>() : skuBrandBindService.query().in("sku_id", skuIds).eq("display", 1).list();

        Map<Long, List<Long>> brandMapIds = new HashMap<>();
        List<Long> brandIds = new ArrayList<>();
        for (SkuBrandBind bind : skuBrandBinds) {
            brandIds.add(bind.getBrandId());
            List<Long> list = brandMapIds.get(bind.getSkuId());
            if (ToolUtil.isEmpty(list)) {
                list = new ArrayList<>();
            }
            list.add(bind.getBrandId());
            brandMapIds.put(bind.getSkuId(), list);
        }
        List<BrandResult> brandResults = brandService.getBrandResults(brandIds);

        /**
         * 查询清单
         */
        List<Parts> parts = skuIds.size() == 0 ? new ArrayList<>() : partsService.query().in("sku_id", skuIds).eq("display", 1).eq("status", 99).list();
//        List<ActivitiProcess> processes = skuIds.size() == 0 ? new ArrayList<>() : processService.query().in("form_id", skuIds).eq("type", "ship").eq("display", 1).list();
        /**
         * 库存数
         */
        List<StockDetails> stockDetailsList = stockDetailsService.query().select("sku_id, sum(number) as num").eq("display", 1).groupBy("sku_id").list();
        /**
         * 查询已占用库存数
         */
        List<StockDetails> lockStockDetail = pickListsCartService.getLockStockDetail();


        for (SkuResult skuResult : param) {
            for (MaintenanceCycle maintenanceCycle : maintenanceCycles) {
                if (skuResult.getSkuId().equals(maintenanceCycle.getSkuId())) {
                    skuResult.setMaintenancePeriod(maintenanceCycle.getMaintenancePeriod());
                }
            }
            List<BrandResult> brandResultList = new ArrayList<>();
            List<Long> allBrandIds = brandMapIds.get(skuResult.getSkuId());
            if (ToolUtil.isNotEmpty(allBrandIds)) {
                for (Long allBrandId : allBrandIds) {
                    for (BrandResult brandResult : brandResults) {
                        if (allBrandId.equals(brandResult.getBrandId())) {
                            brandResultList.add(brandResult);
                        }
                    }
                }
                skuResult.setBrandResults(brandResultList);
            }


            for (StockDetails stockDetails : stockDetailsList) {
                if (skuResult.getSkuId().equals(stockDetails.getSkuId())) {
                    skuResult.setStockNumber(Math.toIntExact(stockDetails.getNum()));
                }
            }

            for (StockDetails stockDetails : lockStockDetail) {
                if (stockDetails.getSkuId().equals(skuResult.getSkuId())) {
                    skuResult.setLockStockDetailNumber(Math.toIntExact(stockDetails.getNumber()));
                }
            }
            //图片
            try {
                List<Long> imageIds = ToolUtil.isEmpty(skuResult.getImages()) ? new ArrayList<>() : Arrays.asList(skuResult.getImages().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                List<MediaUrlResult> mediaUrlResults = mediaService.getMediaUrlResults(new ArrayList<Long>(){{
                    add(imageIds.get(0));
                }});
                if (imageIds.size()>0){
                    skuResult.setImgResults(mediaUrlResults);
                }
            } catch (Exception e) {

            }
            List<String> imageUrls = new ArrayList<>();
            List<String> imgThumbUrls = new ArrayList<>();
//            for (Long imageid : imageIds) {
//                imageUrls.add(mediaService.getMediaUrl(imageid, 1L));
//                String imgThumbUrl = mediaService.getMediaUrlAddUseData(imageid, 0L, "image/resize,m_fill,h_200,w_200");
//                imgThumbUrls.add(imgThumbUrl);
//            }

//            skuResult.setImgUrls(imageUrls);
//            skuResult.setImgThumbUrls(imgThumbUrls);

//            for (StockDetails stockDetails : totalLockDetail) {
//                if (stockDetails.getSkuId().equals(skuResult.getSkuId())) {
//                    skuResult.setLockStockDetailNumber(Math.toIntExact(stockDetails.getNumber()));
//                }
//            }


//            for (ActivitiProcess process : processes) {
//                if (process.getFormId().equals(skuResult.getSkuId())) {
//                    ActivitiProcessResult processResult = new ActivitiProcessResult();
//                    ToolUtil.copyProperties(process, processResult);
//                    skuResult.setProcessResult(processResult);
//                }
//            }
            skuResult.setInBom(false);

            for (Parts part : parts) {
                if (part.getSkuId().equals(skuResult.getSkuId())) {
                    skuResult.setInBom(true);
                    skuResult.setPartsId(part.getPartsId());
                    break;
                }
            }
            for (User user : users) {
                if (ToolUtil.isNotEmpty(skuResult.getCreateUser()) && user.getUserId().equals(skuResult.getCreateUser())) {
                    skuResult.setUser(user);
                    break;
                }
            }

            if (ToolUtil.isNotEmpty(spus)) {
                for (Spu spu : spus) {
                    if (spu.getSpuId() != null && skuResult.getSpuId() != null && spu.getSpuId().equals(skuResult.getSpuId())) {
                        SpuResult spuResult = new SpuResult();
                        ToolUtil.copyProperties(spu, spuResult);
                        UnitResult unitResult = unitMaps.get(spu.getSpuId());
                        SpuClassificationResult spuClassificationResult = spuClassificationMap.get(spu.getSpuId());
                        if (ToolUtil.isNotEmpty(unitResult)) {
                            spuResult.setUnitResult(unitResult);
                        }
                        if (ToolUtil.isNotEmpty(spuClassificationResult)) {
                            spuResult.setSpuClassificationResult(spuClassificationResult);
                        }
                        skuResult.setSpuResult(spuResult);
                        break;
                    }
                }
            }

            JSONArray jsonArray = JSONUtil.parseArray(skuResult.getSkuValue());
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            List<SkuJson> list = new ArrayList<>();
            for (AttributeValues valuesRequest : valuesRequests) {
                if (ToolUtil.isNotEmpty(valuesRequest.getAttributeId()) && ToolUtil.isNotEmpty(valuesRequest.getAttributeValuesId())) {
                    SkuJson skuJson = new SkuJson();
                    for (ItemAttribute itemAttribute : itemAttributes) {
                        if (itemAttribute.getAttributeId().equals(valuesRequest.getAttributeId())) {
                            Attribute attribute = new Attribute();
                            attribute.setAttributeId(itemAttribute.getAttributeId().toString());
                            attribute.setAttribute(itemAttribute.getAttribute());
                            skuJson.setAttribute(attribute);
                        }
                    }
                    for (AttributeValues attributeValue : attributeValues) {
                        if (valuesRequest.getAttributeValuesId().equals(attributeValue.getAttributeValuesId())) {
                            Values values = new Values();
                            values.setAttributeValuesId(valuesRequest.getAttributeValuesId().toString());
                            values.setAttributeValues(attributeValue.getAttributeValues());
                            skuJson.setValues(values);
                        }
                    }
                    list.add(skuJson);
                }
                skuResult.setSkuJsons(list);
            }
        }


    }

    @Override
    public SkuResult getSku(Long id) {
        Sku sku = this.getById(id);
        SkuResult skuResult = BeanUtil.copyProperties(sku, SkuResult.class);
        if (ToolUtil.isEmpty(sku)) {
            return skuResult;
        }
        this.format(new ArrayList<SkuResult>() {{
            add(skuResult);
        }});

        //返回附件图片等
        try {
            if (ToolUtil.isNotEmpty(skuResult.getFileId())) {
                List<Long> filedIds = Arrays.asList(skuResult.getFileId().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                List<String> filedUrls = new ArrayList<>();
                for (Long filedId : filedIds) {
                    String mediaUrl = mediaService.getMediaUrl(filedId, 0L);
                    filedUrls.add(mediaUrl);
                }
                skuResult.setFiledUrls(filedUrls);
            }
        } catch (Exception ignored) {

        }


        try {
            List<Long> imageIds = ToolUtil.isEmpty(skuResult.getImages()) ? new ArrayList<>() : Arrays.asList(skuResult.getImages().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            List<MediaUrlResult> mediaUrlResults = mediaService.getMediaUrlResults(imageIds);
            if (imageIds.size()>0){
                skuResult.setImgResults(mediaUrlResults);
            }
        } catch (Exception e) {

        }


        try {
            if (ToolUtil.isNotEmpty(skuResult.getDrawing())) {
                List<Long> filedIds = Arrays.asList(skuResult.getDrawing().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                List<String> filedUrls = new ArrayList<>();
                for (Long filedId : filedIds) {
                    String mediaUrl = mediaService.getMediaUrl(filedId, 0L);
                    filedUrls.add(mediaUrl);
                }
                skuResult.setDrawingUrls(filedUrls);
            }
        } catch (Exception ignored) {

        }

        try {
            if (ToolUtil.isNotEmpty(skuResult.getEnclosure())) {
                List<Long> enclosure = Arrays.asList(skuResult.getEnclosure().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                List<String> filedUrls = new ArrayList<>();
                for (Long filedId : enclosure) {
                    String mediaUrl = mediaService.getMediaUrl(filedId, 0L);
                    filedUrls.add(mediaUrl);
                }
                skuResult.setEnclosureUrls(filedUrls);
            }


        } catch (Exception ignored) {

        }
//        if (ToolUtil.isNotEmpty(skuResult.getImages())) {
//            List<Long> enclosure = Arrays.asList(skuResult.getImages().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
//            List<String> filedUrls = new ArrayList<>();
//            List<String> imgThumbUrls = new ArrayList<>();
//            for (Long filedId : enclosure) {
//                String mediaUrl = mediaService.getMediaUrl(filedId, 0L);
//                String imgThumbUrl = mediaService.getMediaUrlAddUseData(filedId, 0L,"image/resize,m_fill,h_200,w_200");
//                filedUrls.add(mediaUrl);
//                imgThumbUrls.add(imgThumbUrl);
//            }
//            skuResult.setImgUrls(filedUrls);
//            skuResult.setImgThumbUrls(imgThumbUrls);
//        }

//        List<SkuBrandBind> skuBrandBindList = skuBrandBindService.query().eq("sku_id", id).eq("display", 1).list();
//        List<Long> brandIds = new ArrayList<>();
//        for (SkuBrandBind skuBrandBind : skuBrandBindList) {
//            brandIds.add(skuBrandBind.getBrandId());
//        }
//        List<Brand> brands = brandIds.size() == 0 ? new ArrayList<>() : brandService.query().in("brand_id", brandIds).list();
//        List<BrandResult> brandResults = new ArrayList<>();
//        for (Brand brand : brands) {
//            BrandResult brandResult = new BrandResult();
//            ToolUtil.copyProperties(brand, brandResult);
//            brandResults.add(brandResult);
//        }
//
//        skuResult.setBrandIds(brandIds);
//
//        skuResult.setBrandResults(brandResults);


//        SpuResult spuResult = this.backSpu(sku.getSkuId());
//        skuResult.setSpuResult(spuResult);
//        ActivitiProcess process = processService.query().eq("form_id", id).eq("type", "ship").eq("display", 1).one();
//
//
//        if (ToolUtil.isNotEmpty(process)) {
//            ActivitiProcessResult processResult = new ActivitiProcessResult();
//            ToolUtil.copyProperties(process, processResult);
//            skuResult.setProcessResult(processResult);
//        }
//
//        Parts parts = partsService.query().eq("sku_id", id).eq("display", 1).eq("status", 99).eq("type", 1).one();
//
//        if (ToolUtil.isNotEmpty(parts)) {
//            skuResult.setInBom(true);
//            skuResult.setPartsId(parts.getPartsId());
//        }

        JSONArray jsonArray = JSONUtil.parseArray(skuResult.getSkuValue());
        List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
        List<Long> attIds = new ArrayList<>();
        List<Long> valueIds = new ArrayList<>();
        for (AttributeValues valuesRequest : valuesRequests) {
            attIds.add(valuesRequest.getAttributeId());
            valueIds.add(valuesRequest.getAttributeValuesId());
        }
        List<ItemAttribute> itemAttributes = attIds.size() == 0 ? new ArrayList<>() : itemAttributeService.query().in("attribute_id", attIds).eq("display", 1).eq("category_id", skuResult.getSpuResult().getCategoryId()).list();
        List<AttributeValues> valuesList = valueIds.size() == 0 ? new ArrayList<>() : attributeValuesService.query().in("attribute_values_id", valueIds).eq("display", 1).list();
        List<AttributeValuesResult> valuesResults = new ArrayList<>();

        for (AttributeValues valuesRequest : valuesList) {
            for (ItemAttribute itemAttribute : itemAttributes) {
                if (valuesRequest.getAttributeId().equals(itemAttribute.getAttributeId())) {
                    AttributeValuesResult valuesResult = new AttributeValuesResult();
                    ToolUtil.copyProperties(valuesRequest, valuesResult);
                    ItemAttributeResult itemAttributeResult = new ItemAttributeResult();
                    ToolUtil.copyProperties(itemAttribute, itemAttributeResult);
                    valuesResult.setItemAttributeResult(itemAttributeResult);
                    valuesResults.add(valuesResult);
                }
            }
        }
        List<AttributeValues> valuesAllList = attIds.size() == 0 ? new ArrayList<>() : attributeValuesService.query().in("attribute_id", attIds).eq("display", 1).list();

        skuResult.setList(valuesResults);
        List<AttributeInSpu> tree = new ArrayList<>();
        for (ItemAttribute itemAttribute : itemAttributes) {
            AttributeInSpu attribute = new AttributeInSpu();
            List<AttributeValueInSpu> values = new ArrayList<>();
            for (AttributeValues attributeValues : valuesAllList) {
                if (attributeValues.getAttributeId().equals(itemAttribute.getAttributeId())) {
                    AttributeValueInSpu value = new AttributeValueInSpu();
                    attribute.setK(itemAttribute.getAttribute());
                    attribute.setK_s(itemAttribute.getAttributeId());
                    value.setAttributeId(itemAttribute.getAttributeId());
                    value.setId(attributeValues.getAttributeValuesId());
                    value.setName(attributeValues.getAttributeValues());
                    values.add(value);
                    attribute.setV(values);
                }
            }
            tree.add(attribute);
        }

        SkuRequest skuRequest = new SkuRequest();
        skuRequest.setTree(tree);
        skuResult.setSkuTree(skuRequest);


        return skuResult;
    }

    /**
     * 物料信息接口
     *
     * @param skuId
     * @return
     */
    @Override
    public String skuMessage(Long skuId) {
        /**
         * 拼接物料信息
         */
        String message = "";
        try {
            if (ToolUtil.isNotEmpty(skuId)) {
                List<SkuResult> skuResults = this.formatSkuResult(new ArrayList<Long>() {{
                    add(skuId);
                }});
                if (ToolUtil.isNotEmpty(skuResults)) {
                    SkuResult skuResult = skuResults.get(0);
                    String skuName = skuResult.getSkuName();
                    String spuName = skuResult.getSpuResult().getName();
                    if (ToolUtil.isEmpty(spuName)) {
                        spuName = "";
                    }
                    String spe = skuResult.getSpecifications();
                    if (ToolUtil.isNotEmpty(spe)) {
                        spe = "/" + spe;
                    } else {
                        spe = "";
                    }
                    message = spuName + "/" + skuName + spe;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public List<SkuResult> formatSkuResult(List<Long> skuIds) {
        if (ToolUtil.isEmpty(skuIds)) {
            return new ArrayList<>();
        }
        List<Sku> skus = this.listByIds(skuIds);
        List<SkuResult> skuResults = new ArrayList<>();
        for (Sku sku : skus) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku, skuResult);
            skuResults.add(skuResult);
        }
        this.format(skuResults);

        return skuResults;
    }

    @Override
    public List<SkuSimpleResult> simpleFormatSkuResult(List<Long> skuIds) {
        List<SkuResult> skuResults = this.formatSkuResult(skuIds);
        this.format(skuResults);
        List<SkuSimpleResult> skuSimpleResults = new ArrayList<>();
        for (SkuResult skuResult : skuResults) {
            SkuSimpleResult skuSimpleResult = new SkuSimpleResult();
            ToolUtil.copyProperties(skuResult, skuSimpleResult);
            skuSimpleResults.add(skuSimpleResult);
        }


        return skuSimpleResults;
    }

    private Serializable getKey(SkuParam param) {
        return param.getSkuId();
    }

    private Page<SkuResult> getPageContext() {
        List<String> fields = new ArrayList<String>() {{
            add("createTime");
            add("skuName");
            add("spuName");
            add("stockNumber");
            add("spuId");
        }};
        return PageFactory.defaultPage(fields);
    }

    private Sku getOldEntity(SkuParam param) {
        return this.getById(getKey(param));
    }

    private Sku getEntity(SkuParam param) {
        Sku entity = new Sku();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<BackSku> backSku(Long ids) {

        Sku sku = this.query().eq("sku_id", ids).one();
        if (ToolUtil.isNotEmpty(sku)) {
            JSONArray jsonArray = JSONUtil.parseArray(sku.getSkuValue());
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            List<Long> atrIds = new ArrayList<>();
            List<Long> atrValueIds = new ArrayList<>();
            for (AttributeValues valuesRequest : valuesRequests) {
                atrIds.add(valuesRequest.getAttributeId());
                atrValueIds.add(valuesRequest.getAttributeValuesId());
            }

            List<AttributeValues> values = atrValueIds.size() == 0 ? new ArrayList<>() : attributeValuesService.query().in("attribute_values_id", atrValueIds).list();
            List<ItemAttribute> attributes = atrIds.size() == 0 ? new ArrayList<>() : itemAttributeService.query().in("attribute_id", atrIds).list();

            List<BackSku> backSkus = new ArrayList<>();
            for (AttributeValues valuesRequest : valuesRequests) {
                for (AttributeValues value : values) {
                    if (value.getAttributeValuesId().equals(valuesRequest.getAttributeValuesId())) {
                        BackSku backSku = new BackSku();
                        backSku.setAttributeValues(value);
                        for (ItemAttribute attribute : attributes) {
                            if (attribute.getAttributeId().equals(valuesRequest.getAttributeId())) {
                                backSku.setItemAttribute(attribute);
                                backSkus.add(backSku);
                            }
                        }
                    }
                }
            }
            return backSkus;
        }

        return new ArrayList<>();

    }

    @Override
    public List<SkuResult> backSkuList(List<Long> skuIds) {
        List<Sku> skuList = skuIds.size() == 0 ? new ArrayList<>() : skuService.lambdaQuery().in(Sku::getSkuId, skuIds).list();
        List<Long> attIds = new ArrayList<>();
        List<SkuResult> results = new ArrayList<>();
        for (Sku sku : skuList) {
            String skuValue = sku.getSkuValue();
            JSONArray jsonArray = JSONUtil.parseArray(skuValue);
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            for (AttributeValues valuesRequest : valuesRequests) {
                attIds.add(valuesRequest.getAttributeId());
            }
        }
        List<AttributeValues> attValuesList = attIds.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery().in(AttributeValues::getAttributeId, attIds).list();
        for (Sku sku : skuList) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku, skuResult);
            String skuValue = sku.getSkuValue();
            JSONArray jsonArray = JSONUtil.parseArray(skuValue);
            List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
            StringBuffer sb = new StringBuffer();
            for (AttributeValues valuesRequest : valuesRequests) {
                for (AttributeValues values : attValuesList) {
                    if (valuesRequest.getAttributeValuesId().equals(values.getAttributeValuesId())) {
                        sb.append(values.getAttributeValues() + ",");
                    }
                }
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
            skuResult.setSkuTextValue(sb.toString());
            results.add(skuResult);
        }
        return results;
    }


    @Override
    public SpuResult backSpu(Long skuId) {
        Sku sku = skuService.query().eq("sku_id", skuId).one();
        if (ToolUtil.isNotEmpty(sku)) {
            Spu spu = spuService.query().eq("spu_id", sku.getSpuId()).one();
            SpuResult spuResult = new SpuResult();
            if (ToolUtil.isNotEmpty(spu)) {
                SpuClassificationResult spuClassificationResult = new SpuClassificationResult();
                SpuClassification spuClassification = spuClassificationService.getById(spu.getSpuClassificationId());
                ToolUtil.copyProperties(spuClassification, spuClassificationResult);
                spuResult.setSpuClassificationResult(spuClassificationResult);
                ToolUtil.copyProperties(spu, spuResult);
            }
            return spuResult;
        }
        return null;
    }

    @Override
    public Map<Long, List<BackSku>> sendSku(List<Long> skuIds) {
        if (skuIds.size() == 0) {
            return null;
        }
        Map<Long, List<BackSku>> skuMap = new HashMap<>();
        List<Long> values = new ArrayList<>();
        List<Long> attIds = new ArrayList<>();
        List<Sku> skus = this.query().in("sku_id", skuIds).list();
        Map<Long, List<AttributeValues>> map = new HashMap<>();
        if (ToolUtil.isNotEmpty(skus)) {
            for (Sku sku : skus) {
                JSONArray jsonArray = JSONUtil.parseArray(sku.getSkuValue());
                List<AttributeValues> valuesRequests = JSONUtil.toList(jsonArray, AttributeValues.class);
                map.put(sku.getSkuId(), valuesRequests);
            }
            for (Long skuiId : skuIds) {
                List<AttributeValues> attributeValues = map.get(skuiId);
                if (ToolUtil.isNotEmpty(attributeValues)) {
                    for (AttributeValues attributeValue : attributeValues) {
                        values.add(attributeValue.getAttributeValuesId());
                        attIds.add(attributeValue.getAttributeId());
                    }
                }
            }
        }
        List<ItemAttribute> attributeList = attIds.size() == 0 ? new ArrayList<>() :
                itemAttributeService.query().in("attribute_id", attIds).list();
        List<AttributeValues> valuesList = values.size() == 0 ? new ArrayList<>() :
                attributeValuesService.query().in("attribute_values_id", values).list();
        for (Long skuiId : skuIds) {
            List<BackSku> backSkus = new ArrayList<>();
            List<AttributeValues> list = map.get(skuiId);
            if (ToolUtil.isNotEmpty(list)) {
                for (AttributeValues attributeValues : list) {
                    for (ItemAttribute itemAttribute : attributeList) {
                        if (itemAttribute.getAttributeId().equals(attributeValues.getAttributeId())) {
                            BackSku backSku = new BackSku();
                            backSku.setItemAttribute(itemAttribute);
                            for (AttributeValues Values : valuesList) {
                                if (Values.getAttributeValuesId().equals(attributeValues.getAttributeValuesId())) {
                                    backSku.setAttributeValues(Values);
                                }
                            }
                            backSkus.add(backSku);
                        }
                    }
                }
                skuMap.put(skuiId, backSkus);
            }

        }
        return skuMap;
    }

    private Category getOrSaveCategory(SkuParam param) {
        Category category = new Category();
        Category entity = new Category();

        if (ToolUtil.isNotEmpty(param.getSpu().getCategoryId())) {
            category = categoryService.getById(param.getSpu().getCategoryId());
        } else {
            if (ToolUtil.isEmpty(param.getSpu().getName())) {
                throw new ServiceException(500, "请传入产品名称");
            }
            String trim = param.getSpu().getName().trim(); //去空格
            QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
            queryWrapper.last("limit 1");
            queryWrapper.eq("category_name", trim);
            queryWrapper.eq("display", 1);
            category = categoryService.getOne(queryWrapper);
        }

        if (ToolUtil.isEmpty(category)) {
            entity = new Category();
            entity.setCategoryName(param.getSpu().getName().replace(" ", ""));
            categoryService.save(entity);
        } else {
            ToolUtil.copyProperties(category, entity);
        }
        return entity;

    }

    private Spu getOrSaveSpu(SkuParam param, Long spuClassificationId, Long categoryId) {
        Spu spu = new Spu();

        if (ToolUtil.isNotEmpty(param.getSpu().getSpuId())) {
            spu = spuService.lambdaQuery().eq(Spu::getSpuId, param.getSpu().getSpuId()).and(i -> i.eq(Spu::getSpuClassificationId, spuClassificationId)).and(i -> i.eq(Spu::getDisplay, 1)).one();
        } else if (ToolUtil.isNotEmpty(param.getSpu().getName())) {
            spu = spuService.lambdaQuery().eq(Spu::getName, param.getSpu().getName()).and(i -> i.eq(Spu::getSpuClassificationId, spuClassificationId)).and(i -> i.eq(Spu::getDisplay, 1)).one();
        }
        Spu spuEntity = new Spu();

        if (ToolUtil.isNotEmpty(spu)) {
            if (ToolUtil.isNotEmpty(param.getSpu().getCoding())) {
                spu.setCoding(param.getSpu().getCoding());
            }
            ToolUtil.copyProperties(spu, spuEntity);
            spuEntity.setSpuClassificationId(spuClassificationId);
            spuEntity.setUnitId(param.getUnitId());
            spuService.updateById(spuEntity);
            return spuEntity;

        } else {
            if (ToolUtil.isNotEmpty(param.getSpu().getCoding())) {
                spuEntity.setCoding(param.getSpu().getCoding());
            }
            spuEntity.setSpuClassificationId(spuClassificationId);
            spuEntity.setUnitId(param.getUnitId());
            spuEntity.setName(param.getSpu().getName());
            spuEntity.setSpuClassificationId(spuClassificationId);
            spuEntity.setCategoryId(categoryId);
            spuEntity.setType(0);
            spuService.save(spuEntity);
        }
        return spuEntity;
    }

    //    /**
//     * 查询产品 新建或返回已有产品id
//     *
//     * @param param
//     * @return
//     */
//    private Long getOrSaveSpuClass(SkuParam param) {
//        Long spuClassificationId = 0L;
//        SpuClassification spuClassification = new SpuClassification();
//
//        if (ToolUtil.isNotEmpty(param.getSpuClassification().getSpuClassificationId())){
//            spuClassification = spuClassificationService.lambdaQuery().eq(SpuClassification::getSpuClassificationId, param.getSpuClassification().getSpuClassificationId()).and(i -> i.eq(SpuClassification::getDisplay, 1)).and(i -> i.eq(SpuClassification::getType, 2)).one();
//        }else{
//            spuClassification = spuClassificationService.lambdaQuery().eq(SpuClassification::getName, param.getSpuClassification().getName()).and(i -> i.eq(SpuClassification::getDisplay, 1)).and(i -> i.eq(SpuClassification::getType, 2)).one();
//        }
//
//        if (ToolUtil.isEmpty(spuClassification)) {
//            SpuClassification spuClassificationEntity = new SpuClassification();
//            spuClassificationEntity.setName(param.getSpuClassification().getName());
//            spuClassificationEntity.setType(1L);
//            spuClassificationEntity.setPid(param.getSpuClass());
//            spuClassificationService.save(spuClassificationEntity);
//            spuClassificationId = spuClassificationEntity.getSpuClassificationId();
//        } else {
//            spuClassificationId = spuClassification.getSpuClassificationId();
//        }
//        param.setSpuClass(spuClassificationId);
//        return spuClassificationId;
//    }

    @Override
    public SkuResult getDetail(Long skuId) {

        SkuResult sku = skuService.getSku(skuId);
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
        User user = userService.getById(sku.getCreateUser());
        if (ToolUtil.isNotEmpty(user)) {
            sku.setCreateUserName(user.getName());
        }
        return sku;
    }

    @Override
    public Long addSkuFromSpu(PartsParam partsParam) {
        Sku sku = new Sku();


        if (ToolUtil.isNotEmpty(partsParam.getSpuId())) {
            Spu spu = spuService.getById(partsParam.getSpuId());
            SpuParam spuParam = new SpuParam();
            ToolUtil.copyProperties(spu, spuParam);
            SkuParam skuParam = new SkuParam();
            skuParam.setSkuName(partsParam.getSkuName());
            skuParam.setType(0);
            skuParam.setRemarks(partsParam.getSkuNote());
            skuParam.setStandard(partsParam.getStandard());
            skuParam.setSpecifications(partsParam.getSpecifications());
            skuParam.setSpuId(partsParam.getSpuId());
            skuParam.setSku(partsParam.getSku());
            skuParam.setSpu(spuParam);
            skuParam.setRemarks(partsParam.getNote());
            skuParam.setSpuClass(spuParam.getSpuClassificationId());
            if (ToolUtil.isNotEmpty(partsParam.getSkuId())) {
                skuParam.setSkuId(partsParam.getSkuId());
                skuService.update(skuParam);
                return partsParam.getSkuId();

            }
            Map<String, Sku> add = skuService.add(skuParam);
            Sku success = add.get("success");
            Long skuId = success.getSkuId();
            sku.setSkuId(skuId);
            return sku.getSkuId();
        }

        return null;
    }


    private Sku throwDuplicate(SkuParam param, Long spuId) {
        List<Sku> skus = this.query().eq("sku_name", param.getSkuName()).list();
        for (Sku sku : skus) {
            if (sku.getSpuId().equals(spuId)) {
                return sku;
            }
        }
        return null;
    }
}
