package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.InvoiceBill;
import cn.atsoft.dasheng.crm.entity.Order;
import cn.atsoft.dasheng.crm.mapper.InvoiceBillMapper;
import cn.atsoft.dasheng.crm.model.params.InvoiceBillParam;
import cn.atsoft.dasheng.crm.model.params.OrderParam;
import cn.atsoft.dasheng.crm.model.result.InvoiceBillResult;
import  cn.atsoft.dasheng.crm.service.InvoiceBillService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.service.OrderService;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 发票 服务实现类
 * </p>
 *
 * @author song
 * @since 2022-04-22
 */
@Service
public class InvoiceBillServiceImpl extends ServiceImpl<InvoiceBillMapper, InvoiceBill> implements InvoiceBillService {
    @Override
    public void add(InvoiceBillParam param){
        InvoiceBill entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(InvoiceBillParam param){
        if (ToolUtil.isEmpty(param.getInvoiceBillId())){
            throw new ServiceException(500,"所删除的目标不存在");
        }else {
            param.setDisplay(0);
            this.update(param);
        }
    }

    @Override
    public void update(InvoiceBillParam param){
        InvoiceBill oldEntity = getOldEntity(param);
        InvoiceBill newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InvoiceBillResult findBySpec(InvoiceBillParam param){
        return null;
    }

    @Override
    public List<InvoiceBillResult> findListBySpec(InvoiceBillParam param){
        return null;
    }

    @Override
    public PageInfo<InvoiceBillResult> findPageBySpec(InvoiceBillParam param){
        Page<InvoiceBillResult> pageContext = getPageContext();
        IPage<InvoiceBillResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(InvoiceBillParam param){
        return param.getInvoiceBillId();
    }

    private Page<InvoiceBillResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private InvoiceBill getOldEntity(InvoiceBillParam param) {
        return this.getById(getKey(param));
    }

    private InvoiceBill getEntity(InvoiceBillParam param) {
        InvoiceBill entity = new InvoiceBill();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
