package cn.atsoft.dasheng.orderPaymentApply.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.orderPaymentApply.entity.CrmOrderPaymentApply;
import cn.atsoft.dasheng.orderPaymentApply.mapper.CrmOrderPaymentApplyMapper;
import cn.atsoft.dasheng.orderPaymentApply.model.params.CrmOrderPaymentApplyParam;
import cn.atsoft.dasheng.orderPaymentApply.model.result.CrmOrderPaymentApplyResult;
import cn.atsoft.dasheng.orderPaymentApply.service.CrmOrderPaymentApplyService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2023-03-18
 */
@Service
public class CrmOrderPaymentApplyServiceImpl extends ServiceImpl<CrmOrderPaymentApplyMapper, CrmOrderPaymentApply> implements CrmOrderPaymentApplyService {

    @Override
    public void add(CrmOrderPaymentApplyParam param){
        CrmOrderPaymentApply entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmOrderPaymentApplyParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CrmOrderPaymentApplyParam param){
        CrmOrderPaymentApply oldEntity = getOldEntity(param);
        CrmOrderPaymentApply newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmOrderPaymentApplyResult findBySpec(CrmOrderPaymentApplyParam param){
        return null;
    }

    @Override
    public List<CrmOrderPaymentApplyResult> findListBySpec(CrmOrderPaymentApplyParam param){
        return null;
    }

    @Override
    public PageInfo<CrmOrderPaymentApplyResult> findPageBySpec(CrmOrderPaymentApplyParam param){
        Page<CrmOrderPaymentApplyResult> pageContext = getPageContext();
        IPage<CrmOrderPaymentApplyResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmOrderPaymentApplyParam param){
        return param.getSpNo();
    }

    private Page<CrmOrderPaymentApplyResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmOrderPaymentApply getOldEntity(CrmOrderPaymentApplyParam param) {
        return this.getById(getKey(param));
    }

    private CrmOrderPaymentApply getEntity(CrmOrderPaymentApplyParam param) {
        CrmOrderPaymentApply entity = new CrmOrderPaymentApply();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
