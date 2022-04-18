package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.AnomalyDetail;
import cn.atsoft.dasheng.erp.mapper.AnomalyDetailMapper;
import cn.atsoft.dasheng.erp.model.params.AnomalyDetailParam;
import cn.atsoft.dasheng.erp.model.result.AnomalyDetailResult;
import cn.atsoft.dasheng.erp.model.result.SkuSimpleResult;
import cn.atsoft.dasheng.erp.service.AnomalyDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.service.SkuService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
 * 异常详情 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-04-12
 */
@Service
public class AnomalyDetailServiceImpl extends ServiceImpl<AnomalyDetailMapper, AnomalyDetail> implements AnomalyDetailService {
    @Autowired
    private SkuService skuService;


    @Override
    public void add(AnomalyDetailParam param) {
        AnomalyDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(AnomalyDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(AnomalyDetailParam param) {
        AnomalyDetail oldEntity = getOldEntity(param);
        AnomalyDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public AnomalyDetailResult findBySpec(AnomalyDetailParam param) {
        return null;
    }

    @Override
    public List<AnomalyDetailResult> findListBySpec(AnomalyDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<AnomalyDetailResult> findPageBySpec(AnomalyDetailParam param) {
        Page<AnomalyDetailResult> pageContext = getPageContext();
        IPage<AnomalyDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(AnomalyDetailParam param) {
        return param.getDetailId();
    }

    private Page<AnomalyDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private AnomalyDetail getOldEntity(AnomalyDetailParam param) {
        return this.getById(getKey(param));
    }

    private AnomalyDetail getEntity(AnomalyDetailParam param) {
        AnomalyDetail entity = new AnomalyDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<AnomalyDetailResult> getResultByAnomalyId(Long id) {
        if (ToolUtil.isEmpty(id)) {
            return null;
        }
        List<AnomalyDetail> details = this.query().eq("anomaly_id", id).list();
        List<AnomalyDetailResult> anomalyDetailResults = BeanUtil.copyToList(details, AnomalyDetailResult.class, new CopyOptions());
        format(anomalyDetailResults);
        return anomalyDetailResults;
    }

    private void format(List<AnomalyDetailResult> data) {

        List<Long> skuIds = new ArrayList<>();
        for (AnomalyDetailResult datum : data) {
            skuIds.add(datum.getSkuId());
        }
        List<SkuSimpleResult> skuSimpleResults = skuService.simpleFormatSkuResult(skuIds);

        for (AnomalyDetailResult datum : data) {
            for (SkuSimpleResult skuSimpleResult : skuSimpleResults) {
                if (datum.getSkuId().equals(skuSimpleResult.getSkuId())) {
                    datum.setSimpleResult(skuSimpleResult);
                    break;
                }
            }
        }

    }
}
