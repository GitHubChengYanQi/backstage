package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Bank;
import cn.atsoft.dasheng.crm.mapper.BankMapper;
import cn.atsoft.dasheng.crm.model.params.BankParam;
import cn.atsoft.dasheng.crm.model.result.BankResult;
import  cn.atsoft.dasheng.crm.service.BankService;
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
 * @author song
 * @since 2022-02-24
 */
@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements BankService {

    @Override
    public void add(BankParam param){
        Bank entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(BankParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(BankParam param){
        Bank oldEntity = getOldEntity(param);
        Bank newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public BankResult findBySpec(BankParam param){
        return null;
    }

    @Override
    public List<BankResult> findListBySpec(BankParam param){
        return null;
    }

    @Override
    public PageInfo<BankResult> findPageBySpec(BankParam param){
        Page<BankResult> pageContext = getPageContext();
        IPage<BankResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(BankParam param){
        return param.getBankId();
    }

    private Page<BankResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Bank getOldEntity(BankParam param) {
        return this.getById(getKey(param));
    }

    private Bank getEntity(BankParam param) {
        Bank entity = new Bank();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
