package cn.atsoft.dasheng.crm.service.impl;


import cn.atsoft.dasheng.app.entity.CrmBusiness;
import cn.atsoft.dasheng.app.model.result.CrmBusinessResult;
import cn.atsoft.dasheng.app.service.CrmBusinessService;
import cn.atsoft.dasheng.base.pojo.page.PageFactory;
import cn.atsoft.dasheng.base.pojo.page.PageInfo;
import cn.atsoft.dasheng.crm.entity.Competitor;
import cn.atsoft.dasheng.crm.mapper.CompetitorMapper;
import cn.atsoft.dasheng.crm.model.params.CompetitorParam;
import cn.atsoft.dasheng.crm.model.result.CompetitorResult;
import cn.atsoft.dasheng.crm.service.CompetitorService;
import cn.atsoft.dasheng.core.util.ToolUtil;
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
 * 竞争对手管理 服务实现类
 * </p>
 *
 * @author
 * @since 2021-09-06
 */
@Service
public class CompetitorServiceImpl extends ServiceImpl<CompetitorMapper, Competitor> implements CompetitorService {
    @Autowired
    private CrmBusinessService businessService;

    @Override
    public void add(CompetitorParam param) {
        Competitor entity = getEntity(param);
        this.save(entity);
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
        format(page.getRecords());
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

    private void format(List<CompetitorResult> data) {
        List<Long> businessIds = new ArrayList<>();
        for (CompetitorResult datum : data) {
            businessIds.add(datum.getBusinessId());
        }
        if (businessIds.size() != 0) {
            List<CrmBusiness> businessList = businessIds.size() == 0 ? new ArrayList<>() : businessService.lambdaQuery().in(CrmBusiness::getBusinessId, businessIds).list();
            for (CompetitorResult datum : data) {
                for (CrmBusiness crmBusiness : businessList) {
                    if (crmBusiness.getBusinessId().equals(datum.getBusinessId())) {
                        CrmBusinessResult businessResult = new CrmBusinessResult();
                        ToolUtil.copyProperties(crmBusiness, businessResult);
                        datum.setBusinessResult(businessResult);
                        break;
                    }
                }
            }
        }

    }
}
