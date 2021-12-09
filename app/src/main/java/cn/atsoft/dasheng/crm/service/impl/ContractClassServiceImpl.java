package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContractClass;
import cn.atsoft.dasheng.crm.mapper.ContractClassMapper;
import cn.atsoft.dasheng.crm.model.params.ContractClassParam;
import cn.atsoft.dasheng.crm.model.result.ContractClassResult;
import  cn.atsoft.dasheng.crm.service.ContractClassService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 合同分类 服务实现类
 * </p>
 *
 * @author song
 * @since 2021-12-09
 */
@Service
public class ContractClassServiceImpl extends ServiceImpl<ContractClassMapper, ContractClass> implements ContractClassService {

    @Override
    public void add(ContractClassParam param){
        ContractClass entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ContractClassParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ContractClassParam param){
        ContractClass oldEntity = getOldEntity(param);
        ContractClass newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ContractClassResult findBySpec(ContractClassParam param){
        return null;
    }

    @Override
    public List<ContractClassResult> findListBySpec(ContractClassParam param){
        return null;
    }

    @Override
    public PageInfo<ContractClassResult> findPageBySpec(ContractClassParam param){
        Page<ContractClassResult> pageContext = getPageContext();
        IPage<ContractClassResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ContractClassParam param){
        return param.getContractClassId();
    }

    private Page<ContractClassResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ContractClass getOldEntity(ContractClassParam param) {
        return this.getById(getKey(param));
    }

    private ContractClass getEntity(ContractClassParam param) {
        ContractClass entity = new ContractClass();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
