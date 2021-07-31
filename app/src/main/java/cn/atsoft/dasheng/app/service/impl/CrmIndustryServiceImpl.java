package cn.atsoft.dasheng.app.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.app.entity.CrmIndustry;
import cn.atsoft.dasheng.app.mapper.CrmIndustryMapper;
import cn.atsoft.dasheng.app.model.params.CrmIndustryParam;
import cn.atsoft.dasheng.app.model.result.CrmIndustryResult;
import  cn.atsoft.dasheng.app.service.CrmIndustryService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 行业表 服务实现类
 * </p>
 *
 * @author 
 * @since 2021-07-31
 */
@Service
public class CrmIndustryServiceImpl extends ServiceImpl<CrmIndustryMapper, CrmIndustry> implements CrmIndustryService {

    @Override
    public void add(CrmIndustryParam param){
        CrmIndustry entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CrmIndustryParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(CrmIndustryParam param){
        CrmIndustry oldEntity = getOldEntity(param);
        CrmIndustry newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CrmIndustryResult findBySpec(CrmIndustryParam param){
        return null;
    }

    @Override
    public List<CrmIndustryResult> findListBySpec(CrmIndustryParam param){
        return null;
    }

    @Override
    public PageInfo<CrmIndustryResult> findPageBySpec(CrmIndustryParam param){
        Page<CrmIndustryResult> pageContext = getPageContext();
        IPage<CrmIndustryResult> page = this.baseMapper.customPageList(pageContext, param);
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CrmIndustryParam param){
        return param.getIndustryId();
    }

    private Page<CrmIndustryResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CrmIndustry getOldEntity(CrmIndustryParam param) {
        return this.getById(getKey(param));
    }

    private CrmIndustry getEntity(CrmIndustryParam param) {
        CrmIndustry entity = new CrmIndustry();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
