package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Invoice;
import cn.atsoft.dasheng.crm.mapper.InvoiceMapper;
import cn.atsoft.dasheng.crm.model.params.InvoiceParam;
import cn.atsoft.dasheng.crm.model.result.InvoiceResult;
import cn.atsoft.dasheng.crm.service.InvoiceService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
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
 * 供应商开票 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-20
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements InvoiceService {


    @Override
    public Long add(InvoiceParam param) {
        Invoice entity = getEntity(param);
        this.save(entity);
        return entity.getInvoiceId();
    }

    @Override
    public void delete(InvoiceParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(InvoiceParam param) {
        Invoice oldEntity = getOldEntity(param);
        Invoice newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public InvoiceResult findBySpec(InvoiceParam param) {
        return null;
    }

    @Override
    public List<InvoiceResult> findListBySpec(InvoiceParam param) {
        return null;
    }

    @Override
    public PageInfo<InvoiceResult> findPageBySpec(InvoiceParam param) {
        Page<InvoiceResult> pageContext = getPageContext();
        IPage<InvoiceResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private void format(List<InvoiceResult> param) {
        List<Long> bankIds = new ArrayList<>();
        for (InvoiceResult invoiceResult : param) {

            bankIds.add(invoiceResult.getBankId());
        }
    }

    private Serializable getKey(InvoiceParam param) {
        return param.getInvoiceId();
    }

    private Page<InvoiceResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Invoice getOldEntity(InvoiceParam param) {
        return this.getById(getKey(param));
    }

    private Invoice getEntity(InvoiceParam param) {
        Invoice entity = new Invoice();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

    public List<InvoiceResult> getDetails(List<Long> ids) {
        if (ToolUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<Invoice> invoices = this.listByIds(ids);
        if (ToolUtil.isEmpty(invoices)) {
            return new ArrayList<>();
        }
        List<InvoiceResult> results = BeanUtil.copyToList(invoices, InvoiceResult.class, new CopyOptions());

        return results;
    }

}
