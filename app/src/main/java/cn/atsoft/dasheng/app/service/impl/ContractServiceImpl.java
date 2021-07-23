package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.Contract;
import cn.atsoft.dasheng.app.mapper.ContractMapper;
import cn.atsoft.dasheng.app.model.params.ContractParam;
import cn.atsoft.dasheng.app.model.result.ContractResult;
import  cn.atsoft.dasheng.app.service.ContractService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 合同表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-21
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {

    @Override
    public void add(ContractParam param){
        Contract entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ContractParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ContractParam param){
        Contract oldEntity = getOldEntity(param);
        Contract newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ContractResult findBySpec(ContractParam param){
        return null;
    }

    @Override
    public List<ContractResult> findListBySpec(ContractParam param){
        return null;
    }

    @Override
    public PageInfo<ContractResult> findPageBySpec(ContractParam param){
        Page<ContractResult> pageContext = getPageContext();
        IPage<ContractResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ContractParam param){
        return param.getContractId();
    }

    private Page<ContractResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Contract getOldEntity(ContractParam param) {
        return this.getById(getKey(param));
    }

    private Contract getEntity(ContractParam param) {
        Contract entity = new Contract();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
