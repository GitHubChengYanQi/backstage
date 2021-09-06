package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.Contacts;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.mapper.CompetitorMapper;
import cn.atsoft.dasheng.crm.model.params.CompetitorParam;
import cn.atsoft.dasheng.crm.model.result.CompetitorResult;
import  cn.atsoft.dasheng.crm.service.CompetitorService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.model.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 竞争对手管理 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-09-06
 */

@Service

public class CompetitorServiceImpl extends ServiceImpl<CompetitorMapper, Competitor> implements CompetitorService {

    @Override
    public void add(CompetitorParam param){
        Competitor entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CompetitorParam param){
        Competitor getId=this.getById(param.getCompetitorId());
        if (ToolUtil.isEmpty(getId)){
            throw new ServiceException(500,"所删除目标不存在");
        }else {
            Competitor entity = getEntity(param);
            param.setDisplay(0);
            this.update(param);
        }
    }

    @Override
    public void update(CompetitorParam param){
        Competitor oldEntity = getOldEntity(param);
        Competitor newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CompetitorResult findBySpec(CompetitorParam param){
        return null;
    }

    @Override
    public List<CompetitorResult> findListBySpec(CompetitorParam param){
        return null;
    }

    @Override
    public PageInfo<CompetitorResult> findPageBySpec(CompetitorParam param){
        Page<CompetitorResult> pageContext = getPageContext();
        IPage<CompetitorResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CompetitorParam param){
        return param.getCompetitorId();
    }

    private Page<CompetitorResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private Competitor getOldEntity(CompetitorParam param) {
        return this.getById(getKey(param));
    }

    private Competitor getEntity(CompetitorParam param) {
        Competitor entity = new Competitor();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
