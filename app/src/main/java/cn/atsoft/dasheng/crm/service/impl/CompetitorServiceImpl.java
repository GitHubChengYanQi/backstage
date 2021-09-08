package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.CrmBusiness;
import cn.atsoft.dasheng.app.entity.Customer;
import cn.atsoft.dasheng.app.model.result.CustomerResult;
import cn.atsoft.dasheng.app.service.CrmBusinessService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.BusinessCompetition;
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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.tools.Tool;
import java.io.Serializable;
import java.util.ArrayList;
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
    @Autowired
    private CrmBusinessService crmBusinessService;


    @Override
    public void add(CompetitorParam param) {
        Competitor entity = getEntity(param);
        this.save(entity);
        if (param.getBusinessId() != null && entity.getCompetitorId() != null) {
            BusinessCompetitionParam businessCompetitionParam = new BusinessCompetitionParam();
            businessCompetitionParam.setBusinessId(param.getBusinessId());
            businessCompetitionParam.setCompetitorId(entity.getCompetitorId());
            businessCompetitionService.add(businessCompetitionParam);
        }



    }

    @Override
    public void delete(CompetitorParam param) {
        this.removeById(getKey(param));
    }

    @Override
    public void update(CompetitorParam param) {
        if (ToolUtil.isNotEmpty(param.getCompetitorId())) {
            QueryWrapper<BusinessCompetition> queryWrapper=new QueryWrapper<>();
            queryWrapper.in("competitor_id",param.getCompetitorId());
            List<BusinessCompetition> list = businessCompetitionService.list(queryWrapper);
            if (list.size()>0){
                BusinessCompetition businessCompetition = businessCompetitionService.getById(list.get(0).getBusinessCompetitionId());
                businessCompetition.setBusinessId(param.getBusinessId());
                businessCompetitionService.updateById(businessCompetition);
            }

        }
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

        Long businessId = param.getBusinessId();
        Page<CompetitorResult> pageContext = getPageContext();
        IPage<CompetitorResult> page = this.baseMapper.customPageList(pageContext, param);

        if (ToolUtil.isNotEmpty(businessId)){
            QueryWrapper<BusinessCompetition> businessCompetitionQueryWrapper = new QueryWrapper<>();
            businessCompetitionQueryWrapper.in("business_id",businessId);
            List<BusinessCompetition> list = businessCompetitionService.list(businessCompetitionQueryWrapper);
            List<Long> longs = new ArrayList<>();
            for (BusinessCompetition businessCompetition : list) {
                longs.add(businessCompetition.getCompetitorId());
            }
            QueryWrapper<Competitor> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("competitor_id",longs);
            List<Competitor> competitorList = this.list(queryWrapper);

            List<CompetitorResult> competitorResultList = new ArrayList<>();
            for (Competitor competitor : competitorList) {
                CompetitorResult competitorResult = new CompetitorResult();
                ToolUtil.copyProperties(competitor, competitorResult);
                competitorResultList.add(competitorResult);
            }

            if (competitorResultList.size()>0){
                page.setRecords(competitorResultList);
            }
        }



        for (CompetitorResult record : page.getRecords()) {
            if (record.getCompetitionLevel() != null) {
                switch (record.getCompetitionLevel()) {
                    case 1:
                        record.setLevel("低");
                        break;
                    case 2:
                        record.setLevel("中");
                        break;
                    case 3:
                        record.setLevel("高");
                        break;
                    default:
                        break;
                }
            }

            QueryWrapper<BusinessCompetition> crmBusinessQueryWrapper = new QueryWrapper<>();
            crmBusinessQueryWrapper.in("competitor_id",record.getCompetitorId());
            List<BusinessCompetition> list = businessCompetitionService.list(crmBusinessQueryWrapper);
            List<Long> bussinessId = new ArrayList<>();
            for (BusinessCompetition businessCompetition : list) {
                bussinessId.add(businessCompetition.getBusinessId());
            }
            QueryWrapper<CrmBusiness> crmBusinessQueryWrapper1 = new QueryWrapper<>();
            crmBusinessQueryWrapper1.in("business_id",bussinessId);
            List<CrmBusiness> list1 = bussinessId.size() == 0 ? new ArrayList<>() : crmBusinessService.list(crmBusinessQueryWrapper1);
            record.setCrmBusinessList(list1);

            if (ToolUtil.isNotEmpty(record.getRegion())) {
                RegionResult region = getRegionService.getRegion(record.getRegion());
                record.setRegionResult(region);
            }


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

    @Override
    public CompetitorResult detail(Long id) {
        Competitor competitor = this.getById(id);
        CompetitorResult competitorResult = new CompetitorResult();
        ToolUtil.copyProperties(competitor, competitorResult);
        List<CompetitorResult> results = new ArrayList<CompetitorResult>() {{
            add(competitorResult);
        }};

        QueryWrapper<BusinessCompetition> crmBusinessQueryWrapper = new QueryWrapper<>();
        crmBusinessQueryWrapper.in("competitor_id",id);
        List<BusinessCompetition> list = businessCompetitionService.list(crmBusinessQueryWrapper);
        List<Long> bussinessId = new ArrayList<>();
        for (BusinessCompetition businessCompetition : list) {
            bussinessId.add(businessCompetition.getBusinessId());
        }
        QueryWrapper<CrmBusiness> crmBusinessQueryWrapper1 = new QueryWrapper<>();
        crmBusinessQueryWrapper1.in("business_id",bussinessId);
        List<CrmBusiness> list1 = bussinessId.size() == 0 ? new ArrayList<>() : crmBusinessService.list(crmBusinessQueryWrapper1);
        competitorResult.setCrmBusinessList(list1);

        if (ToolUtil.isNotEmpty(competitorResult.getCompetitionLevel())) {
            switch (competitorResult.getCompetitionLevel()) {
                case 1:
                    competitorResult.setLevel("低");
                    break;
                case 2:
                    competitorResult.setLevel("中");
                    break;
                case 3:
                    competitorResult.setLevel("高");
                    break;
                default:
                    break;
            }
        }
            if (ToolUtil.isNotEmpty(competitorResult.getRegion())) {
                RegionResult region = getRegionService.getRegion(competitorResult.getRegion());
                competitorResult.setRegionResult(region);
            }
        return results.get(0);
    }

}
