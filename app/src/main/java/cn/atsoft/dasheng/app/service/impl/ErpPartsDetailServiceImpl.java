package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ErpPartsDetail;
import cn.atsoft.dasheng.app.mapper.ErpPartsDetailMapper;
import cn.atsoft.dasheng.app.model.params.ErpPartsDetailParam;
import cn.atsoft.dasheng.app.model.result.ErpPartsDetailResult;
import cn.atsoft.dasheng.app.service.ErpPartsDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
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
 * 清单详情 服务实现类
 * </p>
 *
 * @author cheng
 * @since 2021-10-26
 */
@Service
public class ErpPartsDetailServiceImpl extends ServiceImpl<ErpPartsDetailMapper, ErpPartsDetail> implements ErpPartsDetailService {
    @Autowired
    private SkuService skuService;


    @Override
    public void add(ErpPartsDetailParam param) {
        ErpPartsDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ErpPartsDetailParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(ErpPartsDetailParam param) {
        ErpPartsDetail oldEntity = getOldEntity(param);
        ErpPartsDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ErpPartsDetailResult findBySpec(ErpPartsDetailParam param) {
        return null;
    }

    @Override
    public List<ErpPartsDetailResult> findListBySpec(ErpPartsDetailParam param) {
        return null;
    }

    @Override
    public PageInfo<ErpPartsDetailResult> findPageBySpec(ErpPartsDetailParam param) {
        Page<ErpPartsDetailResult> pageContext = getPageContext();
        IPage<ErpPartsDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ErpPartsDetailParam param) {
        return param.getPartsDetailId();
    }

    private Page<ErpPartsDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ErpPartsDetail getOldEntity(ErpPartsDetailParam param) {
        return this.getById(getKey(param));
    }

    private ErpPartsDetail getEntity(ErpPartsDetailParam param) {
        ErpPartsDetail entity = new ErpPartsDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    @Override
    public List<ErpPartsDetailResult> getDetails(List<Long> partIds) {
        if (ToolUtil.isEmpty(partIds)) {
            return new ArrayList<>();
        }
        List<ErpPartsDetail> partsDetails = this.query().in("parts_id", partIds).list();
        List<Long> skuIds = new ArrayList<>();
        List<ErpPartsDetailResult> detailResults = BeanUtil.copyToList(partsDetails, ErpPartsDetailResult.class, new CopyOptions());

        for (ErpPartsDetailResult detailResult : detailResults) {
            skuIds.add(detailResult.getSkuId());
        }
        List<SkuResult> results = skuService.formatSkuResult(skuIds);

        for (ErpPartsDetailResult detailResult : detailResults) {
            for (SkuResult result : results) {
                if (detailResult.getSkuId().equals(result.getSkuId())) {
                    detailResult.setSkuResult(result);
                    break;
                }
            }
        }

        return detailResults;
    }

}
