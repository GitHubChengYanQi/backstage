package cn.atsoft.dasheng.erp.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.erp.entity.Sku;
import cn.atsoft.dasheng.erp.mapper.SkuMapper;
import cn.atsoft.dasheng.erp.model.params.SkuParam;
import cn.atsoft.dasheng.erp.model.result.SkuResult;
import  cn.atsoft.dasheng.erp.service.SkuService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * sku表	 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-10-18
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {

    @Override
    public void add(SkuParam param){
        Sku entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(SkuParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(SkuParam param){
        Sku oldEntity = getOldEntity(param);
        Sku newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public SkuResult findBySpec(SkuParam param){
        return null;
    }

    @Override
    public List<SkuResult> findListBySpec(SkuParam param){
        return null;
    }

    @Override
    public PageInfo<SkuResult> findPageBySpec(SkuParam param){
        Page<SkuResult> pageContext = getPageContext();
        IPage<SkuResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(SkuParam param){
        return param.getSkuId();
    }

    private Page<SkuResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Sku getOldEntity(SkuParam param) {
        return this.getById(getKey(param));
    }

    private Sku getEntity(SkuParam param) {
        Sku entity = new Sku();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
