package cn.atsoft.dasheng.goods.sku.service.impl;


import cn.atsoft.dasheng.codeRule.service.RestCodeRuleService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.goods.brand.model.params.RestSkuBrandBindParam;
import cn.atsoft.dasheng.goods.brand.service.RestBrandService;
import cn.atsoft.dasheng.goods.brand.service.RestSkuBrandBindService;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.model.result.RestCategoryResult;
import cn.atsoft.dasheng.goods.category.service.RestCategoryService;
import cn.atsoft.dasheng.goods.classz.entity.RestAttribute;
import cn.atsoft.dasheng.goods.classz.entity.RestAttributeValues;
import cn.atsoft.dasheng.goods.classz.model.RestAttributes;
import cn.atsoft.dasheng.goods.classz.model.RestSkuAttributeAndValue;
import cn.atsoft.dasheng.goods.classz.model.RestValues;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeParam;
import cn.atsoft.dasheng.goods.classz.model.params.RestAttributeValuesParam;
import cn.atsoft.dasheng.goods.classz.model.params.RestClassParam;
import cn.atsoft.dasheng.goods.classz.model.result.RestAttributeAddResult;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeService;
import cn.atsoft.dasheng.goods.classz.service.RestAttributeValuesService;
import cn.atsoft.dasheng.goods.classz.service.RestClassService;
import cn.atsoft.dasheng.goods.sku.entity.RestSku;
import cn.atsoft.dasheng.goods.sku.mapper.RestSkuMapper;
import cn.atsoft.dasheng.goods.sku.model.RestSkuJson;
import cn.atsoft.dasheng.goods.sku.model.params.RestSkuParam;
import cn.atsoft.dasheng.goods.sku.model.params.SkuListParam;
import cn.atsoft.dasheng.goods.sku.model.result.RestSkuResult;
import cn.atsoft.dasheng.goods.sku.model.result.SkuListResult;
import cn.atsoft.dasheng.goods.sku.service.RestSkuService;
import cn.atsoft.dasheng.goods.spu.entity.RestSpu;
import cn.atsoft.dasheng.goods.spu.model.params.RestSpuParam;
import cn.atsoft.dasheng.goods.spu.model.result.RestSpuResult;
import cn.atsoft.dasheng.goods.spu.service.RestSpuService;
import cn.atsoft.dasheng.goods.texture.model.result.RestTextrueResult;
import cn.atsoft.dasheng.goods.texture.service.RestTextrueService;
import cn.atsoft.dasheng.goods.unit.entity.RestUnit;
import cn.atsoft.dasheng.goods.unit.model.result.RestUnitResult;
import cn.atsoft.dasheng.goods.unit.service.RestUnitService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.sys.modular.system.entity.Dict;
import cn.atsoft.dasheng.sys.modular.system.service.DictService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * sku表	 服务实现类
 * </p>
 *
 * @author
 * @since 2021-10-18
 */
@Service
public class RestSkuServiceImpl extends ServiceImpl<RestSkuMapper, RestSku> implements RestSkuService {
    @Autowired
    private RestSpuService spuService; //spu

    @Autowired
    private RestClassService  classService; //类目

    @Autowired
    private RestAttributeValuesService attributeValuesService; //属性

    @Autowired
    private RestAttributeService itemAttributeService; //数值

    @Autowired
    private RestSkuService skuService; //sku

    @Autowired
    private RestCodeRuleService codingRulesService; //编码规则

    @Autowired
    private RestCategoryService spuClassificationService;

    @Autowired
    private RestUnitService unitService; //单位

    @Autowired
    private RestSkuBrandBindService skuBrandBindService; //品牌绑定

    @Autowired
    private RestBrandService brandService; //品牌


    @Autowired
    private DictService dictService; //基础字典

    @Autowired
    private RestTextrueService materialService; //材质

    @Autowired
    private RestAttributeService attributeService; //材质


//    @Autowired
//    private StockForewarnService stockForewarnService; //库存预警


    @Transactional(propagation = Propagation.REQUIRED, timeout = 90)

//    @Override
    public Map<String, RestSku> add(RestSkuParam param) {

        RestSku result = new RestSku();
        Long skuId = null;

        /**
         * type=1 是整机添加
         */
        if (param.getType() == 0) {
            /**
             * 查询分类  添加分类
             */


//            Long spuClassificationId = this.getOrSaveSpuClass(param);
//            RestCategory spuClassification = spuClassificationService.getById(param.getSpuClass());
            RestCategory spuClassification = spuClassificationService.getById(param.getSpuClass());

//            Integer parentSpuClassifications = spuClassificationService.query().eq("pid", spuClassification.getSpuClassificationId()).eq("display", 1).count();
//            if (parentSpuClassifications > 0) {
//                throw new ServiceException(500, "物料必须添加在最底级分类中");
//            }
            Long spuClassificationId = 0L;
            if (!param.getSpuClass().equals(0L) && ToolUtil.isNotEmpty(spuClassification)){
                spuClassificationId =  spuClassification.getSpuClassificationId();
            }
            /**
             * sku名称（skuName）加型号(spuName)判断防止重复
             */
            param.setRestClass(new RestClassParam(){{
                setName(param.getSpu().getName().trim());
            }});
            Long classId = this.classService.add(param.getRestClass());
            param.getSpu().setSpuClassificationId(spuClassificationId);
            param.getSpu().setUnitId(param.getUnitId());
            Long spuId = this.spuService.add(param.getSpu());

//            同分类，同产品下不可有同名物料
            Dict md5FlagDict = dictService.query().eq("code", "skuMd5").one();
            boolean md5Flag = ToolUtil.isNotEmpty(md5FlagDict) && md5FlagDict.getStatus().equals("ENABLE");
            /**
             *  查询同名的sku 并返回提示
             */
            RestSku sku = this.throwDuplicate(param,spuId);
            if (ToolUtil.isNotEmpty(sku) && md5Flag) {
                {
                    return new HashMap<String, RestSku>() {{
                        put("error", sku);
                    }};
                }
            }


            //生成编码
            if (ToolUtil.isEmpty(param.getStandard())) {
                    String backCoding = codingRulesService.backCoding("sku", param);
                    param.setStandard(backCoding);
                    param.setCoding(backCoding);
            }
            /**
             * 判断成品码是否重复
             */
            int count = skuService.count(new QueryWrapper<RestSku>() {{
                eq("standard", param.getStandard());
                eq("display", 1);
            }});
            if (count > 0) {
                throw new ServiceException(500, "编码/成品码重复");
            }


            /**
             * 做匹配保存 属性属性值方法
             *
             */
            List<RestAttributeValues> list = ToolUtil.isEmpty(param.getSkuAttributeAndValues()) ? new ArrayList<>() : this.addAttributeAndValue(param.getSkuAttributeAndValues(), classId);

            RestSku entity = getEntity(param);

            list.sort(Comparator.comparing(RestAttributeValues::getAttributeId));
            String json = JSON.toJSONString(list);

            entity.setSpuId(spuId);
            entity.setSkuValue(json);

            String md5 = SecureUtil.md5(entity.getSkuValue() + entity.getSpuId().toString() + entity.getSkuName() + spuClassificationId);

            entity.setSkuValueMd5(md5);
            if (md5Flag) {
                Integer skuCount = this.lambdaQuery().eq(RestSku::getSkuValueMd5, md5).and(i -> i.eq(RestSku::getDisplay, 1).ne(RestSku::getSkuId, entity.getSkuId())).count();
                if (skuCount > 0) {
                    throw new ServiceException(500, "该物料已经存在");
                }
            }
            this.save(entity);
            skuId = entity.getSkuId();
            ToolUtil.copyProperties(entity, result);
            /**
             * 绑定品牌（多个）
             */
            if (ToolUtil.isNotEmpty(param.getBrandIds())) {
                skuBrandBindService.addBatch(new RestSkuBrandBindParam() {{
                    setBrandIds(param.getBrandIds());
                    setSkuId(entity.getSkuId());
                }});
            }

        } else if (param.getType() == 1) {
            Long spuId = param.getSpu().getSpuId();
            if (ToolUtil.isEmpty(spuId)) {
                spuId = spuService.lambdaQuery().eq(RestSpu::getName, param.getSpu().getName()).and(i -> i.eq(RestSpu::getDisplay, 1)).one().getSpuId();
            }

            if (spuId == null) {
                RestSpuParam spu = new RestSpuParam();
                spu.setName(param.getSpu().getName());
                spu.setSpuClassificationId(param.getSpuClassificationId());
                spu.setIsHidden(false);
                spu.setType(0);
                spuId = spuService.add(spu);
            } else {
                param.getSpuAttribute().getSpuRequests().sort(Comparator.comparing(RestAttributes::getAttributeId));
                List<RestAttributeValues> list = new ArrayList<>();
                for (RestAttributes spuRequest : param.getSpuAttribute().getSpuRequests()) {
                    RestAttributeValues attributeValueResult = new RestAttributeValues();
                    attributeValueResult.setAttributeId(Long.valueOf(spuRequest.getAttributeId()));
                    for (RestValues attributeValue : spuRequest.getAttributeValues()) {
                        attributeValueResult.setAttributeValuesId(Long.valueOf(attributeValue.getAttributeValuesId()));
                    }
                    list.add(attributeValueResult);
                }
                RestSku entity = getEntity(param);
                entity.setSpuId(spuId);
                String json = JSON.toJSONString(list);
                entity.setSkuValue(json);
//                entity.setSkuValue(spuId + "," + json);
                String md5 = SecureUtil.md5(entity.getSkuValue() + entity.getSpuId().toString() + entity.getSkuName() + param.getSpuClassificationId());
                entity.setSkuValueMd5(md5);
                Dict md5FlagDict = dictService.query().eq("code", "skuMd5").one();
                boolean md5Flag = ToolUtil.isNotEmpty(md5FlagDict) && md5FlagDict.getStatus().equals("ENABLE");
                if (md5Flag) {
                    Integer skuCount = skuService.lambdaQuery().eq(RestSku::getSkuValueMd5, md5).and(i -> i.eq(RestSku::getDisplay, 1).ne(RestSku::getSkuId, entity.getSkuId())).count();
                    if (skuCount > 0) {
                        throw new ServiceException(500, "该物料已经存在");
                    }
                }
                /**
                 * //原 SKU防重复判断
                 *
                 *
                 *  ↓为新sku防止重复判断  以名称加型号 做数据库比对判断
                 */
                this.save(entity);
//                TODO 养护周期
//                if (ToolUtil.isNotEmpty(param.getMaintenancePeriod())) {
//                    MaintenanceCycle maintenanceCycle = new MaintenanceCycle();
//                    maintenanceCycle.setSkuId(entity.getSkuId());
//                    maintenanceCycle.setMaintenancePeriod(param.getMaintenancePeriod());
//                    maintenanceCycleService.save(maintenanceCycle);
//                }
                skuId = entity.getSkuId();
                if(param.getInitialNumber()>0){

                }

                ToolUtil.copyProperties(entity, result);
//                /**
//                 * 绑定品牌（多个）
//                 */
                if (ToolUtil.isNotEmpty(param.getBrandIds())) {
                    skuBrandBindService.addBatch(new RestSkuBrandBindParam() {{
                        setBrandIds(param.getBrandIds());
                        setSkuId(entity.getSkuId());
                    }});
                }
//            }

            }
//        if (ToolUtil.isNotEmpty(param.getOldSkuId())) {
//            ActivitiProcess activitiProcess = processService.query().eq("form_id", param.getOldSkuId()).eq("type", "ship").eq("display", 1).one();
//            if (ToolUtil.isNotEmpty(activitiProcess)) {
//                copyProcessRoute(param.getOldSkuId(), skuId);
//            } else {
//                copySkuBomById(param.getOldSkuId(), skuId);
//            }
//        }
        }
        return new HashMap<String, RestSku>() {{
            put("success", result);
        }};
    }



    @Transactional
    public List<RestAttributeValues> addAttributeAndValue(List<RestSkuAttributeAndValue> param, Long categoryId) {
        List<String> attributeName = new ArrayList<>();
        List<Long> attributeIds = new ArrayList<>();
        for (RestSkuAttributeAndValue RestSkuAttributeAndValue : param) {
            attributeName.add(RestSkuAttributeAndValue.getLabel());
            if (ToolUtil.isEmpty(RestSkuAttributeAndValue.getLabel()) || RestSkuAttributeAndValue.getLabel().replace(" ", "").length() == 0) {
                throw new ServiceException(500, "规格名称不可为空或空格");
            }
            if (ToolUtil.isEmpty(RestSkuAttributeAndValue.getValue()) || RestSkuAttributeAndValue.getValue().replace(" ", "").length() == 0) {
                throw new ServiceException(500, "规格值不可为空或空格");
            }
        }
        List<RestAttribute> attributes = attributeName.size() == 0 ? new ArrayList<>() : itemAttributeService.lambdaQuery().in(RestAttribute::getAttribute, attributeName).and(i -> i.eq(RestAttribute::getCategoryId, categoryId)).and(i -> i.isNotNull(RestAttribute::getAttribute)).and(i -> i.eq(RestAttribute::getDisplay, 1)).list();
        for (RestAttribute attribute : attributes) {
            attributeIds.add(attribute.getAttributeId());
        }
//        List<RestAttributeValues> attributeValues = attributeIds.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery().in(RestAttributeValues::getAttributeId, attributeIds).and(i -> i.isNotNull(RestAttributeValues::getAttributeValues)).and(i -> i.eq(RestAttributeValues::getDisplay, 1)).list();
        List<RestAttributeValues> list = new ArrayList<>();
        for (RestSkuAttributeAndValue RestSkuAttributeAndValue : param) {
            if (ToolUtil.isNotEmpty(RestSkuAttributeAndValue)) {
                RestAttributeValues value = new RestAttributeValues();
//                if (ToolUtil.isNotEmpty(RestSkuAttributeAndValue.getLabel()) && attributes.size() > 0 && attributes.stream().anyMatch(attribute -> attribute.getAttribute().equals(RestSkuAttributeAndValue.getLabel()))) {
                /**
                 * 添加属性
                 */
                RestAttributeAddResult addResult = attributeService.add(new RestAttributeParam() {{
                    setAttribute(RestSkuAttributeAndValue.getLabel());
                    setClassId(categoryId);
                    setAttributeValuesParams(new ArrayList<RestAttributeValuesParam>() {{
                        add(new RestAttributeValuesParam() {{
                            setAttributeValues(RestSkuAttributeAndValue.getValue());
                        }});
                    }});
                }});
                list.addAll(addResult.getAttributeValues());
//                value.setAttributeId(attributeId);
//                if (attributes.size() > 0 && attributes.stream().anyMatch(attribute -> attribute.getAttribute().equals(RestSkuAttributeAndValue.getLabel()))) {
//                    for (RestAttribute itemAttribute : attributes) {
//                        if (RestSkuAttributeAndValue.getLabel().equals(itemAttribute.getAttribute())) {
//                            value.setAttributeId(itemAttribute.getAttributeId());
//                        }
//                    }
//                } else {
//                    RestAttribute itemAttributeEntity = new RestAttribute();
//                    itemAttributeEntity.setAttribute(RestSkuAttributeAndValue.getLabel());
//                    itemAttributeEntity.setCategoryId(categoryId);
//                    itemAttributeService.save(itemAttributeEntity);
//                    value.setAttributeId(itemAttributeEntity.getAttributeId());
//
//                }
//                if (ToolUtil.isNotEmpty(RestSkuAttributeAndValue.getValue()) && attributes.size() > 0 && attributeValues.stream().anyMatch(attributeValue -> attributeValue.getAttributeValues().equals(RestSkuAttributeAndValue.getValue()) && attributeValue.getAttributeId().equals(value.getAttributeId()))) {
//                    for (RestAttributeValues attributeValue : attributeValues) {
//                        if (RestSkuAttributeAndValue.getValue().equals(attributeValue.getAttributeValues()) && attributeValue.getAttributeId().equals(value.getAttributeId())) {
//                            value.setAttributeValuesId(attributeValue.getAttributeValuesId());
//                        }
//                    }
//                } else {
//                    RestAttributeValues attributeValuesEntity = new RestAttributeValues();
//                    attributeValuesEntity.setAttributeId(value.getAttributeId());
//                    attributeValuesEntity.setAttributeValues(RestSkuAttributeAndValue.getValue());
//                    attributeValuesService.save(attributeValuesEntity);
//                    value.setAttributeValuesId(attributeValuesEntity.getAttributeValuesId());
//                }
//                list.add(value);
            }
        }
        list.sort(Comparator.comparing(RestAttributeValues::getAttributeId));
        return list;
    }


        @Override
        public void format (List < RestSkuResult > param) {

            List<Long> spuIds = new ArrayList<>();
            List<Long> attributeIds = new ArrayList<>();
            List<Long> userIds = new ArrayList<>();
            List<Long> skuIds = new ArrayList<>();
            List<Long> materialIds = new ArrayList<>();

            for (RestSkuResult skuResult : param) {
                skuIds.add(skuResult.getSkuId());
                spuIds.add(skuResult.getSpuId());
                userIds.add(skuResult.getCreateUser());
                JSONArray jsonArray = JSONUtil.parseArray(skuResult.getSkuValue());
                List<RestAttributeValues> valuesRequests = JSONUtil.toList(jsonArray, RestAttributeValues.class);
                for (RestAttributeValues valuesRequest : valuesRequests) {
                    attributeIds.add(valuesRequest.getAttributeId());
                }
//                if (ToolUtil.isNotEmpty(skuResult.getMaterialId())) {
//                    List<Long> materialIdList = JSON.parseArray(skuResult.getMaterialId(), Long.class);
//                    skuResult.setMaterialIdList(materialIdList);
//                    materialIds.addAll(materialIdList);
//                }
            }
//        List<MaintenanceCycle> maintenanceCycles = skuIds.size() == 0 ? new ArrayList<>() : maintenanceCycleService.query().in("sku_id", skuIds).eq("display", 1).list();
//        List<StockForewarn> stockForewarns = skuIds.size() == 0 ? new ArrayList<>() : stockForewarnService.lambdaQuery().eq(StockForewarn::getType,"sku").in(StockForewarn::getFormId,skuIds).eq(StockForewarn::getDisplay,1).list();
            List<RestAttribute> itemAttributes = itemAttributeService.lambdaQuery().list();
            List<RestAttributeValues> attributeValues = attributeIds.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery()
                    .in(RestAttributeValues::getAttributeId, attributeIds)
                    .list();
            List<RestSpu> spus = spuIds.size() == 0 ? new ArrayList<>() : spuService.query().in("spu_id", spuIds).list();
            List<Long> unitIds = new ArrayList<>();
            List<Long> spuClassId = new ArrayList<>();
            Map<Long, RestUnitResult> unitMaps = new HashMap<>();
            Map<Long, RestCategoryResult> spuClassificationMap = new HashMap<>();
//        List<User> users = userIds.size() == 0 ? new ArrayList<>() : userService.listByIds(userIds);
            for (RestSpu spu : spus) {
                if (ToolUtil.isNotEmpty(spu.getUnitId())) {
                    unitIds.add(spu.getUnitId());
                    spuClassId.add(spu.getSpuClassificationId());
                }
            }

            List<RestUnit> units = unitIds.size() == 0 ? new ArrayList<>() : unitService.query().in("unit_id", unitIds).eq("display", 1).list();
            List<RestCategory> spuClassifications = spuClassId.size() == 0 ? new ArrayList<>() : spuClassificationService.query().in("spu_classification_id", spuClassId).list();

            for (RestSpu spu : spus) {
                if (ToolUtil.isNotEmpty(units)) {
                    for (RestUnit unit : units) {
                        if (spu.getUnitId() != null && spu.getUnitId().equals(unit.getUnitId())) {
                            RestUnitResult unitResult = new RestUnitResult();
                            ToolUtil.copyProperties(unit, unitResult);
                            unitMaps.put(spu.getSpuId(), unitResult);
                        }
                    }
                    for (RestCategory spuClassification : spuClassifications) {
                        if (spu.getSpuClassificationId() != null && spu.getSpuClassificationId().equals(spuClassification.getSpuClassificationId())) {
                            RestCategoryResult classification = new RestCategoryResult();
                            ToolUtil.copyProperties(spuClassification, classification);
                            spuClassificationMap.put(spu.getSpuId(), classification);
                        }
                    }
                }
            }
//
//        /**
//         * 查询品牌
//         */
//        List<SkuBrandBind> skuBrandBinds = skuIds.size() == 0 ? new ArrayList<>() : skuBrandBindService.query().in("sku_id", skuIds).eq("display", 1).list();
//
//        Map<Long, List<Long>> brandMapIds = new HashMap<>();
//        List<Long> brandIds = new ArrayList<>();
//        for (SkuBrandBind bind : skuBrandBinds) {
//            brandIds.add(bind.getBrandId());
//            List<Long> list = brandMapIds.get(bind.getSkuId());
//            if (ToolUtil.isEmpty(list)) {
//                list = new ArrayList<>();
//            }
//            list.add(bind.getBrandId());
//            brandMapIds.put(bind.getSkuId(), list);
//        }
//        List<RestBrandResult> brandResults = brandService.getBrandResults(brandIds);
//
//        /**
//         * 查询清单
//         */
//        List<Parts> parts = skuIds.size() == 0 ? new ArrayList<>() : partsService.query().in("sku_id", skuIds).eq("display", 1).eq("status", 99).list();
//        List<ActivitiProcess> processes = skuIds.size() == 0 ? new ArrayList<>() : processService.query().in("form_id", skuIds).eq("type", "ship").eq("display", 1).list();
//        /**
//         * 库存数
//         */
//        List<StockDetails> stockDetailsList = stockDetailsService.query().select("sku_id, sum(number) as num").eq("display", 1).groupBy("sku_id").list();
//        /**
//         * 查询已占用库存数
//         */
//        List<StockDetails> lockStockDetail = pickListsCartService.getLockStockDetail();
//
            List<RestTextrueResult> materialResults = materialService.details(materialIds);

            for (RestSkuResult skuResult : param) {
                /**
                 * 材质
                 */
                List<RestTextrueResult> materialResultList = new ArrayList<>();
                if (ToolUtil.isNotEmpty(skuResult.getMaterialIdList())) {
                    for (Long materialId : skuResult.getMaterialIdList()) {
                        for (RestTextrueResult materialResult : materialResults) {
                            if (materialResult.getMaterialId().equals(materialId)) {
                                materialResultList.add(materialResult);
                            }
                        }
                    }
                }
                skuResult.setMaterialResultList(materialResultList);

//            //图片
//            List<Long> imageIds = new ArrayList<>();
//            if (ToolUtil.isNotEmpty(skuResult.getImages())) {
//                List<MediaResult> results = strToMediaResults(skuResult.getImages());
//                skuResult.setImgResults(results);
//            }
//
//            for (MaintenanceCycle maintenanceCycle : maintenanceCycles) {
//                if (skuResult.getSkuId().equals(maintenanceCycle.getSkuId())) {
//                    skuResult.setMaintenancePeriod(maintenanceCycle.getMaintenancePeriod());
//                }
//            }
//            List<RestBrandResult> brandResultList = new ArrayList<>();
//            List<Long> allBrandIds = brandMapIds.get(skuResult.getSkuId());
//            if (ToolUtil.isNotEmpty(allBrandIds)) {
//                for (Long allBrandId : allBrandIds) {
//                    for (RestBrandResult brandResult : brandResults) {
//                        if (allBrandId.equals(brandResult.getBrandId())) {
//                            brandResultList.add(brandResult);
//                        }
//                    }
//                }
//                skuResult.setBrandResults(brandResultList);
//            }
//
//
//            for (StockDetails stockDetails : stockDetailsList) {
//                if (skuResult.getSkuId().equals(stockDetails.getSkuId())) {
//                    skuResult.setStockNumber(Math.toIntExact(stockDetails.getNum()));
//                }
//            }
//
//            for (StockDetails stockDetails : lockStockDetail) {
//                if (stockDetails.getSkuId().equals(skuResult.getSkuId())) {
//                    skuResult.setLockStockDetailNumber(Math.toIntExact(stockDetails.getNumber()));
//                }
//            }
//
////            for (Long imageid : imageIds) {
////                imageUrls.add(mediaService.getMediaUrl(imageid, 1L));
////                String imgThumbUrl = mediaService.getMediaUrlAddUseData(imageid, 0L, "image/resize,m_fill,h_200,w_200");
////                imgThumbUrls.add(imgThumbUrl);
////            }
//
////            skuResult.setImgUrls(imageUrls);
////            skuResult.setImgThumbUrls(imgThumbUrls);
//
////            for (StockDetails stockDetails : totalLockDetail) {
////                if (stockDetails.getSkuId().equals(skuResult.getSkuId())) {
////                    skuResult.setLockStockDetailNumber(Math.toIntExact(stockDetails.getNumber()));
////                }
////            }
//
//
//            for (ActivitiProcess process : processes) {
//                if (process.getFormId().equals(skuResult.getSkuId())) {
//                    ActivitiProcessResult processResult = new ActivitiProcessResult();
//                    ToolUtil.copyProperties(process, processResult);
//                    skuResult.setProcessResult(processResult);
//                }
//            }
//            skuResult.setInBom(false);
//
//            for (Parts part : parts) {
//                if (part.getSkuId().equals(skuResult.getSkuId())) {
//                    skuResult.setInBom(true);
//                    skuResult.setPartsId(part.getPartsId());
//                    skuResult.setPartsResult(BeanUtil.copyProperties(part, PartsResult.class));
//                    break;
//                }
//            }
//            for (User user : users) {
//                if (ToolUtil.isNotEmpty(skuResult.getCreateUser()) && user.getUserId().equals(skuResult.getCreateUser())) {
//                    skuResult.setUser(user);
//                    break;
//                }
//            }
//
            if (ToolUtil.isNotEmpty(spus)) {
                for (RestSpu spu : spus) {
                    if (spu.getSpuId() != null && skuResult.getSpuId() != null && spu.getSpuId().equals(skuResult.getSpuId())) {
                        RestSpuResult spuResult = new RestSpuResult();
                        ToolUtil.copyProperties(spu, spuResult);
                        RestUnitResult unitResult = unitMaps.get(spu.getSpuId());
                        RestCategoryResult spuClassificationResult = spuClassificationMap.get(spu.getSpuId());
                        if (ToolUtil.isNotEmpty(unitResult)) {
                            spuResult.setUnitResult(unitResult);
                        }
//                        if (ToolUtil.isNotEmpty(spuClassificationResult)) {
//                            spuResult.setSpuClassificationResult(spuClassificationResult);
//                        }
                        skuResult.setSpuResult(spuResult);
                        break;
                    }
                }
            }
//
                JSONArray jsonArray = JSONUtil.parseArray(skuResult.getSkuValue());
                List<RestAttributeValues> valuesRequests = JSONUtil.toList(jsonArray, RestAttributeValues.class);
                List<RestSkuJson> list = new ArrayList<>();
                for (RestAttributeValues valuesRequest : valuesRequests) {
                    if (ToolUtil.isNotEmpty(valuesRequest.getAttributeId()) && ToolUtil.isNotEmpty(valuesRequest.getAttributeValuesId())) {
                        RestSkuJson skuJson = new RestSkuJson();
                        for (RestAttribute itemAttribute : itemAttributes) {
                            if (itemAttribute.getAttributeId().equals(valuesRequest.getAttributeId())) {
                                RestAttributes attribute = new RestAttributes();
                                attribute.setAttributeId(itemAttribute.getAttributeId().toString());
                                attribute.setAttribute(itemAttribute.getAttribute());
                                skuJson.setAttribute(attribute);
                            }
                        }
                        for (RestAttributeValues attributeValue : attributeValues) {
                            if (valuesRequest.getAttributeValuesId().equals(attributeValue.getAttributeValuesId())) {
                                RestValues values = new RestValues();
                                values.setAttributeValuesId(valuesRequest.getAttributeValuesId().toString());
                                values.setAttributeValues(attributeValue.getAttributeValues());
                                skuJson.setValues(values);
                            }
                        }
                        list.add(skuJson);
                    }
                    skuResult.setSkuJsons(list);
                }
//            for (StockForewarn stockForewarn : stockForewarns) {
//                if(stockForewarn.getFormId().equals(skuResult.getSkuId())){
//                    skuResult.setStockForewarnResult(BeanUtil.copyProperties(stockForewarn,StockForewarnResult.class));
//                    break;
//                }
//            }
            }
//
//
        }

    @Override
    public List<RestSku> skuResultBySpuId(Long spuId) {
        List<RestSku> results = this.baseMapper.restSkuResultBySpuId(spuId);
        return results;
    }

    @Override
    public List<RestSku> getByIds(List<Long> skuIds) {
        skuIds.add(0L);
        return this.baseMapper.getByIds(skuIds);
    }

    @Override
    public List<RestSkuResult> formatSkuResult(List<Long> skuIds) {
        if (ToolUtil.isEmpty(skuIds)) {
            return new ArrayList<>();
        }
        List<RestSku> skus = this.listByIds(skuIds);
        List<RestSkuResult> skuResults = new ArrayList<>();
        for (RestSku sku : skus) {
            RestSkuResult skuResult = new RestSkuResult();
            ToolUtil.copyProperties(sku, skuResult);
            skuResults.add(skuResult);
        }
        this.format(skuResults);

        return skuResults;
    }

        private RestSku getEntity (RestSkuParam param){
            RestSku entity = new RestSku();
            ToolUtil.copyProperties(param, entity);
            return entity;
        }

        private RestSpu getOrSaveSpu (RestSkuParam param, Long spuClassificationId, Long categoryId){
            RestSpu spu = new RestSpu();

            if (ToolUtil.isNotEmpty(param.getSpu().getSpuId())) {
                spu = spuService.lambdaQuery().eq(RestSpu::getSpuId, param.getSpu().getSpuId()).and(i -> i.eq(RestSpu::getSpuClassificationId, spuClassificationId)).and(i -> i.eq(RestSpu::getDisplay, 1)).one();
            } else if (ToolUtil.isNotEmpty(param.getSpu().getName())) {
                spu = spuService.lambdaQuery().eq(RestSpu::getName, param.getSpu().getName()).and(i -> i.eq(RestSpu::getSpuClassificationId, spuClassificationId)).and(i -> i.eq(RestSpu::getDisplay, 1)).one();
            }
            RestSpu spuEntity = new RestSpu();

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



        private RestSku throwDuplicate (RestSkuParam param, Long spuId){
            List<RestSku> skus = this.query().eq("sku_name", param.getSkuName()).list();
            for (RestSku sku : skus) {
                if (sku.getSpuId().equals(spuId)) {
                    return sku;
                }
            }
            return null;
        }

    @Override
    public List<SkuListResult> viewResultsByIds(List<Long> ids) {
        List<SkuListResult> skuListResults =this.baseMapper.customListBySkuView(new SkuListParam(){{
            setSkuIds(ids);
        }});

        viewFormat(skuListResults);


        return skuListResults;

    }

    public void viewFormat(List<SkuListResult> dataList){
        List<Long> attributeIds = new ArrayList<>();

        for (SkuListResult skuListResult : dataList) {
            JSONArray jsonArray = JSONUtil.parseArray(skuListResult.getSkuValue());
            List<RestAttributeValues> valuesRequests = JSONUtil.toList(jsonArray, RestAttributeValues.class);
            for (RestAttributeValues valuesRequest : valuesRequests) {
                attributeIds.add(valuesRequest.getAttributeId());
            }
        }
        List<RestAttribute> itemAttributes = itemAttributeService.lambdaQuery().list();

        List<RestAttributeValues> attributeValues = attributeIds.size() == 0 ? new ArrayList<>() : attributeValuesService.lambdaQuery()
                .in(RestAttributeValues::getAttributeId, attributeIds)
                .list();
        for (SkuListResult skuListResult : dataList) {
            JSONArray jsonArray = JSONUtil.parseArray(skuListResult.getSkuValue());
            List<RestAttributeValues> valuesRequests = JSONUtil.toList(jsonArray, RestAttributeValues.class);
            List<RestSkuJson> list = new ArrayList<>();
            for (RestAttributeValues valuesRequest : valuesRequests) {
                if (ToolUtil.isNotEmpty(valuesRequest.getAttributeId()) && ToolUtil.isNotEmpty(valuesRequest.getAttributeValuesId())) {
                    RestSkuJson skuJson = new RestSkuJson();
                    for (RestAttribute itemAttribute : itemAttributes) {
                        if (itemAttribute.getAttributeId().equals(valuesRequest.getAttributeId())) {
                            RestAttributes attribute = new RestAttributes();
                            attribute.setAttributeId(itemAttribute.getAttributeId().toString());
                            attribute.setAttribute(itemAttribute.getAttribute());
                            skuJson.setAttribute(attribute);
                        }
                    }
                    for (RestAttributeValues attributeValue : attributeValues) {
                        if (valuesRequest.getAttributeValuesId().equals(attributeValue.getAttributeValuesId())) {
                            RestValues values = new RestValues();
                            values.setAttributeValuesId(valuesRequest.getAttributeValuesId().toString());
                            values.setAttributeValues(attributeValue.getAttributeValues());
                            skuJson.setValues(values);
                        }
                    }
                    list.add(skuJson);
                }
                skuListResult.setSkuJsons(list);
            }
        }
    }
    @Override
    public Boolean skuCheck(RestSkuParam param, Long skuId){
        List<RestSkuResult> restSkuResults = this.formatSkuResult(new ArrayList<Long>() {{
            add(skuId);
        }});
        if (restSkuResults.size() == 0 ){
            throw new ServiceException(500,"id未找到");
        }
        RestSkuResult restSkuResult = restSkuResults.get(0);
        RestSkuParam restSkuParam = BeanUtil.copyProperties(restSkuResult, RestSkuParam.class);

        Boolean flag = true;






    return flag;


    }
}
