package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.ContractTempleteDetail;
import cn.atsoft.dasheng.crm.mapper.ContractTempleteDetailMapper;
import cn.atsoft.dasheng.crm.model.params.ContractTempleteDetailParam;
import cn.atsoft.dasheng.crm.model.result.ContractTempleteDetailResult;
import  cn.atsoft.dasheng.crm.service.ContractTempleteDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 自定义合同变量 服务实现类
 * </p>
 *
 * @author Captain_Jazz
 * @since 2022-04-18
 */
@Service
public class ContractTempleteDetailServiceImpl extends ServiceImpl<ContractTempleteDetailMapper, ContractTempleteDetail> implements ContractTempleteDetailService {

    @Override
    public void add(ContractTempleteDetailParam param){
        ContractTempleteDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ContractTempleteDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ContractTempleteDetailParam param){
        ContractTempleteDetail oldEntity = getOldEntity(param);
        ContractTempleteDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ContractTempleteDetailResult findBySpec(ContractTempleteDetailParam param){
        return null;
    }

    @Override
    public List<ContractTempleteDetailResult> findListBySpec(ContractTempleteDetailParam param){
        return null;
    }

    @Override
    public PageInfo<ContractTempleteDetailResult> findPageBySpec(ContractTempleteDetailParam param){
        Page<ContractTempleteDetailResult> pageContext = getPageContext();
        IPage<ContractTempleteDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ContractTempleteDetailParam param){
        return param.getContractTempleteDetailId();
    }

    private Page<ContractTempleteDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ContractTempleteDetail getOldEntity(ContractTempleteDetailParam param) {
        return this.getById(getKey(param));
    }

    private ContractTempleteDetail getEntity(ContractTempleteDetailParam param) {
        ContractTempleteDetail entity = new ContractTempleteDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
