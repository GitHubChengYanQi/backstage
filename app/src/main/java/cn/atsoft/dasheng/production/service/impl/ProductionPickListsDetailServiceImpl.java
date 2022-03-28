package cn.atsoft.dasheng.production.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.production.entity.ProductionPickListsDetail;
import cn.atsoft.dasheng.production.mapper.ProductionPickListsDetailMapper;
import cn.atsoft.dasheng.production.model.params.ProductionPickListsDetailParam;
import cn.atsoft.dasheng.production.model.result.ProductionPickListsDetailResult;
import  cn.atsoft.dasheng.production.service.ProductionPickListsDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 领料单详情表 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-03-25
 */
@Service
public class ProductionPickListsDetailServiceImpl extends ServiceImpl<ProductionPickListsDetailMapper, ProductionPickListsDetail> implements ProductionPickListsDetailService {

    @Override
    public void add(ProductionPickListsDetailParam param){
        ProductionPickListsDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ProductionPickListsDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ProductionPickListsDetailParam param){
        ProductionPickListsDetail oldEntity = getOldEntity(param);
        ProductionPickListsDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ProductionPickListsDetailResult findBySpec(ProductionPickListsDetailParam param){
        return null;
    }

    @Override
    public List<ProductionPickListsDetailResult> findListBySpec(ProductionPickListsDetailParam param){
      return this.baseMapper.customList2(param);
    }

    @Override
    public PageInfo<ProductionPickListsDetailResult> findPageBySpec(ProductionPickListsDetailParam param){
        Page<ProductionPickListsDetailResult> pageContext = getPageContext();
        IPage<ProductionPickListsDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProductionPickListsDetailParam param){
        return param.getPickListsDetailId();
    }

    private Page<ProductionPickListsDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProductionPickListsDetail getOldEntity(ProductionPickListsDetailParam param) {
        return this.getById(getKey(param));
    }

    private ProductionPickListsDetail getEntity(ProductionPickListsDetailParam param) {
        ProductionPickListsDetail entity = new ProductionPickListsDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
