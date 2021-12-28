package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.PurchaseConfig;
import cn.atsoft.dasheng.purchase.mapper.PurchaseConfigMapper;
import cn.atsoft.dasheng.purchase.model.params.PurchaseConfigParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseConfigResult;
import  cn.atsoft.dasheng.purchase.service.PurchaseConfigService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 采购配置表 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
@Service
public class PurchaseConfigServiceImpl extends ServiceImpl<PurchaseConfigMapper, PurchaseConfig> implements PurchaseConfigService {

    @Override
    public void add(PurchaseConfigParam param){
        PurchaseConfig entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PurchaseConfigParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PurchaseConfigParam param){
        PurchaseConfig oldEntity = getOldEntity(param);
        PurchaseConfig newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PurchaseConfigResult findBySpec(PurchaseConfigParam param){
        return null;
    }

    @Override
    public List<PurchaseConfigResult> findListBySpec(PurchaseConfigParam param){
        return null;
    }

    @Override
    public PageInfo<PurchaseConfigResult> findPageBySpec(PurchaseConfigParam param){
        Page<PurchaseConfigResult> pageContext = getPageContext();
        IPage<PurchaseConfigResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PurchaseConfigParam param){
        return param.getPurchaseConfigId();
    }

    private Page<PurchaseConfigResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PurchaseConfig getOldEntity(PurchaseConfigParam param) {
        return this.getById(getKey(param));
    }

    private PurchaseConfig getEntity(PurchaseConfigParam param) {
        PurchaseConfig entity = new PurchaseConfig();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
