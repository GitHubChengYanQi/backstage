package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.service.CustomerService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Bank;
import cn.atsoft.dasheng.crm.entity.Invoice;
import cn.atsoft.dasheng.crm.mapper.InvoiceMapper;
import cn.atsoft.dasheng.crm.model.params.InvoiceParam;
import cn.atsoft.dasheng.crm.model.result.BankResult;
import cn.atsoft.dasheng.crm.model.result.InvoiceResult;
import cn.atsoft.dasheng.crm.service.BankService;
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
    @Autowired
    private BankService bankService;

    @Override
    public Invoice add(InvoiceParam param) {
        if (ToolUtil.isEmpty(param.getBankAccount()) || ToolUtil.isEmpty(param.getBankNo()) || ToolUtil.isEmpty(param.getBankId())) {
            throw new ServiceException(500, "请填写完整银行信息");
        }
        Invoice entity = getEntity(param);
        this.save(entity);
        return entity;
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
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private void format(List<InvoiceResult> param) {
        List<Long> bankIds = new ArrayList<>();
        for (InvoiceResult invoiceResult : param) {
            bankIds.add(invoiceResult.getBankId());
        }
        List<Bank> bankList = bankIds.size() == 0 ? new ArrayList<>() : bankService.listByIds(bankIds);
        List<BankResult> bankResults = BeanUtil.copyToList(bankList, BankResult.class, new CopyOptions());
        for (InvoiceResult invoiceResult : param) {
            for (BankResult bankResult : bankResults) {
                if (ToolUtil.isNotEmpty(invoiceResult.getBankId()) && invoiceResult.getBankId().equals(bankResult.getBankId())) {
                    invoiceResult.setBankResult(bankResult);
                    break;
                }
            }
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


    @Override
    public List<InvoiceResult> getDetails(List<Long> ids) {
        if (ToolUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<Invoice> invoices = this.listByIds(ids);
        if (ToolUtil.isEmpty(invoices)) {
            return new ArrayList<>();
        }
        List<InvoiceResult> results = BeanUtil.copyToList(invoices, InvoiceResult.class, new CopyOptions());
        List<Long> bankIds = new ArrayList<>();
        for (InvoiceResult result : results) {
            bankIds.add(result.getBankId());
        }
        List<Bank> banks = bankIds.size() == 0 ? new ArrayList<>() : bankService.listByIds(bankIds);
        List<BankResult> bankResults = BeanUtil.copyToList(banks, BankResult.class);

        for (InvoiceResult result : results) {
            for (BankResult bankResult : bankResults) {
                if (ToolUtil.isNotEmpty(result.getBankId()) && bankResult.getBankId().equals(result.getBankId())) {
                    result.setBankResult(bankResult);
                    break;
                }
            }
        }
        return results;
    }

}
