package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanBind;
import cn.atsoft.dasheng.purchase.entity.ProcurementPlanDetal;
import cn.atsoft.dasheng.purchase.entity.PurchaseListing;
import cn.atsoft.dasheng.purchase.mapper.ProcurementPlanBindMapper;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanBindParam;
import cn.atsoft.dasheng.purchase.model.params.ProcurementPlanParam;
import cn.atsoft.dasheng.purchase.model.result.ProcurementPlanBindResult;
import cn.atsoft.dasheng.purchase.service.ProcurementPlanBindService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.purchase.service.PurchaseListingService;
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
 * 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-21
 */
@Service
public class ProcurementPlanBindServiceImpl extends ServiceImpl<ProcurementPlanBindMapper, ProcurementPlanBind> implements ProcurementPlanBindService {
    @Autowired
    private PurchaseListingService purchaseListingService;

    @Override
    public void add(ProcurementPlanBindParam param) {
        ProcurementPlanBind entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void batchAdd(ProcurementPlanParam param) {
        List<PurchaseListing> purchaseListings = purchaseListingService.listByIds(param.getListingIds());
        if (purchaseListings.size() != param.getListingIds().size()) {
            throw new ServiceException(500, "申请单详情不合法");
        }
        List<ProcurementPlanBind> entityList = new ArrayList<>();
        for (PurchaseListing purchaseListing : purchaseListings) {
            ProcurementPlanBind entity = new ProcurementPlanBind();
            entity.setProcurementPlanId(param.getProcurementPlanId());
            entity.setAskId(purchaseListing.getPurchaseAskId());
            entity.setAskDetailId(purchaseListing.getPurchaseListingId());
            entityList.add(entity);
        }
        this.saveBatch(entityList);
    }

    @Override
    public void delete(ProcurementPlanBindParam param) {
        param.setDisplay(0);
        ProcurementPlanBind newEntity = getEntity(param);
        this.updateById(newEntity);
    }

    @Override
    public void update(ProcurementPlanBindParam param) {
        ProcurementPlanBind oldEntity = getOldEntity(param);
        ProcurementPlanBind newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        if (oldEntity.getDisplay().equals(newEntity.getDisplay())) {
            this.updateById(newEntity);
        }
    }

    @Override
    public ProcurementPlanBindResult findBySpec(ProcurementPlanBindParam param) {
        return null;
    }

    @Override
    public List<ProcurementPlanBindResult> findListBySpec(ProcurementPlanBindParam param) {
        return null;
    }

    @Override
    public PageInfo<ProcurementPlanBindResult> findPageBySpec(ProcurementPlanBindParam param) {
        Page<ProcurementPlanBindResult> pageContext = getPageContext();
        IPage<ProcurementPlanBindResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ProcurementPlanBindParam param) {
        return param.getDetailId();
    }

    private Page<ProcurementPlanBindResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ProcurementPlanBind getOldEntity(ProcurementPlanBindParam param) {
        return this.getById(getKey(param));
    }

    private ProcurementPlanBind getEntity(ProcurementPlanBindParam param) {
        ProcurementPlanBind entity = new ProcurementPlanBind();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
