package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.entity.CompetitorQuote;
import cn.atsoft.dasheng.crm.mapper.CompetitorQuoteMapper;
import cn.atsoft.dasheng.crm.model.params.CompetitorQuoteParam;
import cn.atsoft.dasheng.crm.model.result.CompetitorQuoteResult;
import cn.atsoft.dasheng.crm.model.result.CompetitorResult;
import cn.atsoft.dasheng.crm.service.CompetitorQuoteService;
import cn.atsoft.dasheng.core.util.ToolUtil;
import cn.atsoft.dasheng.crm.service.CompetitorService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 竞争对手报价 服务实现类
 * </p>
 *
 * @author
 * @since 2021-09-06
 */
@Service
public class CompetitorQuoteServiceImpl extends ServiceImpl<CompetitorQuoteMapper, CompetitorQuote> implements CompetitorQuoteService {
    @Autowired
    private CompetitorService competitorService;


    @Override
    public void add(CompetitorQuoteParam param) {
        CompetitorQuote entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(CompetitorQuoteParam param) {
        this.removeById(getKey(param));
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
        return param.getCompetitorsQuoteId();
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

    public void format(List<CompetitorQuoteResult> data) {
        List<Long> ids = new ArrayList<>();
        for (CompetitorQuoteResult datum : data) {
            ids.add(datum.getCompetitorId());
        }
        List<Competitor> competitorList = ids.size() == 0 ? new ArrayList<>() : competitorService.lambdaQuery().in(Competitor::getCompetitorId).list();
        for (CompetitorQuoteResult datum : data) {
            for (Competitor competitor : competitorList) {
                if (datum.getCompetitorId().equals(competitor.getCompetitorId())) {
                    CompetitorResult competitorResult = new CompetitorResult();
                    ToolUtil.copyProperties(competitor, competitorResult);
                    datum.setCompetitorResult(competitorResult);
                    break;
                }
            }
        }
    }
}
