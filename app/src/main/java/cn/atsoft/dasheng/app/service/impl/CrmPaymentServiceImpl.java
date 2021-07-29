package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmPayment;
import cn.atsoft.dasheng.app.mapper.CrmPaymentMapper;
import cn.atsoft.dasheng.app.model.params.CrmPaymentParam;
import cn.atsoft.dasheng.app.model.result.CrmPaymentResult;
import  cn.atsoft.dasheng.app.service.CrmPaymentService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 付款信息表 服务实现类
 * </p>
 *
 * @author ta
 * @since 2021-07-26
 */
@Service
public class CrmPaymentServiceImpl extends ServiceImpl<CrmPaymentMapper, CrmPayment> implements CrmPaymentService {

    @Override
    public void add(CrmPaymentParam param){
        CrmPayment entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmPaymentParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CrmPaymentParam param){
        CrmPayment oldEntity = getOldEntity(param);
        CrmPayment newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmPaymentResult findBySpec(CrmPaymentParam param){
        return null;
    }

    @Override
    public List<CrmPaymentResult> findListBySpec(CrmPaymentParam param){
        return null;
    }

    @Override
    public PageInfo<CrmPaymentResult> findPageBySpec(CrmPaymentParam param){
        Page<CrmPaymentResult> pageContext = getPageContext();
        IPage<CrmPaymentResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmPaymentParam param){
        return param.getPaymentId();
    }

    private Page<CrmPaymentResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmPayment getOldEntity(CrmPaymentParam param) {
        return this.getById(getKey(param));
    }

    private CrmPayment getEntity(CrmPaymentParam param) {
        CrmPayment entity = new CrmPayment();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
