package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.QualityIssuess;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.entity.Tool;
import cn.atsoft.dasheng.erp.mapper.QualityIssuessMapper;
import cn.atsoft.dasheng.erp.model.params.QualityIssuessParam;
import cn.atsoft.dasheng.erp.model.result.QualityIssuessResult;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import cn.atsoft.dasheng.erp.service.QualityIssuessService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 质检事项表 服务实现类
 * </p>
 *
 * @author
 * @since 2021-11-15
 */
@Service
public class QualityIssuessServiceImpl extends ServiceImpl<QualityIssuessMapper, QualityIssuess> implements QualityIssuessService {

    @Autowired
    private SkuService skuService;

    @Override
    public void add(QualityIssuessParam param) {
        QualityIssuess entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QualityIssuessParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(QualityIssuessParam param) {
        QualityIssuess oldEntity = getOldEntity(param);
        QualityIssuess newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QualityIssuessResult findBySpec(QualityIssuessParam param) {
        return null;
    }

    @Override
    public List<QualityIssuessResult> findListBySpec(QualityIssuessParam param) {
        return null;
    }

    @Override
    public PageInfo<QualityIssuessResult> findPageBySpec(QualityIssuessParam param) {
        Page<QualityIssuessResult> pageContext = getPageContext();
        IPage<QualityIssuessResult> page = this.baseMapper.customPageList(pageContext, param);
        this.format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    public void format(List<QualityIssuessResult> param) {
        List<String> skuIds = new ArrayList<>();
        for (QualityIssuessResult qualityIssuessResult : param) {
            if (ToolUtil.isNotEmpty(qualityIssuessResult.getSkuIds())) {
                skuIds = Arrays.asList(qualityIssuessResult.getSkuIds().split(","));
            }
        }
        List<Sku> skuList = skuIds.size() == 0 ? new ArrayList<>() : skuService.lambdaQuery().in(Sku::getSkuId, skuIds).and(i -> i.eq(Sku::getDisplay, 1)).list();
        List<SkuResult> skuResultList = new ArrayList<>();
        for (Sku sku : skuList) {
            SkuResult skuResult = new SkuResult();
            ToolUtil.copyProperties(sku,skuResult);
            skuResultList.add(skuResult);
        }
        skuService.format(skuResultList);
        for (QualityIssuessResult qualityIssuessResult : param) {
            if (ToolUtil.isNotEmpty(qualityIssuessResult.getSkuIds())) {
                skuIds = Arrays.asList(qualityIssuessResult.getSkuIds().split(","));
            }
            List<SkuResult> skuResults = new ArrayList<>();
            for (String skuId : skuIds) {
                for (SkuResult sku : skuResultList) {
                    if (skuId.equals(sku.getSkuId().toString())) {
                        SkuResult skuResult = new SkuResult();
                        ToolUtil.copyProperties(sku,skuResult);
                        skuResults.add(skuResult);
                    }
                }
            }
            qualityIssuessResult.setSkuResults(skuResults);
        }

    }

    private Serializable getKey(QualityIssuessParam param) {
        return null;
    }

    private Page<QualityIssuessResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private QualityIssuess getOldEntity(QualityIssuessParam param) {
        return this.getById(getKey(param));
    }

    private QualityIssuess getEntity(QualityIssuessParam param) {
        QualityIssuess entity = new QualityIssuess();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
