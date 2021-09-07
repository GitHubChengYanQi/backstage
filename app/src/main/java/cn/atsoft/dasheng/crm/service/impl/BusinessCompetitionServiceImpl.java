package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.BusinessCompetition;
import cn.atsoft.dasheng.crm.mapper.BusinessCompetitionMapper;
import cn.atsoft.dasheng.crm.model.params.BusinessCompetitionParam;
import cn.atsoft.dasheng.crm.model.result.BusinessCompetitionResult;
import  cn.atsoft.dasheng.crm.service.BusinessCompetitionService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商机 竞争对手 绑定 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-09-07
 */
@Service
public class BusinessCompetitionServiceImpl extends ServiceImpl<BusinessCompetitionMapper, BusinessCompetition> implements BusinessCompetitionService {

    @Override
    public void add(BusinessCompetitionParam param){
        BusinessCompetition entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(BusinessCompetitionParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(BusinessCompetitionParam param){
        BusinessCompetition oldEntity = getOldEntity(param);
        BusinessCompetition newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public BusinessCompetitionResult findBySpec(BusinessCompetitionParam param){
        return null;
    }

    @Override
    public List<BusinessCompetitionResult> findListBySpec(BusinessCompetitionParam param){
        return null;
    }

    @Override
    public PageInfo<BusinessCompetitionResult> findPageBySpec(BusinessCompetitionParam param){
        Page<BusinessCompetitionResult> pageContext = getPageContext();
        IPage<BusinessCompetitionResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(BusinessCompetitionParam param){
        return param.getBusinessCompetitionId();
    }

    private Page<BusinessCompetitionResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private BusinessCompetition getOldEntity(BusinessCompetitionParam param) {
        return this.getById(getKey(param));
    }

    private BusinessCompetition getEntity(BusinessCompetitionParam param) {
        BusinessCompetition entity = new BusinessCompetition();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
