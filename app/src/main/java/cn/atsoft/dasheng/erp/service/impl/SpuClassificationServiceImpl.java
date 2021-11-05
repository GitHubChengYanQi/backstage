package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.entity.Parts;
import cn.atsoft.dasheng.base.log.BussinessLog;
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
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class SpuClassificationServiceImpl extends ServiceImpl<SpuClassificationMapper, SpuClassification> implements SpuClassificationService {

    @Autowired
    private SpuService spuService;

    @Override
    public Long add(SpuClassificationParam param) {
        Integer count = this.lambdaQuery().in(SpuClassification::getDisplay, 1).in(SpuClassification::getName, param.getName()).count();
        if (count > 0) {
            throw new ServiceException(500, "名字以重复");
        }
        SpuClassification entity = getEntity(param);
        this.save(entity);

        if (ToolUtil.isNotEmpty(param.getPid()) && param.getPid() != 0) {
            Map<String, List<Long>> childrens = this.getChildrens(param.getPid());
            param.setChildren(JSON.toJSONString(childrens.get("children")));
            param.setChildrens(JSON.toJSONString(childrens.get("childrens")));
        }

        // 更新当前节点，及下级
        SpuClassification spuClassification = new SpuClassification();
        Map<String, List<Long>> childrenMap = getChildrens(entity.getSpuClassificationId());
        spuClassification.setChildrens(JSON.toJSONString(childrenMap.get("childrens")));
        spuClassification.setChildren(JSON.toJSONString(param.getPid()));
        QueryWrapper<SpuClassification> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("spu_classification_id", entity.getPid());
        this.update(spuClassification, QueryWrapper);

        updateChildren(entity.getSpuClassificationId());

        return entity.getSpuClassificationId();

    }

    @Override
    @BussinessLog
    public void delete(SpuClassificationParam param) {
        Integer count = spuService.lambdaQuery().eq(Spu::getSpuClassificationId, param.getSpuClassificationId()).and(i -> i.eq(Spu::getDisplay, 1)).count();
        if (count > 0) {
            throw new ServiceException(500, "此分类下有物品,无法删除");
        } else {
            param.setDisplay(0);
            this.update(param);
        }
//        this.removeById(getKey(param));
    }

    @Override
    @BussinessLog
    public void update(SpuClassificationParam param) {

//        List<SpuClassification> classifications = this.query().eq("pid", param.getSpuClassificationId()).list();
//
//        for (SpuClassification classification : classifications) {
//            JSONArray jsonArray = JSONUtil.parseArray(classification.getChildrens());
//            List<Long> longs = JSONUtil.toList(jsonArray, Long.class);
//            for (Long aLong : longs) {
//                if (param.getPid().equals(aLong)) {
//                    throw new ServiceException(500, "请勿循环添加");
//                }
//            }
//        }

        Map<String, List<Long>> childrens = this.getChildrens(param.getSpuClassificationId());

        param.setChildren(JSON.toJSONString(childrens.get("children")));
        param.setChildrens(JSON.toJSONString(childrens.get("childrens")));

        SpuClassification oldEntity = getOldEntity(param);
        SpuClassification newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
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
                Map<String, List<Long>> childrenMap = this.getChildrens(detail.getPid());
                childrensSkuIds.addAll(childrenMap.get("childrens"));
            }
            result.put("children", skuIds);
            result.put("childrens", childrensSkuIds);
        }
        return result;
    }

    /**
     * 更新包含它的
     */
    public void updateChildren(Long id) {
        List<SpuClassification> classifications = this.query().like("children", id).eq("display", 1).list();
        for (SpuClassification classification : classifications) {
            Map<String, List<Long>> childrenMap = getChildrens(id);
            classification.setChildren(JSON.toJSONString(childrenMap.get("children")));
            classification.setChildrens(JSON.toJSONString(childrenMap.get("childrens")));
            // update
            QueryWrapper<SpuClassification> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("spu_classification_id", classification.getPid());
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


}
