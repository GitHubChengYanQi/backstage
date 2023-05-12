package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.auth.context.LoginContextHolder;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Spu;
import cn.atsoft.dasheng.erp.entity.SpuClassification;
import cn.atsoft.dasheng.erp.mapper.SpuClassificationMapper;
import cn.atsoft.dasheng.erp.model.params.SpuClassificationParam;
import cn.atsoft.dasheng.erp.model.result.SpuClassificationResult;
import cn.atsoft.dasheng.erp.service.SpuClassificationService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SpuService;
import cn.atsoft.dasheng.form.entity.FormStyle;
import cn.atsoft.dasheng.form.service.FormStyleService;
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
import java.util.*;

/**
 * <p>
 * SPU分类 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-10-25
 */
@Service
public class SpuClassificationServiceImpl extends ServiceImpl<SpuClassificationMapper, SpuClassification> implements SpuClassificationService {

    @Autowired
    private SpuService spuService;

    @Autowired
    private FormStyleService formStyleService;

    @Override
    @Transactional
    public Long add(SpuClassificationParam param) {
//        Integer count = this.lambdaQuery().in(SpuClassification::getDisplay, 1).eq(SpuClassification::getTenantId, LoginContextHolder.getContext().getTenantId()).in(SpuClassification::getName, param.getName()).and(i -> i.eq(SpuClassification::getType, 1)).count();
//        if (count > 0) {
//            throw new ServiceException(500, "名字已重复");
//        }
//        if (ToolUtil.isNotEmpty(param.getPid())) {
//            Integer num = this.query().eq("pid", param.getPid()).eq("type", 2).eq("display", 1).count();
//            if (num > 0) {
//                throw new ServiceException(500, "当前分类下已有产品，不可创建");
//            }
//            Integer spuCount = spuService.query().eq("spu_classification_id", param.getPid()).eq("display", 1).count();
//            if (spuCount > 0) {
//                throw new ServiceException(500, "父级分类下已存在产品,不可添加");
//            }

//        }
        SpuClassification entity = getEntity(param);
        this.save(entity);

        //分类标单排版
        if (ToolUtil.isNotEmpty(param.getTypeSetting())) {
            FormStyle formStyle = new FormStyle();
            formStyle.setTypeSetting(param.getTypeSetting());
            formStyle.setFormType("spuClass");
            formStyleService.save(formStyle);
            entity.setFormStyleId(formStyle.getStyleId());
            this.updateById(entity);
        }


        return entity.getSpuClassificationId();

    }

    @Override

    public void  delete(SpuClassificationParam param) {
        Integer children = this.query().eq("pid", param.getSpuClassificationId()).eq("display", 1).count();
        if (children > 0) {
            throw new ServiceException(500, "此分类下有下级,无法删除");
        } else {
            Integer count = spuService.lambdaQuery().eq(Spu::getSpuClassificationId, param.getSpuClassificationId()).eq(Spu::getDisplay, 1).count();
            if (count> 0) {
                throw new ServiceException(500, "此分类下有产品,无法删除");
            }
            SpuClassification spuClassification = new SpuClassification();
            spuClassification.setSpuClassificationId(param.getSpuClassificationId());
            spuClassification.setDisplay(0);
            this.updateById(spuClassification);
        }
//        this.removeById(getKey(param));
    }

    @Override
    @Transactional
    public void update(SpuClassificationParam param) {

        SpuClassification oldEntity = getOldEntity(param);
        SpuClassification newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);

        if (ToolUtil.isNotEmpty(param.getTypeSetting())) {
            FormStyle formStyle = null;
            if (ToolUtil.isNotEmpty(newEntity.getFormStyleId())) {
                formStyle = formStyleService.getById(newEntity.getFormStyleId());
                formStyle.setTypeSetting(param.getTypeSetting());
                formStyleService.updateById(formStyle);
            } else {
                formStyle = new FormStyle();
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
        SpuClassification classification = this.query().eq("spu_classification_id", id).eq("display", 1).one();
        if (ToolUtil.isNotEmpty(classification)) {
            List<SpuClassification> details = this.query().eq("pid", classification.getSpuClassificationId()).eq("display", 1).list();
            for (SpuClassification detail : details) {
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

        List<SpuClassification> classifications = this.query().eq("display", 1).eq("type", 1).list();
        List<SpuClassificationResult> results = BeanUtil.copyToList(classifications, SpuClassificationResult.class, new CopyOptions());

        for (SpuClassificationResult result : results) {
            if (result.getPid() == 0) {
                getChild(result, results);
            }
        }
        for (SpuClassificationResult result : results) {
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
    private void getChild(SpuClassificationResult father, List<SpuClassificationResult> classification) {

        for (SpuClassificationResult spuClassificationResult : classification) {
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
        List<SpuClassification> classifications = this.query().like("children", id).eq("display", 1).list();
        for (SpuClassification classification : classifications) {
            Map<String, List<Long>> childrenMap = getChildrens(id);
            JSONArray childrensjsonArray = JSONUtil.parseArray(classification.getChildrens());
            List<Long> longs = JSONUtil.toList(childrensjsonArray, Long.class);
            List<Long> list = childrenMap.get("childrens");
            for (Long aLong : list) {
                longs.add(aLong);
            }
            classification.setChildrens(JSON.toJSONString(longs));
            // update
            QueryWrapper<SpuClassification> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("spu_classification_id", classification.getSpuClassificationId());
            this.update(classification, queryWrapper);
            updateChildren(classification.getSpuClassificationId());
        }
    }

    @Override
    public SpuClassificationResult findBySpec(SpuClassificationParam param) {
        return null;
    }

    @Override
    public List<SpuClassificationResult> findListBySpec(SpuClassificationParam param) {
        return null;
    }

    @Override
    public PageInfo<SpuClassificationResult> findPageBySpec(SpuClassificationParam param) {
        Page<SpuClassificationResult> pageContext = getPageContext();
        IPage<SpuClassificationResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SpuClassificationParam param) {
        return param.getSpuClassificationId();
    }

    private Page<SpuClassificationResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private SpuClassification getOldEntity(SpuClassificationParam param) {
        return this.getById(getKey(param));
    }

    private SpuClassification getEntity(SpuClassificationParam param) {
        SpuClassification entity = new SpuClassification();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public void format(List<SpuClassificationResult> data) {
        List<Long> ids = new ArrayList<>();
        List<Long> pids = new ArrayList<>();
        for (SpuClassificationResult datum : data) {
            ids.add(datum.getSpuClassificationId());
            pids.add(datum.getPid());
        }

        List<Spu> spus = ids.size() == 0 ? new ArrayList<>() : spuService.lambdaQuery().in(Spu::getSpuClassificationId, ids).list();
        List<SpuClassification> classifications = pids.size() == 0 ? new ArrayList<>() : this.query().in("spu_classification_id", pids).list();

        for (SpuClassificationResult datum : data) {
            List<Spu> spuList = new ArrayList<>();
            for (Spu spu : spus) {
                if (datum.getSpuClassificationId().equals(spu.getSpuClassificationId())) {
                    spuList.add(spu);
                }
            }
            if (ToolUtil.isNotEmpty(classifications)) {
                for (SpuClassification classification : classifications) {
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
        if (classId.equals(0L)){
            return "";
        }
        SpuClassification now = this.getById(classId);
        List<SpuClassification> all = this.list();
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

    private String getCoding(SpuClassification result, List<SpuClassification> resultList) {
        String coding = "";
        if (result.getPid() == 0) {
            coding = result.getCodingClass();
        } else {
            for (SpuClassification spuClassification : resultList) {
                if (result.getPid().equals(spuClassification.getSpuClassificationId())) {
                    String cod = getCoding(spuClassification, resultList);
                    coding = cod + result.getCodingClass();
                }
            }
        }
        return coding;
    }

}
