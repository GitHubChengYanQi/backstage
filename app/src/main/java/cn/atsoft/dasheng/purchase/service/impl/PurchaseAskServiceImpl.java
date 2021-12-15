package cn.atsoft.dasheng.purchase.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.purchase.entity.PurchaseAsk;
import cn.atsoft.dasheng.purchase.mapper.PurchaseAskMapper;
import cn.atsoft.dasheng.purchase.model.params.PurchaseAskParam;
import cn.atsoft.dasheng.purchase.model.result.PurchaseAskResult;
import  cn.atsoft.dasheng.purchase.service.PurchaseAskService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 采购申请 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-15
 */
@Service
public class PurchaseAskServiceImpl extends ServiceImpl<PurchaseAskMapper, PurchaseAsk> implements PurchaseAskService {

    @Override
    public void add(PurchaseAskParam param){
        PurchaseAsk entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(PurchaseAskParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(PurchaseAskParam param){
        PurchaseAsk oldEntity = getOldEntity(param);
        PurchaseAsk newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public PurchaseAskResult findBySpec(PurchaseAskParam param){
        return null;
    }

    @Override
    public List<PurchaseAskResult> findListBySpec(PurchaseAskParam param){
        return null;
    }

    @Override
    public PageInfo<PurchaseAskResult> findPageBySpec(PurchaseAskParam param){
        Page<PurchaseAskResult> pageContext = getPageContext();
        IPage<PurchaseAskResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(PurchaseAskParam param){
        return param.getPurchaseAskId();
    }

    private Page<PurchaseAskResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private PurchaseAsk getOldEntity(PurchaseAskParam param) {
        return this.getById(getKey(param));
    }

    private PurchaseAsk getEntity(PurchaseAskParam param) {
        PurchaseAsk entity = new PurchaseAsk();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
