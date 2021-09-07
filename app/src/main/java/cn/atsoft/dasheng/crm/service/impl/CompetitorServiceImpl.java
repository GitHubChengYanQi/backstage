package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.mapper.CompetitorMapper;
import cn.atsoft.dasheng.crm.model.params.BusinessCompetitionParam;
import cn.atsoft.dasheng.crm.model.params.CompetitorParam;
import cn.atsoft.dasheng.crm.model.result.CompetitorResult;
import cn.atsoft.dasheng.crm.region.GetRegionService;
import cn.atsoft.dasheng.crm.region.RegionResult;
import cn.atsoft.dasheng.crm.service.BusinessCompetitionService;
import cn.atsoft.dasheng.crm.service.CompetitorQuoteService;
import cn.atsoft.dasheng.crm.service.CompetitorService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2021-09-07
 */
@Service
public class CompetitorServiceImpl extends ServiceImpl<CompetitorMapper, Competitor> implements CompetitorService {

    @Autowired
    private BusinessCompetitionService businessCompetitionService;

    @Autowired
    private CompetitorQuoteService competitorQuoteService;
    @Autowired
    private GetRegionService getRegionService;


    @Override
    public Competitor add(CompetitorParam param) {
        Competitor entity = getEntity(param);
        this.save(entity);
        if (param.getBusinessId() != null && entity.getCompetitorId() != null) {
            BusinessCompetitionParam businessCompetitionParam = new BusinessCompetitionParam();
            businessCompetitionParam.setBusinessId(param.getBusinessId());
            businessCompetitionParam.setCompetitorId(entity.getCompetitorId());
            businessCompetitionService.add(businessCompetitionParam);
        }


        return entity;

    }

    @Override
    public void delete(CompetitorParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(CompetitorParam param) {
        Competitor oldEntity = getOldEntity(param);
        Competitor newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CompetitorResult findBySpec(CompetitorParam param) {
        return null;
    }

    @Override
    public List<CompetitorResult> findListBySpec(CompetitorParam param) {
        return null;
    }

    @Override
    public PageInfo<CompetitorResult> findPageBySpec(CompetitorParam param) {
        Page<CompetitorResult> pageContext = getPageContext();
        IPage<CompetitorResult> page = this.baseMapper.customPageList(pageContext, param);

        for (CompetitorResult record : page.getRecords()) {

                RegionResult region = getRegionService.getRegion(record.getRegion());
                record.setRegionResult(region);

        }
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CompetitorParam param) {
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
