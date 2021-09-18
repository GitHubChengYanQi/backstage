package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.ContractDetail;
import cn.atsoft.dasheng.app.mapper.ContractDetailMapper;
import cn.atsoft.dasheng.app.model.params.ContractDetailParam;
import cn.atsoft.dasheng.app.model.result.ContractDetailResult;
import  cn.atsoft.dasheng.app.service.ContractDetailService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 合同产品明细 服务实现类
 * </p>
 *
 * @author sb
 * @since 2021-09-18
 */
@Service
public class ContractDetailServiceImpl extends ServiceImpl<ContractDetailMapper, ContractDetail> implements ContractDetailService {

    @Override
    public void add(ContractDetailParam param){
        ContractDetail entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(ContractDetailParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(ContractDetailParam param){
        ContractDetail oldEntity = getOldEntity(param);
        ContractDetail newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public ContractDetailResult findBySpec(ContractDetailParam param){
        return null;
    }

    @Override
    public List<ContractDetailResult> findListBySpec(ContractDetailParam param){
        return null;
    }

    @Override
    public PageInfo<ContractDetailResult> findPageBySpec(ContractDetailParam param){
        Page<ContractDetailResult> pageContext = getPageContext();
        IPage<ContractDetailResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(ContractDetailParam param){
        return param.getId();
    }

    private Page<ContractDetailResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private ContractDetail getOldEntity(ContractDetailParam param) {
        return this.getById(getKey(param));
    }

    private ContractDetail getEntity(ContractDetailParam param) {
        ContractDetail entity = new ContractDetail();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
