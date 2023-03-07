package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.PurchaseList;
import cn.atsoft.dasheng.purchase.mapper.RestPurchaseListMapper;
import cn.atsoft.dasheng.purchase.model.params.PurchaseListParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseListResult;
import cn.atsoft.dasheng.purchase.service.RestPurchaseListService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 预购管理 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-04
 */
@Service
public class RestPurchaseListServiceImpl extends ServiceImpl<RestPurchaseListMapper, PurchaseList> implements RestPurchaseListService {

    @Override
    public void add(PurchaseListParam param){
        PurchaseList entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PurchaseListParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PurchaseListParam param){
        PurchaseList oldEntity = getOldEntity(param);
        PurchaseList newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PurchaseListResult findBySpec(PurchaseListParam param){
        return null;
    }

    @Override
    public List<PurchaseListResult> findListBySpec(PurchaseListParam param){
        return null;
    }

    @Override
    public PageInfo<PurchaseListResult> findPageBySpec(PurchaseListParam param){
        Page<PurchaseListResult> pageContext = getPageContext();
        IPage<PurchaseListResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PurchaseListParam param){
        return param.getPurchaseListId();
    }

    private Page<PurchaseListResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PurchaseList getOldEntity(PurchaseListParam param) {
        return this.getById(getKey(param));
    }

    private PurchaseList getEntity(PurchaseListParam param) {
        PurchaseList entity = new PurchaseList();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
