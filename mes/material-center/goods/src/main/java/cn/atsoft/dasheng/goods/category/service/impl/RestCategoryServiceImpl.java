package cn.atsoft.dasheng.goods.category.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.core.util.ToolUtil;

import cn.atsoft.dasheng.form.entity.RestFormStyle;
import cn.atsoft.dasheng.form.service.RestFormStyleService;
import cn.atsoft.dasheng.goods.category.entity.RestCategory;
import cn.atsoft.dasheng.goods.category.mapper.RestCategoryMapper;
import cn.atsoft.dasheng.goods.category.model.params.RestCategoryParam;
import cn.atsoft.dasheng.goods.category.model.result.RestCategoryResult;
import cn.atsoft.dasheng.goods.category.service.RestCategoryService;
import cn.atsoft.dasheng.goods.spu.entity.RestSpu;
import cn.atsoft.dasheng.goods.spu.service.RestSpuService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * SPU分类 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-25
 */
@Service
public class RestCategoryServiceImpl extends ServiceImpl<RestCategoryMapper, RestCategory> implements RestCategoryService {

    @Autowired
    private RestSpuService spuService;

    @Autowired
    private RestFormStyleService formStyleService;

    @Override
    @Transactional
    public Long add(RestCategoryParam param) {
        Integer count = this.lambdaQuery().in(RestCategory::getDisplay, 1).in(RestCategory::getName, param.getName()).and(i -> i.eq(RestCategory::getType, 1)).count();
        if (count > 0) {
            throw new ServiceException(500, "名字已重复");
        }
        if (ToolUtil.isNotEmpty(param.getPid())) {
            Integer num = this.query().eq("pid", param.getPid()).eq("type", 2).eq("display", 1).count();
            if (num > 0) {
                throw new ServiceException(500, "当前分类下已有产品，不可创建");
            }
            Integer spuCount = spuService.query().eq("spu_classification_id", param.getPid()).eq("display", 1).count();
            if (spuCount > 0) {
                throw new ServiceException(500, "父级分类下已存在产品,不可添加");
            }

        }
        RestCategory entity = getEntity(param);
        this.save(entity);

        //分类标单排版
        if (ToolUtil.isNotEmpty(param.getTypeSetting())) {
            RestFormStyle formStyle = new RestFormStyle();
            formStyle.setTypeSetting(param.getTypeSetting());
            formStyle.setFormType("spuClass");
            formStyleService.save(formStyle);
            entity.setFormStyleId(formStyle.getStyleId());
            this.updateById(entity);
        }


        return entity.getSpuClassificationId();

    }

    @Override

    public void delete(RestCategoryParam param) {
        Integer children = this.query().eq("pid", param.getSpuClassificationId()).eq("display", 1).count();
        if (children > 0) {
            throw new ServiceException(500, "此分类下有下级,无法删除");
        } else {
            RestCategory spuClassification = new RestCategory();
            spuClassification.setSpuClassificationId(param.getSpuClassificationId());
            spuClassification.setDisplay(0);
            this.updateById(spuClassification);
        }
//        this.removeById(getKey(param));
    }

    @Override
    @Transactional
    public void update(RestCategoryParam param) {

        RestCategory oldEntity = getOldEntity(param);
        RestCategory newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);

        if (ToolUtil.isNotEmpty(param.getTypeSetting())) {
            RestFormStyle formStyle = null;
            if (ToolUtil.isNotEmpty(newEntity.getFormStyleId())) {
                formStyle = formStyleService.getById(newEntity.getFormStyleId());
                formStyle.setTypeSetting(param.getTypeSetting());
                formStyleService.updateById(formStyle);
            } else {
                formStyle = new RestFormStyle();
                formStyle.setTypeSetting(param.getTypeSetting());
                formStyleService.save(formStyle);
                newEntity.setFormStyleId(formStyle.getStyleId());
                this.updateById(newEntity);
            }
        }
    }

    /**
     * 递归
     */
    public Map<String, List<Long>> getChildrens(Long id) {

        List<Long> childrensSkuIds = new ArrayList<>();
        Map<String, List<Long>> result = new HashMap<String, List<Long>>() {
            {
                put("children", new ArrayList<>());
                put("childrens", new ArrayList<>());
            }
        };

        List<Long> skuIds = new ArrayList<>();
        RestCategory classification = this.query().eq("spu_classification_id", id).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(classification)) {
            List<RestCategory> details = this.query().eq("pid", classification.getSpuClassificationId()).eq("display", 1).list();
            for (RestCategory detail : details) {
                skuIds.add(detail.getSpuClassificationId());
                childrensSkuIds.add(detail.getSpuClassificationId());
                Map<String, List<Long>> childrenMap = this.getChildrens(detail.getSpuClassificationId());
                childrensSkuIds.addAll(childrenMap.get("childrens"));
            }
            result.put("children", skuIds);
            result.put("childrens", childrensSkuIds);
        }
        return result;
    }

    /**
     * 获取所有最下级分类
     *
     * @return
     */
    @Override
    public List<String> getClassName() {
        List<String> names = new ArrayList<>();

        List<RestCategory> classifications = this.query().eq("display", 1).eq("type", 1).list();
        List<RestCategoryResult> results = BeanUtil.copyToList(classifications, RestCategoryResult.class, new CopyOptions());

        for (RestCategoryResult result : results) {
            if (result.getPid() == 0) {
                getChild(result, results);
            }
        }
        for (RestCategoryResult result : results) {
            if (result.isMostJunior()) {
                names.add(result.getName());
            }
        }
        return names;
    }


    /**
     * MostJunior ture（最下级）
     *
     * @param father
     * @param classification
     */
    private void getChild(RestCategoryResult father, List<RestCategoryResult> classification) {

        for (RestCategoryResult spuClassificationResult : classification) {
            if (father.getSpuClassificationId().equals(spuClassificationResult.getPid())) {
                father.setMostJunior(false);
                getChild(spuClassificationResult, classification);
            }
        }
    }


    /**
     * 更新包含它的
     */
    public void updateChildren(Long id) {
        List<RestCategory> classifications = this.query().like("children", id).eq("display", 1).list();
        for (RestCategory classification : classifications) {
            Map<String, List<Long>> childrenMap = getChildrens(id);
            JSONArray childrensjsonArray = JSONUtil.parseArray(classification.getChildrens());
            List<Long> longs = JSONUtil.toList(childrensjsonArray, Long.class);
            List<Long> list = childrenMap.get("childrens");
            for (Long aLong : list) {
                longs.add(aLong);
            }
            classification.setChildrens(JSON.toJSONString(longs));
            // update
            QueryWrapper<RestCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("spu_classification_id", classification.getSpuClassificationId());
            this.update(classification, queryWrapper);
            updateChildren(classification.getSpuClassificationId());
        }
    }

    @Override
    public RestCategoryResult findBySpec(RestCategoryParam param) {
        return null;
    }

    @Override
    public List<RestCategoryResult> findListBySpec(RestCategoryParam param) {
        return null;
    }

    @Override
    public PageInfo<RestCategoryResult> findPageBySpec(RestCategoryParam param) {
        Page<RestCategoryResult> pageContext = getPageContext();
        IPage<RestCategoryResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(RestCategoryParam param) {
        return param.getSpuClassificationId();
    }

    private Page<RestCategoryResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private RestCategory getOldEntity(RestCategoryParam param) {
        return this.getById(getKey(param));
    }

    private RestCategory getEntity(RestCategoryParam param) {
        RestCategory entity = new RestCategory();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<RestCategoryResult> data) {
        List<Long> ids = new ArrayList<>();
        List<Long> pids = new ArrayList<>();
        for (RestCategoryResult datum : data) {
            ids.add(datum.getSpuClassificationId());
            pids.add(datum.getPid());
        }

        List<RestSpu> spus = ids.size() == 0 ? new ArrayList<>() : spuService.lambdaQuery().in(RestSpu::getSpuClassificationId, ids).list();
        List<RestCategory> classifications = pids.size() == 0 ? new ArrayList<>() : this.query().in("spu_classification_id", pids).list();

        for (RestCategoryResult datum : data) {
            List<RestSpu> spuList = new ArrayList<>();
            for (RestSpu spu : spus) {
                if (datum.getSpuClassificationId().equals(spu.getSpuClassificationId())) {
                    spuList.add(spu);
                }
            }
            if (ToolUtil.isNotEmpty(classifications)) {
                for (RestCategory classification : classifications) {
                    if (datum.getPid() != null && classification.getSpuClassificationId().equals(datum.getPid())) {
                        datum.setPidName(classification.getName());
                        break;
                    }
                }
            }
            datum.setSpuList(spuList);
        }
    }


    /**
     * 从子集到父级的所有编码拼接
     *
     * @return
     */
    @Override
    public String getCodings(Long classId) {
        RestCategory now = this.getById(classId);
        List<RestCategory> all = this.list();
//        String codings = now.getCodingClass() ;
//        StringBuffer stringBuffer = this.loopGetCoding(now, all, new StringBuffer());
        String coding = getCoding(now, all);
        if (ToolUtil.isEmpty(coding)) {
            return "";
        }
        return coding;
    }

//    private StringBuffer loopGetCoding(SpuClassification now,List<SpuClassification> all,StringBuffer codings){
//        if(ToolUtil.isNotEmpty(now.getPid())){
//            for (SpuClassification spuClassification : all) {
//                if (now.getPid().equals(spuClassification.getSpuClassificationId())) {
//                   StringBuffer sb = new StringBuffer().append(ToolUtil.isEmpty(now.getCodingClass()) ? "" : now.getCodingClass()).append(codings);
//                    codings = sb;
//                    codings = loopGetCoding(spuClassification,all,codings);
//                }
//            }
//        }else {
//            StringBuffer sb = new StringBuffer().append(ToolUtil.isEmpty(now.getCodingClass()) ? "" : now.getCodingClass()).append(codings);
//            codings = sb;
//        }
//        return codings;
//    }

    private String getCoding(RestCategory result, List<RestCategory> resultList) {
        String coding = "";
        if (result.getPid() == 0) {
            coding = result.getCodingClass();
        } else {
            for (RestCategory spuClassification : resultList) {
                if (result.getPid().equals(spuClassification.getSpuClassificationId())) {
                    String cod = getCoding(spuClassification, resultList);
                    coding = cod + result.getCodingClass();
                }
            }
        }
        return coding;
    }

}
