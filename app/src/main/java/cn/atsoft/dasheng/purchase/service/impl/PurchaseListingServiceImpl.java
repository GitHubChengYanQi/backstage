package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.mapper.PurchaseListingMapper;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListingParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListingResult;
import  cn.atsoft.dasheng.purchase.service.PurchaseListingService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 采购清单 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
@Service
public class PurchaseListingServiceImpl extends ServiceImpl<PurchaseListingMapper, PurchaseListing> implements PurchaseListingService {

    @Override
    public void add(PurchaseListingParam param){
        PurchaseListing entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PurchaseListingParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PurchaseListingParam param){
        PurchaseListing oldEntity = getOldEntity(param);
        PurchaseListing newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PurchaseListingResult findBySpec(PurchaseListingParam param){
        return null;
    }

    @Override
    public List<PurchaseListingResult> findListBySpec(PurchaseListingParam param){
        return null;
    }

    @Override
    public PageInfo<PurchaseListingResult> findPageBySpec(PurchaseListingParam param){
        Page<PurchaseListingResult> pageContext = getPageContext();
        IPage<PurchaseListingResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PurchaseListingParam param){
        return param.getPurchaseListingId();
    }

    private Page<PurchaseListingResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PurchaseListing getOldEntity(PurchaseListingParam param) {
        return this.getById(getKey(param));
    }

    private PurchaseListing getEntity(PurchaseListingParam param) {
        PurchaseListing entity = new PurchaseListing();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
