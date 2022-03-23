package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionJobBookingDetail;
import cn.atsoft.dasheng.production.mapper.ProductionJobBookingDetailMapper;
import cn.atsoft.dasheng.production.model.params.ProductionJobBookingDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionJobBookingDetailResult;
import  cn.atsoft.dasheng.production.service.ProductionJobBookingDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 报工详情表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-23
 */
@Service
public class ProductionJobBookingDetailServiceImpl extends ServiceImpl<ProductionJobBookingDetailMapper, ProductionJobBookingDetail> implements ProductionJobBookingDetailService {

    @Override
    public void add(ProductionJobBookingDetailParam param){
        ProductionJobBookingDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionJobBookingDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionJobBookingDetailParam param){
        ProductionJobBookingDetail oldEntity = getOldEntity(param);
        ProductionJobBookingDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionJobBookingDetailResult findBySpec(ProductionJobBookingDetailParam param){
        return null;
    }

    @Override
    public List<ProductionJobBookingDetailResult> findListBySpec(ProductionJobBookingDetailParam param){
        return null;
    }

    @Override
    public PageInfo<ProductionJobBookingDetailResult> findPageBySpec(ProductionJobBookingDetailParam param){
        Page<ProductionJobBookingDetailResult> pageContext = getPageContext();
        IPage<ProductionJobBookingDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionJobBookingDetailParam param){
        return param.getJobBookingDetailId();
    }

    private Page<ProductionJobBookingDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionJobBookingDetail getOldEntity(ProductionJobBookingDetailParam param) {
        return this.getById(getKey(param));
    }

    private ProductionJobBookingDetail getEntity(ProductionJobBookingDetailParam param) {
        ProductionJobBookingDetail entity = new ProductionJobBookingDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
