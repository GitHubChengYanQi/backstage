package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.CrmBusiness;
import cn.atsoft.dasheng.app.model.params.CrmBusinessTrackParam;
import cn.atsoft.dasheng.app.model.result.CrmBusinessResult;
import cn.atsoft.dasheng.app.service.CrmBusinessService;
import cn.atsoft.dasheng.app.service.CrmBusinessTrackService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.entity.CompetitorQuote;
import cn.atsoft.dasheng.crm.mapper.CompetitorQuoteMapper;
import cn.atsoft.dasheng.crm.model.params.CompetitorQuoteParam;
import cn.atsoft.dasheng.crm.model.result.CompetitorQuoteResult;
import cn.atsoft.dasheng.crm.model.result.CompetitorResult;
import cn.atsoft.dasheng.crm.service.BusinessCompetitionService;
import  cn.atsoft.dasheng.crm.service.CompetitorQuoteService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.service.CompetitorService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.ARETURN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.OnError;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 竞争对手报价 服务实现类
 * </p>
 *
 * @author
 * @since 2021-09-07
 */
@Service
public class CompetitorQuoteServiceImpl extends ServiceImpl<CompetitorQuoteMapper, CompetitorQuote> implements CompetitorQuoteService {
    @Autowired
    private CompetitorQuoteService competitorQuoteService;

    @Autowired
    private CompetitorService competitorService;
    @Autowired
    private CrmBusinessService crmBusinessService;
    @Autowired
    private CrmBusinessTrackService crmBusinessTrackService;


    @Override
    public void add(CompetitorQuoteParam param) {
        CompetitorQuote entity = getEntity(param);
        this.save(entity);
        CrmBusinessTrackParam crmBusinessTrackParam = new CrmBusinessTrackParam();

            crmBusinessTrackParam.setBusinessId(entity.getBusinessId());
            crmBusinessTrackParam.setCompetitorsQuoteId(entity.getQuoteId());
            crmBusinessTrackParam.setCampType(param.getCampType());
            crmBusinessTrackService.add(crmBusinessTrackParam);







//        Map<String,Object> map = new HashMap<>();
//        map.put("business_id",param.getBusinessId());
//        List<CompetitorQuote> quoteList = competitorQuoteService.query().allEq(map).list();



    }

    @Override
    public void addTrack(CompetitorQuoteParam param) {
        CompetitorQuote entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CompetitorQuoteParam param) {
        param.setDisplay(0);
        this.update(param);
    }

    @Override
    public void update(CompetitorQuoteParam param) {

        CompetitorQuote oldEntity = getOldEntity(param);
        CompetitorQuote newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public CompetitorQuoteResult findBySpec(CompetitorQuoteParam param) {
        return null;
    }

    @Override
    public List<CompetitorQuoteResult> findListBySpec(CompetitorQuoteParam param) {
        return null;
    }

    @Override
    public PageInfo<CompetitorQuoteResult> findPageBySpec(CompetitorQuoteParam param) {
        Page<CompetitorQuoteResult> pageContext = getPageContext();

        IPage<CompetitorQuoteResult> page = this.baseMapper.customPageList(pageContext, param);
        format(page.getRecords());
        return PageFactory.createPageInfo(page);
    }

    private Serializable getKey(CompetitorQuoteParam param) {
        return param.getQuoteId();
    }

    private Page<CompetitorQuoteResult> getPageContext() {
        return PageFactory.defaultPage();
    }

    private CompetitorQuote getOldEntity(CompetitorQuoteParam param) {
        return this.getById(getKey(param));
    }

    private CompetitorQuote getEntity(CompetitorQuoteParam param) {
        CompetitorQuote entity = new CompetitorQuote();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }
  public void format (List<CompetitorQuoteResult> data){
      if(data.size() > 0) {
          List<Long> ids = new ArrayList<>();
          List<Long> business = new ArrayList<>();
          for (CompetitorQuoteResult datum : data) {
              ids.add(datum.getCompetitorId());
              business.add(datum.getBusinessId());
          }
          List<CrmBusiness> crmBusinessList = crmBusinessService.lambdaQuery().in(CrmBusiness::getBusinessId, business).list();
          List<Competitor> competitorList = competitorService.lambdaQuery().in(Competitor::getCompetitorId, ids).list();
          for (CompetitorQuoteResult datum : data) {
              for (Competitor competitor : competitorList) {
                  if (ToolUtil.isNotEmpty(datum.getCompetitorId()) && datum.getCompetitorId().equals(competitor.getCompetitorId())) {

                      CompetitorResult competitorResult = new CompetitorResult();
                      ToolUtil.copyProperties(competitor, competitorResult);
                      datum.setCompetitorResult(competitorResult);

                  }
              }
              for (CrmBusiness crmBusiness : crmBusinessList) {
                  if (ToolUtil.isNotEmpty(datum.getBusinessId()) && datum.getBusinessId().equals(crmBusiness.getBusinessId())) {

                      CrmBusinessResult crmBusinessResult = new CrmBusinessResult();
                      ToolUtil.copyProperties(crmBusiness, crmBusinessResult);
                      datum.setCrmBusinessResult(crmBusinessResult);

                  }
              }
          }
      }
  }

}
