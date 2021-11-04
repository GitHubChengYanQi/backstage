package cn.atsoft.dasheng.erp.service.impl;


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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        return entity.getSpuClassificationId();
    }

    @Override
    public void delete(SpuClassificationParam param) {
        SpuClassification spuClassification = new SpuClassification();
        spuClassification.setDisplay(0);
        QueryWrapper<SpuClassification> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("spu_classification_id", param.getSpuClassificationId());
        this.update(spuClassification, queryWrapper);
//        this.removeById(getKey(param));
    }

    @Override
    public void update(SpuClassificationParam param) {
        SpuClassification oldEntity = getOldEntity(param);
        SpuClassification newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
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
