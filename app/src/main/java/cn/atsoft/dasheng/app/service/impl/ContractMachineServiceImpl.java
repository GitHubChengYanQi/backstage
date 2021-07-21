package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ContractMachine;
import cn.atsoft.dasheng.app.mapper.ContractMachineMapper;
import cn.atsoft.dasheng.app.model.params.ContractMachineParam;
import cn.atsoft.dasheng.app.model.result.ContractMachineResult;
import  cn.atsoft.dasheng.app.service.ContractMachineService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 机床合同表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-20
 */
@Service
public class ContractMachineServiceImpl extends ServiceImpl<ContractMachineMapper, ContractMachine> implements ContractMachineService {

    @Override
    public void add(ContractMachineParam param){
        ContractMachine entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ContractMachineParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ContractMachineParam param){
        ContractMachine oldEntity = getOldEntity(param);
        ContractMachine newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ContractMachineResult findBySpec(ContractMachineParam param){
        return null;
    }

    @Override
    public List<ContractMachineResult> findListBySpec(ContractMachineParam param){
        return null;
    }

    @Override
    public PageInfo<ContractMachineResult> findPageBySpec(ContractMachineParam param){
        Page<ContractMachineResult> pageContext = getPageContext();
        IPage<ContractMachineResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ContractMachineParam param){
        return param.getContractId();
    }

    private Page<ContractMachineResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ContractMachine getOldEntity(ContractMachineParam param) {
        return this.getById(getKey(param));
    }

    private ContractMachine getEntity(ContractMachineParam param) {
        ContractMachine entity = new ContractMachine();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
